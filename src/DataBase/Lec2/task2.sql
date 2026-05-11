-- Create Employees table with columns: EmployeeID, FirstName, LastName, Department, and Salary
CREATE TABLE Employees (
                           EmployeeID NUMBER PRIMARY KEY,
                           FirstName VARCHAR2(30) NOT NULL,
                           LastName VARCHAR2(30) NOT NULL,
                           Department VARCHAR2(40),
                           Salary NUMBER DEFAULT 0
);

-- Insert 5 records into the Employees table
INSERT INTO Employees (EmployeeID, FirstName, LastName, Department, Salary)
VALUES (101, 'John1', 'Doe1', 'HR', 20000);

INSERT INTO Employees (EmployeeID, FirstName, LastName, Department, Salary)
VALUES (102, 'John2', 'Doe2', 'IT', 50000);

INSERT INTO Employees (EmployeeID, FirstName, LastName, Department, Salary)
VALUES (103, 'John3', 'Doe3', 'CS', 40000);

INSERT INTO Employees (EmployeeID, FirstName, LastName, Department, Salary)
VALUES (104, 'John4', 'Doe4', 'IT', 10000);

INSERT INTO Employees (EmployeeID, FirstName, LastName, Department, Salary)
VALUES (105, 'John5', 'Doe5', 'ZX', 30000);

-- Save the inserted records permanently
COMMIT;

-- Update the salary of the employee with EmployeeID 101 to 600000
UPDATE Employees
SET Salary = 600000
WHERE EmployeeID = 101;

-- Delete the employee record with Department 101
-- Wrong statement:
-- DELETE FROM Employees
-- WHERE Department = 101;

-- Error that may appear:
-- ORA-01722: invalid number

-- Explanation:
-- Department column is VARCHAR2, so it stores text values like 'HR', 'IT', 'CS', and 'ZX'.
-- 101 is a number, not text.
-- Oracle tries to convert Department values like 'HR' or 'IT' to numbers.
-- Because these text values cannot be converted to numbers, Oracle raises ORA-01722.

-- Correct statement if you want to delete by department:
-- DELETE FROM Employees
-- WHERE Department = 'IT';

-- Correct statement if you want to delete employee number 101:
-- DELETE FROM Employees
-- WHERE EmployeeID = 101;
DELETE FROM Employees
WHERE Department = 101;



-- Retrieve all employees in the IT department
SELECT *
FROM Employees
WHERE Department = 'IT';

-- Display all employee data with FirstName and LastName concatenated into one column
SELECT
    EmployeeID,
    FirstName || ' ' || LastName AS FullName,
    Department,
    Salary
FROM Employees;
