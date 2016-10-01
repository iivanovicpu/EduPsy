
CREATE TABLE IF NOT EXISTS USER(
  id int PRIMARY KEY auto_increment,
  username TEXT NOT NULL,
  password TEXT NOT NULL,
  firstName TEXT NOT NULL,
  lastname TEXT,
  email TEXT,
  status TEXT CHECK (status IN ('ADMIN','TEACHER','STUDENT'))
);

CREATE TABLE IF NOT EXISTS SUBJECT(
  id  INT PRIMARY KEY AUTO_INCREMENT,
  title TEXT NOT NULL,
  keywords TEXT,
  url TEXT
);

CREATE TABLE IF NOT EXISTS QUESTIONS(
  id  int PRIMARY KEY auto_increment,
  subject_id INT NOT NULL,
  title_id TEXT NOT NULL,
  question TEXT NOT NULL,
  answers TEXT NOT NULL,
  points INT NOT NULL
);



