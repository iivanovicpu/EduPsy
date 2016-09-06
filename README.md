# EduPsy learning system (Web application)

Project prototype "Edu-Psy" learning system provides a customized learning based on pedagogical-psychological profile of students.

Main features:
* multi user platform
* three type of user context (admin, teacher, student)
* different presentation of same learning materilals based on pp profile
* managing learning materials
* managing students achievement

Project based on these technologies:

* [Java (1.8)](https://www.oracle.com/java/) - general programming language
* [Spark framework](http://sparkjava.com/) - creating dynamic Web pages, templating etc.
* [sql2o](http://www.sql2o.org/) - working with stored data
* [bootstrap](http://getbootstrap.com/) for responsive Web UI design

Running application:
requirements:
- java jdk 1.8
- git
- maven (ver. 3.x.x)

Manual:
1. clone repository
```git clone https://github.com/iivanovicpu/EduPsy.git```
2. change directory:
```cd EduPsy```
3. compile project:
```mvn -cpu compile```
4. running application:
```mvn -cpu exec:java -Dexec.mainClass="hr.iivanovic.psyedu.Application" ```
5. from your browser go to address:
```http://localhost:4567```