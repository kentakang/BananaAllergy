<%@ page contentType="text/html;charset=UTF-8" language="java" import="com.kentakang.bananaallergy.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.StringTokenizer" %>
<html>
    <head>
        <title>BananaAllergy</title>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css" integrity="sha384-9gVQ4dYFwwWSjIDZnLEWnxCjeSWFphJiwGPXr1jddIhOegiu1FwO5qRGvFXOdJZ4" crossorigin="anonymous">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <style>
            :root {
                --jumbotron-padding-y: 3rem;
            }

            .jumbotron {
                padding-top: var(--jumbotron-padding-y);
                padding-bottom: var(--jumbotron-padding-y);
                margin-bottom: 0;
                background-color: #fff;
            }
            @media (min-width: 768px) {
                .jumbotron {
                    padding-top: calc(var(--jumbotron-padding-y) * 2);
                    padding-bottom: calc(var(--jumbotron-padding-y) * 2);
                }
            }

            .jumbotron p:last-child {
                margin-bottom: 0;
            }

            .jumbotron-heading {
                font-weight: 300;
            }

            .jumbotron .container {
                max-width: 40rem;
            }

            .card {
                height: 22rem;
            }

            footer {
                padding-top: 3rem;
                padding-bottom: 3rem;
                text-align: center;
            }

            footer p {
                margin-bottom: .25rem;
            }

            .box-shadow { box-shadow: 0 .25rem .75rem rgba(0, 0, 0, .05); }
        </style>
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
                            <a href="http://tv.naver.com<%=videoLink%>" target="_blank"><img class="card-img-top" src="<%=imgFile%>" alt="<%=title%>"></a>
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
