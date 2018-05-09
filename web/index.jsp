<%@ page contentType="text/html;charset=UTF-8" language="java" import="com.kentakang.bananaallergy.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.StringTokenizer" %>
<html>
    <head>
        <title>BananaAllergy</title>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css" integrity="sha384-9gVQ4dYFwwWSjIDZnLEWnxCjeSWFphJiwGPXr1jddIhOegiu1FwO5qRGvFXOdJZ4" crossorigin="anonymous">
        <link rel="stylesheet" href="./resource/css/bootstrap.custom.css">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    </head>
    <body>
        <section class="jumbotron text-center">
            <div class="container">
                <h1 class="jumbotron-heading">바나나 알러지 원숭이</h1>
                <p class="lead text-muted">나는 바나나 알러지 원숭이</p>
            </div>
        </section>
        <div class="album py-5 bg-light">
            <div class="container">
                <div class="row">
                    <%
                        ListParser parser = new ListParser();
                        ImageProcessor img = new ImageProcessor();
                        parser.dbInit();

                        ArrayList<String> list = parser.getList(false);
                        Iterator<String> it = list.iterator();

                        while (it.hasNext()) {
                            StringTokenizer st = new StringTokenizer(it.next(), ",");
                            String uploader = st.nextToken();
                            String title = st.nextToken();
                            String imgSrc = st.nextToken();
                            String videoLink = st.nextToken();
                            imgSrc = imgSrc.substring(0, imgSrc.lastIndexOf("?"));
                            String imgFile = imgSrc.substring(imgSrc.lastIndexOf('/') + 1, imgSrc.length());

                            if (img.checkImg(imgSrc) == false) {
                                img.ImgDownload(imgSrc);
                            }
                    %>
                    <div class="col-md-4 text-center">
                        <div class="card mb-4 box-shadow">
                            <a href="http://tv.naver.com<%=videoLink%>" target="_blank"><img class="card-img-top" src="./resource/images/<%=imgFile%>" alt="<%=title%>"></a>
                            <div class="card-body">
                                <a href="http://tv.naver.com<%=videoLink%>" target="_blank"><h5 class="card-title"><%=title%></h5></a>
                                <p class="card-text"><%=uploader%></p>
                            </div>
                        </div>
                    </div>
                        <%
                            }
                        %>
                </div>
            </div>
        </div>
        <footer class="text-muted">
            <div class="container">
                <p>
                    Copyright by <a href="https://github.com/kentakang">@kentakang</a> All rights reserved.<br>
                    사이트 관련 문의는 <a href="mailto:kentakang@icloud.com">kentakang@icloud.com</a> 로 메일 보내주세요.
                </p>
            </div>
        </footer>
    </body>
</html>
