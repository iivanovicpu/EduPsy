CREATE TABLE IF NOT EXISTS EXTERNAL_LINKS (
  id    INT PRIMARY KEY NOT NULL DEFAULT nextval('all_id_seq'),
  url   TEXT NOT NULL,
  title TEXT NOT NULL,
  subject_id INT NULL REFERENCES subjects(id)
);

ALTER TABLE subjects ADD COLUMN subject_id INT NULL;
ALTER TABLE subjects ADD COLUMN subject_level_id INT NULL;
ALTER TABLE subjects ADD COLUMN ordinal_number INT NULL;
ALTER TABLE subjects ADD COLUMN content TEXT NULL;

UPDATE subjects SET subject_level_id = 1; -- osnovno (SubjectLevel enum)

ALTER TABLE subjects ADD CONSTRAINT SUBJECTS_SUBJECT_FK FOREIGN KEY (subject_id) REFERENCES subjects ON DELETE RESTRICT;

CREATE TABLE IF NOT EXISTS INTELLIGENCE_TYPES (
  id    INT PRIMARY KEY NOT NULL DEFAULT nextval('all_id_seq'),
  code  TEXT NOT NULL CONSTRAINT INTELLIGENCE_TYPES_CODE_UQ UNIQUE,
  name  TEXT NOT NULL CONSTRAINT INTELLIGENCE_TYPES_NAME_UQ UNIQUE
);

INSERT INTO INTELLIGENCE_TYPES (code, name) VALUES ('O', 'Opći tip inteligencije');
INSERT INTO INTELLIGENCE_TYPES (code, name) VALUES ('ML', 'Matematičko-logička inteligencije');
INSERT INTO INTELLIGENCE_TYPES (code, name) VALUES ('NV', 'Non Verbal - Prostorna inteligencija');
INSERT INTO INTELLIGENCE_TYPES (code, name) VALUES ('V', 'Verbal - Lingvistička inteligencija');

CREATE TABLE IF NOT EXISTS LEARNING_STYLES (
  id    INT PRIMARY KEY NOT NULL DEFAULT nextval('all_id_seq'),
  code  TEXT NOT NULL CONSTRAINT LEARNING_STYLES_CODE_UQ UNIQUE,
  name  TEXT NOT NULL CONSTRAINT LEARNING_STYLES_NAME_UQ UNIQUE
);

INSERT INTO LEARNING_STYLES (code, name) VALUES ('O','Opći stil učenja');
INSERT INTO LEARNING_STYLES (code, name) VALUES ('AKT','Aktivni');
INSERT INTO LEARNING_STYLES (code, name) VALUES ('REF','Reflektivni');
INSERT INTO LEARNING_STYLES (code, name) VALUES ('NAR','Neutralno aktivno-reflektivni');
INSERT INTO LEARNING_STYLES (code, name) VALUES ('VIZ','Vizualni');
INSERT INTO LEARNING_STYLES (code, name) VALUES ('VER','Verbalni');
INSERT INTO LEARNING_STYLES (code, name) VALUES ('NVV','Neutralno vizualno-verbalni');
INSERT INTO LEARNING_STYLES (code, name) VALUES ('SEK','Sekvencijalni');
INSERT INTO LEARNING_STYLES (code, name) VALUES ('GLO','Globalni');
INSERT INTO LEARNING_STYLES (code, name) VALUES ('NSG','Neutralno sekvencijalno-globalni');
INSERT INTO LEARNING_STYLES (code, name) VALUES ('INT','Intuitivni');
INSERT INTO LEARNING_STYLES (code, name) VALUES ('SEN','Senzorni');
INSERT INTO LEARNING_STYLES (code, name) VALUES ('NIS','Neutralno intuitivno-senzorni');

CREATE TABLE IF NOT EXISTS INTELLIGENCE_ADAPTIVE_RULES (
  id    INT PRIMARY KEY NOT NULL DEFAULT nextval('all_id_seq'),
  adaptive_rule_id      INT NOT NULL,
  intelligence_type_id  INT NOT NULL REFERENCES INTELLIGENCE_TYPES(id),
  CONSTRAINT INTELLIGENCE_ADAPTIVE_RULES_UQ UNIQUE (adaptive_rule_id, intelligence_type_id)
);

INSERT INTO INTELLIGENCE_ADAPTIVE_RULES (adaptive_rule_id, intelligence_type_id) VALUES (1, (SELECT id FROM INTELLIGENCE_TYPES WHERE code = 'ML'));
INSERT INTO INTELLIGENCE_ADAPTIVE_RULES (adaptive_rule_id, intelligence_type_id) VALUES (2, (SELECT id FROM INTELLIGENCE_TYPES WHERE code = 'ML'));
INSERT INTO INTELLIGENCE_ADAPTIVE_RULES (adaptive_rule_id, intelligence_type_id) VALUES (3, (SELECT id FROM INTELLIGENCE_TYPES WHERE code = 'ML'));
INSERT INTO INTELLIGENCE_ADAPTIVE_RULES (adaptive_rule_id, intelligence_type_id) VALUES (5, (SELECT id FROM INTELLIGENCE_TYPES WHERE code = 'ML'));
INSERT INTO INTELLIGENCE_ADAPTIVE_RULES (adaptive_rule_id, intelligence_type_id) VALUES (6, (SELECT id FROM INTELLIGENCE_TYPES WHERE code = 'ML'));
INSERT INTO INTELLIGENCE_ADAPTIVE_RULES (adaptive_rule_id, intelligence_type_id) VALUES (12, (SELECT id FROM INTELLIGENCE_TYPES WHERE code = 'ML'));
INSERT INTO INTELLIGENCE_ADAPTIVE_RULES (adaptive_rule_id, intelligence_type_id) VALUES (2, (SELECT id FROM INTELLIGENCE_TYPES WHERE code = 'NV'));
INSERT INTO INTELLIGENCE_ADAPTIVE_RULES (adaptive_rule_id, intelligence_type_id) VALUES (6, (SELECT id FROM INTELLIGENCE_TYPES WHERE code = 'NV'));
INSERT INTO INTELLIGENCE_ADAPTIVE_RULES (adaptive_rule_id, intelligence_type_id) VALUES (7, (SELECT id FROM INTELLIGENCE_TYPES WHERE code = 'NV'));
INSERT INTO INTELLIGENCE_ADAPTIVE_RULES (adaptive_rule_id, intelligence_type_id) VALUES (2, (SELECT id FROM INTELLIGENCE_TYPES WHERE code = 'V'));
INSERT INTO INTELLIGENCE_ADAPTIVE_RULES (adaptive_rule_id, intelligence_type_id) VALUES (6, (SELECT id FROM INTELLIGENCE_TYPES WHERE code = 'V'));

CREATE TABLE IF NOT EXISTS LEARNING_STYLES_ADAPTIVE_RULES (
  id    INT PRIMARY KEY NOT NULL DEFAULT nextval('all_id_seq'),
  adaptive_rule_id   INT NOT NULL,
  learning_style_id  INT NOT NULL REFERENCES LEARNING_STYLES(id),
  CONSTRAINT LEARNING_STYLE_ADAPTIVE_RULES_UQ UNIQUE (adaptive_rule_id, learning_style_id)
);

INSERT INTO LEARNING_STYLES_ADAPTIVE_RULES (adaptive_rule_id, learning_style_id) VALUES (2, (SELECT id FROM LEARNING_STYLES WHERE code = 'AKT'));
INSERT INTO LEARNING_STYLES_ADAPTIVE_RULES (adaptive_rule_id, learning_style_id) VALUES (9, (SELECT id FROM LEARNING_STYLES WHERE code = 'AKT'));
INSERT INTO LEARNING_STYLES_ADAPTIVE_RULES (adaptive_rule_id, learning_style_id) VALUES (10, (SELECT id FROM LEARNING_STYLES WHERE code = 'REF'));
INSERT INTO LEARNING_STYLES_ADAPTIVE_RULES (adaptive_rule_id, learning_style_id) VALUES (5, (SELECT id FROM LEARNING_STYLES WHERE code = 'REF'));
INSERT INTO LEARNING_STYLES_ADAPTIVE_RULES (adaptive_rule_id, learning_style_id) VALUES (2, (SELECT id FROM LEARNING_STYLES WHERE code = 'VIZ'));
INSERT INTO LEARNING_STYLES_ADAPTIVE_RULES (adaptive_rule_id, learning_style_id) VALUES (6, (SELECT id FROM LEARNING_STYLES WHERE code = 'VIZ'));
INSERT INTO LEARNING_STYLES_ADAPTIVE_RULES (adaptive_rule_id, learning_style_id) VALUES (7, (SELECT id FROM LEARNING_STYLES WHERE code = 'VIZ'));
INSERT INTO LEARNING_STYLES_ADAPTIVE_RULES (adaptive_rule_id, learning_style_id) VALUES (2, (SELECT id FROM LEARNING_STYLES WHERE code = 'VER'));
INSERT INTO LEARNING_STYLES_ADAPTIVE_RULES (adaptive_rule_id, learning_style_id) VALUES (6, (SELECT id FROM LEARNING_STYLES WHERE code = 'VER'));
INSERT INTO LEARNING_STYLES_ADAPTIVE_RULES (adaptive_rule_id, learning_style_id) VALUES (5, (SELECT id FROM LEARNING_STYLES WHERE code = 'SEK'));
INSERT INTO LEARNING_STYLES_ADAPTIVE_RULES (adaptive_rule_id, learning_style_id) VALUES (3, (SELECT id FROM LEARNING_STYLES WHERE code = 'SEK'));
INSERT INTO LEARNING_STYLES_ADAPTIVE_RULES (adaptive_rule_id, learning_style_id) VALUES (7, (SELECT id FROM LEARNING_STYLES WHERE code = 'GLO'));
INSERT INTO LEARNING_STYLES_ADAPTIVE_RULES (adaptive_rule_id, learning_style_id) VALUES (8, (SELECT id FROM LEARNING_STYLES WHERE code = 'GLO'));
INSERT INTO LEARNING_STYLES_ADAPTIVE_RULES (adaptive_rule_id, learning_style_id) VALUES (1, (SELECT id FROM LEARNING_STYLES WHERE code = 'INT'));
INSERT INTO LEARNING_STYLES_ADAPTIVE_RULES (adaptive_rule_id, learning_style_id) VALUES (9, (SELECT id FROM LEARNING_STYLES WHERE code = 'INT'));
INSERT INTO LEARNING_STYLES_ADAPTIVE_RULES (adaptive_rule_id, learning_style_id) VALUES (5, (SELECT id FROM LEARNING_STYLES WHERE code = 'SEN'));
INSERT INTO LEARNING_STYLES_ADAPTIVE_RULES (adaptive_rule_id, learning_style_id) VALUES (11, (SELECT id FROM LEARNING_STYLES WHERE code = 'SEN'));





