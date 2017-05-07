CREATE TABLE IF NOT EXISTS EXTERNAL_LINKS (
  id    INT PRIMARY KEY NOT NULL DEFAULT nextval('all_id_seq'),
  url   TEXT NOT NULL,
  title TEXT NOT NULL,
  subject_id INT NULL REFERENCES subjects(id)
);

ALTER TABLE subjects ADD COLUMN subject_id INT NULL;
ALTER TABLE subjects ADD COLUMN parent_subject_id INT NULL;
ALTER TABLE subjects DROP COLUMN parent_subject_id;

ALTER TABLE subjects ADD COLUMN subject_position_id INT NOT NULL DEFAULT 1;
ALTER TABLE subjects ADD COLUMN subject_level_id INT NULL;
ALTER TABLE subjects ADD COLUMN ordinal_number INT NULL;
ALTER TABLE subjects ADD COLUMN content TEXT NULL;
ALTER TABLE subjects ADD COLUMN additional_content TEXT NULL;

UPDATE subjects SET subject_level_id = 1; -- osnovno (SubjectLevel enum)

ALTER TABLE subjects ADD CONSTRAINT SUBJECTS_SUBJECT_FK FOREIGN KEY (subject_id) REFERENCES subjects ON DELETE RESTRICT;

CREATE TABLE IF NOT EXISTS INTELLIGENCE_TYPES (
  id    INT PRIMARY KEY NOT NULL DEFAULT nextval('all_id_seq'),
  code  TEXT NOT NULL CONSTRAINT INTELLIGENCE_TYPES_CODE_UQ UNIQUE,
  name  TEXT NOT NULL CONSTRAINT INTELLIGENCE_TYPES_NAME_UQ UNIQUE
);

ALTER TABLE learning_log DROP COLUMN titleid;

ALTER TABLE questions ADD COLUMN correctAnswers TEXT NULL;
ALTER TABLE questions RENAME COLUMN answers to possibleAnswers;
ALTER TABLE questions ALTER COLUMN possibleanswers DROP NOT NULL;
ALTER TABLE questions ADD COLUMN questiontypeid INT NOT NULL DEFAULT 1;
DELETE FROM questions;



