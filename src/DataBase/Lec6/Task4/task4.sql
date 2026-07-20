
-- Rename a CHECK constraint named chk_age to check_min_age on the Students table.

ALTER TABLE Students RENAME CONSTRAINT chk_age TO check_min_age;

-- --------------------------------------------------------------------------------
-- Rename a FOREIGN KEY constraint fk_emp_dept to fk_employee_department on the Employees table.

ALTER TABLE Employees RENAME CONSTRAINT fk_emp_dept TO fk_employee_department;

-- --------------------------------------------------------------------------------
-- Rename the PRIMARY KEY constraint on the Users table to pk_users_id.

ALTER TABLE Users RENAME CONSTRAINT PK_USERS TO pk_users_id;

-- --------------------------------------------------------------------------------
-- Write SQL to rename the UNIQUE constraint on the username column to uk_user_name.

ALTER TABLE Users RENAME CONSTRAINT SYS_C008743 TO uk_user_name;

-- --------------------------------------------------------------------------------
-- Provide the syntax to rename a constraint in SQL Server vs PostgreSQL.

-- SQL
EXEC sp_rename
	    'table_name.old_constraint_name',
	    'new_constraint_name',
	    'OBJECT';
	-- Example
EXEC sp_rename
	    'Employees.CHK_AGE',
	    'CHK_EMPLOYEE_AGE',
	    'OBJECT';
	------------------------------
-- PostgreSQL
ALTER TABLE table_name
    RENAME CONSTRAINT old_constraint_name
    TO new_constraint_name;
-- Example
ALTER TABLE Employees
    RENAME CONSTRAINT chk_age
    TO chk_employee_age;

-- ------------------------------------------------------------------------------------










