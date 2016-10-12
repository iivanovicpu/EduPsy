
CREATE TABLE IF NOT EXISTS ADAPTIVE_RULES (
  id                 INT PRIMARY KEY NOT NULL DEFAULT nextval('all_id_seq'),
  learningStyleId    INT NOT NULL,
  intelligenceTypeId INT NOT NULL,
  ruleId             INT NOT NULL,
  ruleData           TEXT,
  mark               TEXT
);

ALTER TABLE USERS ADD COLUMN intelligenceTypeId INT NOT NULL DEFAULT 1;
ALTER TABLE USERS ADD COLUMN learningStyleId INT NOT NULL DEFAULT 0;

