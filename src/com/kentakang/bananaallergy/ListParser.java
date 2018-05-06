package com.kentakang.bananaallergy;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.sql.*;

public class ListParser {
    private String url = "http://tv.naver.com/search/clip?query=바나나 알러지 원숭이&sort=date&page=1&isTag=false";
    private String[] uploaders = {"SBS 인기가요", "쇼 음악중심", "뮤직뱅크", "Show Champion", "M COUNTDOWN", "더 쇼 (THE SHOW)", "WM 엔터테인먼트"};

    String dbUrl = "jdbc:postgresql://localhost/bam";
    String user = "username";
    String password = "password";

    public String recentVideo() throws IOException {
        Document document = Jsoup.connect(url).get();
        Elements endBtn = document.select(".next_end");
        int endPage = Integer.parseInt(endBtn.attr("data-page"));

        for (int i = 1; i <= endPage; i++) {
            Document searchResult = Jsoup.connect(
                    "http://tv.naver.com/search/clip?query=바나나 알러지 원숭이&sort=date&page=" + i + "&isTag=false").get();
            Elements titles = searchResult.select(".thl_a");

            for (Element e : titles) {
                String title = e.select(".inner dt").text();
                String uploader = e.select(".inner .ch_txt").text();

                for (int j = 0; j < uploaders.length; j++) {
                    if (uploader.equals(uploaders[j])) {
                        return title;
                    }
                }
            }
        }

        return "Not Found";
    }

    public ArrayList<String> getListFromWeb(boolean getFancam) throws IOException {
        Document document = Jsoup.connect(url).get();
        Elements endBtn = document.select(".next_end");
        int endPage = Integer.parseInt(endBtn.attr("data-page"));
        ArrayList<String> resultList = new ArrayList<>();

        for (int i = 1; i <= endPage; i++) {
            Document searchResult = Jsoup.connect(
                    "http://tv.naver.com/search/clip?query=바나나 알러지 원숭이&sort=date&page=" + i + "&isTag=false").get();
            Elements titles = searchResult.select(".thl_a");
            String query = "INSERT INTO video_list (title, uploader, imgsrc, videolink) VALUES (?, ?, ?, ?)";

            for (Element e: titles) {
                String title = e.select(".inner dt").text();
                String uploader = e.select(".inner .ch_txt").text();
                String imgSrc = e.select("img").attr("src");
                String videoLink = e.select(".cds_thm").attr("href");
                int idx = 1;

                for (int j = 0; j < uploaders.length; j++) {
                    if (uploader.equals(uploaders[j])) {
                        if (!getFancam) {
                            if (title.contains("직캠")) {
                                continue;
                            }
                        }

                        resultList.add(uploader + "," + title + "," + imgSrc + "," + videoLink);
                        try (Connection conn =DriverManager.getConnection(dbUrl,user,password);
                            PreparedStatement pst = conn.prepareStatement(query)) {

                            pst.setString(1, title);
                            pst.setString(2, uploader);
                            pst.setString(3, imgSrc);
                            pst.setString(4, videoLink);
                            pst.executeUpdate();
                        } catch (SQLException sqlException) {
                            System.out.println(sqlException);
                        }

                        break;
                    }
                }
            }
        }

        return resultList;
    }

    public ArrayList<String> getList(boolean getFancam) throws IOException {
        ArrayList<String> resultList = new ArrayList<>();
        boolean isRecent = false;

        try {
            Connection conn = DriverManager.getConnection(dbUrl, user, password);
            Statement state = conn.createStatement();
            ResultSet rs = state.executeQuery("SELECT * FROM video_list LIMIT 1");
            String title = null;

            if (rs.next()) {
                title = rs.getString("title");

                if (title.equals(recentVideo())) {
                    isRecent = true;
                }
            }
        } catch (SQLException except) {
            System.out.println(except);
        }

        if (isRecent) {
            try {
                Connection conn = DriverManager.getConnection(dbUrl, user, password);
                Statement state = conn.createStatement();
                ResultSet rs = state.executeQuery("SELECT * FROM video_list");

                while (rs.next()) {
                    resultList.add(rs.getString("uploader") + "," + rs.getString("title") + "," + rs.getString("imgsrc") + "," + rs.getString("videolink"));
                }
            } catch (SQLException except) {
                System.out.println(except);
            }
        } else {
            return getListFromWeb(getFancam);
        }

        return resultList;
    }
}
