CREATE TABLE language (
                          id NUMBER PRIMARY KEY,
                          name VARCHAR2(100) NOT NULL
);

CREATE TABLE teacher (
                         id NUMBER PRIMARY KEY,
                         name VARCHAR2(100) NOT NULL,
                         salary NUMBER(10, 2),
                         language_id NUMBER NOT NULL,

                         CONSTRAINT teacher_language_fk
                             FOREIGN KEY (language_id)
                                 REFERENCES language(id)
);