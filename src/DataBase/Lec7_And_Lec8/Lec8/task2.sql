
DROP TABLE Job_History;
DROP TABLE Jobs;

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
                             start_date TIMESTAMP,
                             end_date TIMESTAMP,
                             job_id VARCHAR2(20),
                             department_id NUMBER,

                             CONSTRAINT PK_JOB_HISTORY
                                 PRIMARY KEY (employee_id, start_date),

                             CONSTRAINT FK_JOB_HISTORY_JOB
                                 FOREIGN KEY (job_id)
                                     REFERENCES Jobs(job_id)
);



INSERT INTO Jobs VALUES ('IT_PROG', 'Programmer');
INSERT INTO Jobs VALUES ('AC_ACCOUNT', 'Accountant');
INSERT INTO Jobs VALUES ('AC_MGR', 'Accounting Manager');
INSERT INTO Jobs VALUES ('MK_REP', 'Marketing Representative');
INSERT INTO Jobs VALUES ('ST_CLERK', 'Stock Clerk');
INSERT INTO Jobs VALUES ('AD_ASST', 'Administration Assistant');
INSERT INTO Jobs VALUES ('SA_REP', 'Sales Representative');
INSERT INTO Jobs VALUES ('SA_MAN', 'Sales Manager');
INSERT INTO Jobs VALUES ('AD_VP', 'Administration Vice President');
INSERT INTO Jobs VALUES ('AD_PRES', 'President');


INSERT INTO Job_History VALUES
    (102, TIMESTAMP '2001-01-13 00:00:00', TIMESTAMP '2006-07-24 00:00:00', 'IT_PROG', 60);

INSERT INTO Job_History VALUES
    (101, TIMESTAMP '1997-09-21 00:00:00', TIMESTAMP '2001-10-27 00:00:00', 'AC_ACCOUNT', 110);

INSERT INTO Job_History VALUES
    (101, TIMESTAMP '2001-10-28 00:00:00', TIMESTAMP '2005-03-15 00:00:00', 'AC_MGR', 110);

INSERT INTO Job_History VALUES
    (201, TIMESTAMP '2004-02-17 00:00:00', TIMESTAMP '2007-12-19 00:00:00', 'MK_REP', 20);

INSERT INTO Job_History VALUES
    (114, TIMESTAMP '2006-03-24 00:00:00', TIMESTAMP '2007-12-31 00:00:00', 'ST_CLERK', 50);

INSERT INTO Job_History VALUES
    (122, TIMESTAMP '2007-01-01 00:00:00', TIMESTAMP '2007-12-31 00:00:00', 'ST_CLERK', 50);

INSERT INTO Job_History VALUES
    (200, TIMESTAMP '1995-09-17 00:00:00', TIMESTAMP '2001-06-17 00:00:00', 'AD_ASST', 90);

INSERT INTO Job_History VALUES
    (176, TIMESTAMP '2006-03-24 00:00:00', TIMESTAMP '2006-12-31 00:00:00', 'SA_REP', 80);

INSERT INTO Job_History VALUES
    (176, TIMESTAMP '2007-01-01 00:00:00', TIMESTAMP '2007-12-31 00:00:00', 'SA_MAN', 80);

INSERT INTO Job_History VALUES
    (200, TIMESTAMP '2002-07-01 00:00:00', TIMESTAMP '2006-12-31 00:00:00', 'AC_ACCOUNT', 90);

INSERT INTO Job_History VALUES
    (101, TIMESTAMP '2005-09-21 02:00:00', TIMESTAMP '2025-08-14 20:50:53', 'AD_VP', 90);

INSERT INTO Job_History VALUES
    (100, TIMESTAMP '2003-06-17 01:25:40', TIMESTAMP '2025-01-28 20:20:03', 'AD_PRES', 90);


-- natural join

SELECT *
FROM Jobs
         NATURAL JOIN Job_History;


-- ----------------------------------------------------------------
-- join with  using key word with column job_id


SELECT *
FROM Jobs
         JOIN Job_History USING (job_id);


-- ----------------------------------------------------------------
-- join with  on key word with column COUNTRY_ID

SELECT *
FROM  Jobs j
          JOIN Job_History jh ON j.job_id = jh.job_id;

-- ----------------------------------------------------------------
-- inner join with

SELECT *
FROM  Jobs j
          INNER JOIN Job_History jh ON j.job_id = jh.job_id;


-- ----------------------------------------------------------------
-- left join with

SELECT *
FROM Jobs j
         LEFT OUTER JOIN Job_History jh ON j.job_id = jh.job_id;

-- ----------------------------------------------------------------
-- right join with

SELECT *
FROM  Jobs j
          RIGHT OUTER JOIN Job_History jh ON j.job_id = jh.job_id;

-- ----------------------------------------------------------------
-- fulljoin with

SELECT *
FROM  Jobs j
          FULL OUTER JOIN Job_History jh ON j.job_id = jh.job_id;


