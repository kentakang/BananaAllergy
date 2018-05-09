# BananaAllergy
[BananaAllergy](https://bam.kentakang.com)는 네이버 TV 영상을 좀 더 편리하게 보기 위해 개발한 서비스입니다.
## 기능
네이버 TV에서 바나나 알러지 원숭이의 영상을 가져와, 방송사 음악 프로그램에서 업로드 한 영상만 선별해냅니다.
## Library
[JAX-WS-Apache Axis](https://axis.apache.org/axis2/java/core/index.html) [The Apache Software Foundation](http://www.apache.org).  
[json-simple](https://code.google.com/archive/p/json-simple/) Yidong Fang.  
[PostgreSQL JDBC Driver](https://jdbc.postgresql.org/) PostgreSQL Global Development Group.  
[jsoup](https://jsoup.org) © 2019 - 2018 [Jonathan Hedley](https://jhy.io).
## 설치 방법
**BananaAllergy는 PostgreSQL 데이터베이스를 사용합니다. 서버에 PostgreSQL 서버가 설치되어 있어야 사용할 수 있습니다.**
1. Release에서 최신 버전 .war 파일을 다운받아 주세요.
2. 톰캣 서버의 webapps 디렉토리에 .war 파일을 넣어 주세요.
3. 아래와 같이 setting.json 파일을 생성한 뒤 데이터베이스 설정을 해주세요.  
```
{
    "dbUrl" : "jdbc:postgresql://localhost/DB명"
    "username" : "PostgreSQL 사용자명"
    "password" : "해당 사용자의 패스워드"
}
```
## License
MIT © [kentakang](http://kentakang.com)
