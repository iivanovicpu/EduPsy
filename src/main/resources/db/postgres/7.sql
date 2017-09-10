
CREATE TABLE IF NOT EXISTS MOTIVATIONAL_MESSAGES (
  id                 INT PRIMARY KEY NOT NULL DEFAULT nextval('all_id_seq'),
  message            VARCHAR(200)
);

INSERT INTO MOTIVATIONAL_MESSAGES (message) VALUES ('The definition of success may be relative, but its ingredients are the same – Hard Work and Sacrifice.');
INSERT INTO MOTIVATIONAL_MESSAGES (message) VALUES ('The relationship between success and procrastination is similar to that of love and cheating. You can never achieve success if you procrastinate and you can never be in love if you cheat.');
INSERT INTO MOTIVATIONAL_MESSAGES (message) VALUES ('On the day Nike said Just Do It, they revealed the secret to success.');
INSERT INTO MOTIVATIONAL_MESSAGES (message) VALUES ('Don’t worry about measuring your success. Worry about measuring your failure so you know how much more you need to try.');
INSERT INTO MOTIVATIONAL_MESSAGES (message) VALUES ('Never Fail is not the mantra to success. Never Give Up is.');
INSERT INTO MOTIVATIONAL_MESSAGES (message) VALUES ('Many people say that success is 99% hard work and 1 % luck. You may not be able to control that 1% but at least you can take care of the 99% and stop worrying about the rest.');
INSERT INTO MOTIVATIONAL_MESSAGES (message) VALUES ('If you believe that you need to be lucky to be successful, you must also remember that working hard is the easiest way to be lucky.');
INSERT INTO MOTIVATIONAL_MESSAGES (message) VALUES ('Instead of wasting time in worrying about how you will deal with failure, spend time thinking about what you can do to succeed.');