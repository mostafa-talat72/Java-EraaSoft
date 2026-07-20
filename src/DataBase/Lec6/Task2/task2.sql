
-- Add a NOT NULL constraint to the email column in the Customers table.

ALTER TABLE Customers MODIFY email CONSTRAINT NN_EMAIL NOT NULL;

-- SQL Error [1400] [23000]: ORA-01400: cannot insert NULL into ("ITEM"."CUSTOMERS"."EMAIL")
INSERT INTO Customers (email) VALUES (NULL);

-- Updated Rows 1
INSERT INTO Customers (email) VALUES ('mostafa@gmail.com');

-- ---------------------------------------------------------------------------------------------------
-- Add a UNIQUE constraint to the username column in the Users table.

ALTER TABLE USERS ADD CONSTRAINT UQ_USERS_USERNAME UNIQUE (username);

-- Updated Rows 1
INSERT INTO Users (username) VALUES ('mostafa');

-- SQL Error [1] [23000]: ORA-00001: unique constraint (ITEM.UQ_USERS_USERNAME) violated
INSERT INTO Users (username) VALUES ('mostafa');

-- ---------------------------------------------------------------------------------------------------
-- Add a FOREIGN KEY constraint on Orders.customer_id referencing Customers(id).

ALTER TABLE Orders ADD CONSTRAINT FK_CUSTOMER_ID FOREIGN KEY (customer_id) REFERENCES Customers(id);

-- SQL Error [2291] [23000]: ORA-02291: integrity constraint (ITEM.FK_CUSTOMER_ID) violated - parent key not found
INSERT INTO Orders (customer_id) VALUES (1000);

-- Updated Rows 1
INSERT INTO Orders (customer_id) VALUES (1);

-- ---------------------------------------------------------------------------------------------------
-- Use ALTER TABLE to add a CHECK constraint to the Accounts table to ensure balance >= 0.

ALTER TABLE Accounts ADD CONSTRAINT CH_BALANCE CHECK (balance >= 0);

-- SQL Error [2290] [23000]: ORA-02290: check constraint (ITEM.CH_BALANCE) violated
INSERT INTO Accounts (balance) VALUES (-100);

-- Updated Rows 1
INSERT INTO Accounts (balance) VALUES (0);

-- ---------------------------------------------------------------------------------------------------
-- Add a PRIMARY KEY constraint to the Departments table on the dept_id column.

ALTER TABLE Departments ADD CONSTRAINT PK_DEPT_ID PRIMARY KEY (dept_id);

-- Updated Rows 1
INSERT INTO Departments (dept_id) VALUES (1);

-- SQL Error [1] [23000]: ORA-00001: unique constraint (ITEM.PK_DEPT_ID) violated
INSERT INTO Departments (dept_id) VALUES (1);

-- ------------------------------------------------------------------------------------------------------


