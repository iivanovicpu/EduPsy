
CREATE TABLE user (
  id int PRIMARY KEY auto_increment,
  username text not null,
  password text not null,
  firstName text not null,
  lastname text,
  email text,
  status text CHECK (status IN ('ADMIN','TEACHER','STUDENT'))
);

CREATE TABLE subject (
  id  int PRIMARY KEY auto_increment,
  title text not null,
  keywords text,
  url text
);

/* predmeti */
INSERT INTO subject (title, keywords, url) VALUES ('Edu Psy - learning system', 'edupsy,learning,system','/materijali/edupsy.html');
INSERT INTO subject (title, keywords, url) VALUES ('Relacijske baze podataka', 'baze podataka,ralacije,sql','/materijali/relacijskebazepodataka.html');

/* korisnici */
INSERT INTO user (username, password, firstName, lastname, email, status) VALUES ('iivanovic','password','Igor','Ivanović','iivanovic.pu@gmail.com','ADMIN');
INSERT INTO user (username, password, firstName, lastname, email, status) VALUES ('jzufic','password','Janko','Žufić','janko.zufic@gmail.com','TEACHER');
INSERT INTO user (username, password, firstName, lastname, email, status) VALUES ('mmarkovic','password','Marko','Marković','janko.zufic@gmail.com','STUDENT');
INSERT INTO user (username, password, firstName, lastname, email, status) VALUES ('iivic','password','Ivo','Ivić','janko.zufic@gmail.com','STUDENT');
INSERT INTO user (username, password, firstName, lastname, email, status) VALUES ('vnovak','password','Vjenceslav','Novak','janko.zufic@gmail.com','STUDENT');



