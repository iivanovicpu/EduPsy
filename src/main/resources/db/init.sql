
CREATE TABLE user (
  id int PRIMARY KEY auto_increment,
  username text not null,
  password text not null,
  firstName text not null,
  lastname text,
  email text
);

CREATE TABLE subject (
  id  int PRIMARY KEY auto_increment,
  title text not null,
  keywords text,
  url text
);

INSERT INTO subject (title, keywords, url) VALUES ('Edu Psy - learning system', 'edupsy,learning,system','/materijali/edupsy.html');
INSERT INTO subject (title, keywords, url) VALUES ('Relacijske baze podataka', 'baze podataka,ralacije,sql','/materijali/relacijskebazepodataka.html');
INSERT INTO subject (title, keywords, url) VALUES ('Edu Psy - learning system', 'edupsy,learning,system','/materijali/edupsy.html');

CREATE TABLE IF NOT EXISTS courses (
   id int PRIMARY KEY auto_increment,
   name VARCHAR,
   url VARCHAR
);

CREATE TABLE IF NOT EXISTS reviews (
   id INTEGER PRIMARY KEY auto_increment,
   course_id INTEGER,
   rating INTEGER,
   comment VARCHAR,
   FOREIGN KEY(course_id) REFERENCES public.courses(id)
);