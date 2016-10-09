
CREATE SEQUENCE all_id_seq;

CREATE TABLE IF NOT EXISTS USERS (
  id        INT PRIMARY KEY NOT NULL DEFAULT nextval('all_id_seq'),
  username  TEXT NOT NULL,
  password  TEXT NOT NULL,
  firstName TEXT NOT NULL,
  lastname  TEXT,
  email     TEXT,
  status    TEXT CHECK (status IN ('ADMIN', 'TEACHER', 'STUDENT')),
  color     TEXT
);

CREATE TABLE IF NOT EXISTS SUBJECTS (
id        INT PRIMARY KEY NOT NULL DEFAULT nextval('all_id_seq'),
  title    TEXT NOT NULL,
  keywords TEXT,
  url      TEXT
);

CREATE TABLE IF NOT EXISTS QUESTIONS (
id        INT PRIMARY KEY NOT NULL DEFAULT nextval('all_id_seq'),
  subjectId INT  NOT NULL,
  titleId   TEXT NOT NULL,
  question  TEXT NOT NULL,
  answers   TEXT NOT NULL,
  points    INT  NOT NULL
);

CREATE TABLE IF NOT EXISTS LEARNING_LOG (
id        INT PRIMARY KEY NOT NULL DEFAULT nextval('all_id_seq'),
  subjectId INT NOT NULL,
  titleId   TEXT NOT NULL,
  studentId INT NOT NULL,
  date TIMESTAMP,
  statusId  INT NOT NULL
)



