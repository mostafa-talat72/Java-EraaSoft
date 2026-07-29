
DROP TABLE Job_History;
DROP TABLE Jobs;
DROP TABLE Departments;

-------------------------------------------------
-- Create DEPARTMENTS table
-------------------------------------------------

CREATE TABLE Departments (
                             department_id NUMBER,
                             department_name VARCHAR2(100) NOT NULL,

                             CONSTRAINT PK_DEPARTMENTS PRIMARY KEY (department_id)
);

-------------------------------------------------
-- Create JOBS table
-------------------------------------------------

CREATE TABLE Jobs (
                      job_id VARCHAR2(20),
                      job_title VARCHAR2(100) NOT NULL,

                      CONSTRAINT PK_JOBS PRIMARY KEY (job_id)
);

-------------------------------------------------
-- Create JOB_HISTORY table
-------------------------------------------------

CREATE TABLE Job_History (
                             employee_id NUMBER,
                             start_date DATE,
                             end_date DATE,
                             job_id VARCHAR2(20),
                             department_id NUMBER,

                             CONSTRAINT PK_JOB_HISTORY
                                 PRIMARY KEY (employee_id, start_date),

                             CONSTRAINT FK_JOB_HISTORY_JOB
                                 FOREIGN KEY (job_id)
                                     REFERENCES Jobs(job_id),

                             CONSTRAINT FK_JOB_HISTORY_DEPARTMENT
                                 FOREIGN KEY (department_id)
                                     REFERENCES Departments(department_id)
);

-------------------------------------------------
-- Insert sample departments
-------------------------------------------------

INSERT INTO Departments VALUES (10, 'Administration');
INSERT INTO Departments VALUES (20, 'Marketing');
INSERT INTO Departments VALUES (30, 'Purchasing');
INSERT INTO Departments VALUES (40, 'Human Resources');
INSERT INTO Departments VALUES (50, 'Shipping');

-------------------------------------------------
-- Insert sample jobs
-------------------------------------------------

INSERT INTO Jobs VALUES ('AD_ASST', 'Administration Assistant');
INSERT INTO Jobs VALUES ('MK_REP', 'Marketing Representative');
INSERT INTO Jobs VALUES ('PU_CLERK', 'Purchasing Clerk');
INSERT INTO Jobs VALUES ('HR_REP', 'HR Representative');
INSERT INTO Jobs VALUES ('ST_CLERK', 'Stock Clerk');

-------------------------------------------------
-- Insert sample job history
-------------------------------------------------

INSERT INTO Job_History
VALUES (100, DATE '2020-01-10', DATE '2021-12-31', 'AD_ASST', 10);

INSERT INTO Job_History
VALUES (101, DATE '2019-03-15', DATE '2022-05-31', 'MK_REP', 20);

INSERT INTO Job_History
VALUES (102, DATE '2021-02-01', DATE '2023-01-31', 'PU_CLERK', 30);

INSERT INTO Job_History
VALUES (103, DATE '2018-07-20', DATE '2021-06-30', 'HR_REP', 40);

INSERT INTO Job_History
VALUES (104, DATE '2022-01-01', DATE '2024-12-31', 'ST_CLERK', 50);

INSERT INTO Job_History
VALUES (105, DATE '2023-04-01', DATE '2025-12-31', 'MK_REP', 20);

INSERT INTO Job_History
VALUES (106, DATE '2021-09-15', DATE '2024-03-31', 'ST_CLERK', 50);

INSERT INTO Job_History
VALUES (107, DATE '2019-11-01', DATE '2022-10-31', 'AD_ASST', 10);

INSERT INTO Job_History
VALUES (108, DATE '2020-06-15', DATE '2023-06-14', 'HR_REP', 40);

INSERT INTO Job_History
VALUES (109, DATE '2022-08-01', DATE '2025-07-31', 'PU_CLERK', 30);

-- --------------------------------------------------------------------------
-- please use sub query
-- to get JOB_HISTORY that job id on this values

SELECT *
FROM Job_History
WHERE job_id IN (
    SELECT job_id
    FROM Jobs
    WHERE job_id IN (
                     'AD_ASST',
                     'FI_MGR',
                     'FI_ACCOUNT',
                     'AC_MGR',
                     'AC_ACCOUNT',
                     'SA_MAN',
                     'SA_REP',
                     'PU_MAN'
        ) AND department_id IN (
        SELECT department_id
        FROM Departments
        WHERE department_name IN(
                                 'Administration',
                                 'Marketing',
                                 'Purchasing',
                                 'Human Resources',
                                 'Shipping'
            )
    )
);
