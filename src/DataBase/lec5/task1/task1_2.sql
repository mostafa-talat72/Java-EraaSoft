CREATE TABLE manger_salary (
                               id NUMBER NOT NULL,
                               name VARCHAR2(100),
                               salary NUMBER(10, 2),
                               CONSTRAINT manger_salary_id_name_uk UNIQUE (id, name)
);