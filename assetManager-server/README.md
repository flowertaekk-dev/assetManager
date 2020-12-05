# assetManager/server

## 환경설정

### Java version

```
openjdk 11.0.9 2020-10-20
OpenJDK Runtime Environment (build 11.0.9+11-Ubuntu-0ubuntu1.20.04)
OpenJDK 64-Bit Server VM (build 11.0.9+11-Ubuntu-0ubuntu1.20.04, mixed mode, sharing)
```

* 우분투가 아니어도 상관은 없지만, jdk는 11로!

### 프로젝트 환경구축

1. `$git clone` 한다
2. `./src/main/resources`에 `application-email.properties` 를 추가한다.
```
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=<본인 이메일>@gmail.com
spring.mail.password=<이메일 비밀번호>
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```
3. `./src/main/resources`에 `application-mariadbdev.properties` 를 추가한다.
```
spring.datasource.url=jdbc:mariadb://localhost:3306/assetManager
spring.datasource.driver-class-name=org.mariadb.jdbc.Driver
spring.datasource.username=<DB 유저명>
spring.datasource.password=pass
spring.datasource.jpa.open-in-view=false
spring.datasource.jpa.generate-ddl=true
spring.datasource.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update
```
4. gradle 초기화 작업을 한다
    * 윈도우인 경우 :  `$./gradlew.bat build`
    * 리눅스인 경우 :  `$./gradlew build`
5. 문제가 없는지 테스트를 돌려본다.
    * 윈도우인 경우 :  `$./gradlew.bat test`
    * 리눅스인 경우 :  `$./gradlew test`


### DataBase 환경구축

1. mariadb-server를 설치한다.
    * 현재 @flowertaekk-dev가 사용하고 있는 버전은 `10.3.25-MariaDB-0ubuntu0.20.04.1 Ubuntu 20.04`
        * 호환성 문제 없는 버전이면 어느 버전이든 상관없다!
2. mariadb 접속
    * 윈도우인 경우 : TBD
    * 리눅스인 경우 : `$sudo mysql`
3. 데이터베이스 만들기
    * `> CREATE DATABASE assetManager;`
4. 데이터베이스 접속하기
    * `> use assetManager;`
5. 데이터베이스 접속권한 설정하기
    * `> GRANT ALL *.* TO '<user name>'@'%'`
    * `> FLUSH PRIVILEGES;`
6. 테이블 생성은 JPA 에게 맡긴다.


