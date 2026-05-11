-- Create table Doctor with columns: id, name, salary, and address
CREATE TABLE Doctor (
                        id NUMBER PRIMARY KEY,
                        name VARCHAR2(10) DEFAULT 'NO NAME',
                        salary NUMBER DEFAULT 5000,
                        address VARCHAR2(40)
);

-- Insert 10 rows into Doctor table with sample data
INSERT INTO Doctor (id, name, salary, address)
VALUES (1, 'Ahmed', 6500, 'Cairo');

INSERT INTO Doctor (id, name, salary, address)
VALUES (2, 'Mona', 7200, 'Giza');

INSERT INTO Doctor (id, name, salary, address)
VALUES (3, 'Omar', 5800, 'Alexandria');

INSERT INTO Doctor (id, name, salary, address)
VALUES (4, 'Sara', 8000, 'Mansoura');

INSERT INTO Doctor (id, name, salary, address)
VALUES (5, 'Youssef', 9000, 'Tanta');

INSERT INTO Doctor (id, name, salary, address)
VALUES (6, 'Nour', 5500, 'Aswan');

INSERT INTO Doctor (id, name, salary, address)
VALUES (7, 'Hany', 6100, 'Luxor');

INSERT INTO Doctor (id, name, salary, address)
VALUES (8, 'Dina', 7500, 'Suez');

INSERT INTO Doctor (id, name, salary, address)
VALUES (9, 'Karim', 6800, 'Zagazig');

INSERT INTO Doctor (id, name, salary, address)
VALUES (10, 'Laila', 8300, 'Ismailia');

-- Save all inserted rows permanently in the database
COMMIT;

-- Update record number 3 and set salary to 20000
UPDATE Doctor
SET salary = 20000
WHERE id = 3;

-- Delete record number 9 from Doctor table
DELETE FROM Doctor
WHERE id = 9;

-- Concatenate all columns into one text column for each row
SELECT
    'ID: ' || id ||
    ', Name: ' || name ||
    ', Salary: ' || salary ||
    ', Address: ' || address AS "All data"
FROM Doctor;

-- Display all records with salary multiplied by 2
SELECT
    id,
    name,
    salary,
    salary * 2 AS salary_double,
    address
FROM Doctor;

-- Select all records where salary is 1000, 2000, or 3000 using OR
SELECT *
FROM Doctor
WHERE salary = 1000 OR salary = 2000 OR salary = 3000;

-- Select all records where salary is 1000, 2000, or 3000 using IN
SELECT *
FROM Doctor
WHERE salary IN (1000, 2000, 3000);

-- Rename table Doctor to PRD_DOCTOR
ALTER TABLE Doctor RENAME TO PRD_DOCTOR;
