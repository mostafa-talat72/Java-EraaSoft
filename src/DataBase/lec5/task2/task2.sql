CREATE TABLE doctor (
                        id NUMBER PRIMARY KEY,
                        name VARCHAR2(100),
                        salary NUMBER(10, 2)
);

CREATE TABLE patient (
                         id NUMBER PRIMARY KEY,
                         name VARCHAR2(100),
                         age NUMBER
);

CREATE TABLE doctor_patient (
                                doctor_id NUMBER NOT NULL,
                                patient_id NUMBER NOT NULL,

                                CONSTRAINT doctor_patient_pk PRIMARY KEY (doctor_id, patient_id),

                                CONSTRAINT doctor_patient_doctor_fk
                                    FOREIGN KEY (doctor_id) REFERENCES doctor(id),

                                CONSTRAINT doctor_patient_patient_fk
                                    FOREIGN KEY (patient_id) REFERENCES patient(id)
);