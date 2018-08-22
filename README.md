# EduPsy learning system (Web application)

Application EduPsy is based on idea to provide customized learning system to students, and managing students achievements and learning materials by teachers.

#### Project based on these technologies:

* [Java (1.8)](https://www.oracle.com/java/) - general programming language
* [Spark framework](http://sparkjava.com/) - creating dynamic Web pages, templating etc.
* [sql2o](http://www.sql2o.org/) - working with stored data
* [H2 Database engine](http://h2database.com) - H2 in memory database (in development mode)
* [PostgreSQL Database](https://www.postgresql.org/) - production database
* [Jsoup](http://jsoup.org/) - parsing HTML
* [jQuery](https://jquery.com/) - javascript library for easy dynamic content managing on pages
* [bootstrap](http://getbootstrap.com/) for responsive Web UI design

#### Currently implemented features:
* localisation support
* internal rest API for main functions
* essential security (authentication and authorisation based on username and password)
* three type of user context (admin, teacher, student)
* users and learning subjects persisting in database (h2 in memory database)
* managing learning materials
* learning log system
* different presentations of learning materials

#### Running application:
##### requirements:
- java jdk 1.8
- git
- maven (ver. 3.x.x)
- postgreSQL database

##### Manual:
1. clone repository
```git clone https://github.com/iivanovicpu/EduPsy.git```
2. change directory:
```cd EduPsy```
3. compile project:
```mvn -cpu compile```
4. running application:
```mvn -cpu exec:java -Dexec.mainClass="hr.iivanovic.psyedu.Application" ```
5. from your browser go to address:
[http://localhost:4567](http://localhost:4567)