package com.kentakang.bananaallergy;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.sql.*;

public class ListParser {
    private String url = "http://tv.naver.com/search/clip?query=바나나 알러지 원숭이&sort=date&page=1&isTag=false";
    private String[] uploaders = {"SBS 인기가요", "쇼 음악중심", "뮤직뱅크", "Show Champion", "M COUNTDOWN", "더 쇼 (THE SHOW)", "WM 엔터테인먼트"};

    private String dbUrl = null;
    private String user = null;
    private String password = null;

    public void dbInit() {
        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(new FileReader("../webapps/BananaAllergy/setting.json"));
            JSONObject JSONobj = (JSONObject) obj;

            dbUrl = (String) JSONobj.get("dbUrl");
            user = (String) JSONobj.get("username");
            password = (String) JSONobj.get("password");
        } catch (FileNotFoundException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        } catch (ParseException e) {
            System.out.println(e);
        }
    }

    public int recentVideo() throws IOException {
        Document document = Jsoup.connect(url).get();
        Elements endBtn = document.select(".next_end");
        int endPage = Integer.parseInt(endBtn.attr("data-page"));

        for (int i = 1; i <= endPage; i++) {
            Document searchResult = Jsoup.connect(
                    "http://tv.naver.com/search/clip?query=바나나 알러지 원숭이&sort=date&page=" + i + "&isTag=false").get();
            Elements titles = searchResult.select(".thl_a");

            for (Element e : titles) {
                String videoLink = e.select(".cds_thm").attr("href");
                String uploader = e.select(".inner .ch_txt").text();

                for (int j = 0; j < uploaders.length; j++) {
                    if (uploader.equals(uploaders[j])) {
                        return Integer.parseInt(videoLink.substring(videoLink.lastIndexOf("/") + 1, videoLink.lastIndexOf("?")));
                    }
                }
            }
        }

        return 0;
    }

    public ArrayList<String> getListFromWeb(boolean getFancam) throws IOException {
        Document document = Jsoup.connect(url).get();
        Elements endBtn = document.select(".next_end");
        int endPage = Integer.parseInt(endBtn.attr("data-page"));

        for (int i = 1; i <= endPage; i++) {
            Document searchResult = Jsoup.connect(
                    "http://tv.naver.com/search/clip?query=바나나 알러지 원숭이&sort=date&page=" + i + "&isTag=false").get();
            Elements titles = searchResult.select(".thl_a");
            String query = "INSERT INTO video_list (title, uploader, imgsrc, videolink, idx) VALUES (?, ?, ?, ?, ?)";

            for (Element e: titles) {
                String title = e.select(".inner dt").text();
                String uploader = e.select(".inner .ch_txt").text();
                String imgSrc = e.select("img").attr("src");
                String videoLink = e.select(".cds_thm").attr("href");

                for (int j = 0; j < uploaders.length; j++) {
                    if (uploader.equals(uploaders[j])) {
                        if (!getFancam) {
                            if (title.contains("직캠")) {
                                continue;
                            }
                        }

                        try {
                            Connection conn = DriverManager.getConnection(dbUrl, user, password);
                            Statement st = conn.createStatement();
                            ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM video_list WHERE idx = " + Integer.parseInt(videoLink.substring(videoLink.lastIndexOf("/") + 1, videoLink.lastIndexOf("?"))));

                            if (rs.next()) {
                                if (rs.getInt(1) != 0)
                                    continue;
                            }

                            PreparedStatement pst = conn.prepareStatement(query);
                            pst.setString(1, title);
                            pst.setString(2, uploader);
                            pst.setString(3, imgSrc);
                            pst.setString(4, videoLink);
                            pst.setInt(5, Integer.parseInt(videoLink.substring(videoLink.lastIndexOf("/") + 1, videoLink.lastIndexOf("?"))));
                            pst.executeUpdate();
                        } catch (SQLException sqlException) {
                            System.out.println(sqlException);
                        }

                        break;
                    }
                }
            }
        }

        return getList(getFancam);
    }

    public ArrayList<String> getList(boolean getFancam) throws IOException {
        ArrayList<String> resultList = new ArrayList<>();
        boolean isRecent = false;

        try {
            Connection conn = DriverManager.getConnection(dbUrl, user, password);
            Statement state = conn.createStatement();
            ResultSet rs = state.executeQuery("SELECT count(*) FROM video_list WHERE idx = " + recentVideo());

            if (rs.next()) {
                if (rs.getInt(1) != 0) {
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
                ResultSet rs = state.executeQuery("SELECT * FROM video_list ORDER BY idx DESC");

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
