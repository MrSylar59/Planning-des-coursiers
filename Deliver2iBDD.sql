CREATE TABLE INSTANCE (ID BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL, DATE TIMESTAMP, DUR_MAX INTEGER, DUR_MIN INTEGER, NOM VARCHAR(255), PRIMARY KEY (ID))
CREATE TABLE SHIFT (ID BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL, DUREE INTEGER, PRIX FLOAT, TEMPS_MORT INTEGER, SOL_ID BIGINT, PRIMARY KEY (ID))
CREATE TABLE TOURNEE (ID BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL, DATE_DEBUT TIMESTAMP, DATE_FIN TIMESTAMP, INST_ID BIGINT, PRIMARY KEY (ID))
CREATE TABLE SOLUTION (ID BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL, ALGO VARCHAR(255), PRIX FLOAT, INST_ID BIGINT, PRIMARY KEY (ID))
CREATE TABLE TOURNEE_SHIFT (SHIFT_ID BIGINT NOT NULL, TOURNEE_ID BIGINT NOT NULL, PRIMARY KEY (SHIFT_ID, TOURNEE_ID))
ALTER TABLE SHIFT ADD CONSTRAINT FK_SHIFT_SOL_ID FOREIGN KEY (SOL_ID) REFERENCES SOLUTION (ID)
ALTER TABLE TOURNEE ADD CONSTRAINT FK_TOURNEE_INST_ID FOREIGN KEY (INST_ID) REFERENCES INSTANCE (ID)
ALTER TABLE SOLUTION ADD CONSTRAINT SOLUTION_INST_ID FOREIGN KEY (INST_ID) REFERENCES INSTANCE (ID)
ALTER TABLE TOURNEE_SHIFT ADD CONSTRAINT TURNEESHIFTSHIFTID FOREIGN KEY (SHIFT_ID) REFERENCES SHIFT (ID)
ALTER TABLE TOURNEE_SHIFT ADD CONSTRAINT TRNEESHIFTTURNEEID FOREIGN KEY (TOURNEE_ID) REFERENCES TOURNEE (ID)