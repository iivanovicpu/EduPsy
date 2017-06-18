
CREATE TABLE IF NOT EXISTS STUDENT_SCORE (
  id                 INT PRIMARY KEY NOT NULL DEFAULT nextval('all_id_seq'),
  subjectId          INT NOT NULL REFERENCES subjects(id),
  studentId          INT NOT NULL REFERENCES users(id),
  result             DECIMAL NOT NULL,
  count              INT NOT NULL,
  success            BOOLEAN DEFAULT FALSE
);

ALTER TABLE users ADD COLUMN intelligence_verbal_points INT;
ALTER TABLE users ADD COLUMN intelligence_not_verbal_points INT;
ALTER TABLE users ADD COLUMN intelligence_math_logic_points INT;
ALTER TABLE users DROP COLUMN intelligencetypeid;