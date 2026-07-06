CREATE TABLE employee (
                          id NUMBER PRIMARY KEY,
                          name VARCHAR2(100) NOT NULL,
                          age NUMBER
);

CREATE TABLE phone (
                       id NUMBER PRIMARY KEY,
                       phone_number VARCHAR2(20) NOT NULL,
                       employee_id NUMBER NOT NULL UNIQUE,

                       CONSTRAINT phone_employee_fk
                           FOREIGN KEY (employee_id)
                               REFERENCES employee(id)
);