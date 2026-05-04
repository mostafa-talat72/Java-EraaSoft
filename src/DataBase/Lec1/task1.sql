-- 1) Create table Manger
CREATE TABLE Manger(
                       id NUMBER NOT NULL,
                       name VARCHAR2(25) DEFAULT 'NO NAME',
                       age NUMBER,
                       birth_date DATE DEFAULT SYSDATE,
                       address VARCHAR2(50)
);

-- 2) Drop the column 'address'
ALTER TABLE Manger DROP COLUMN address;

-- 3) Add new columns 'city_address' and 'street'
ALTER TABLE Manger ADD (
    city_address VARCHAR2(50),
    street VARCHAR2(25)
);

-- 4) Rename column 'name' to 'full_name'
ALTER TABLE Manger RENAME COLUMN name TO full_name;

-- 5) Set table Manger to READ ONLY
ALTER TABLE Manger READ ONLY;

-- 6) Create table Owner with the same structure as Manger
CREATE TABLE Owner AS SELECT * FROM Manger;

-- 7) Create table Owner with only specific columns
CREATE TABLE Owner AS SELECT id, full_name, birth_date FROM Manger;

-- 8) Rename table Manger to Master
ALTER TABLE Manger RENAME TO Master;

-- 9) Drop table Master
DROP TABLE Master;

-- 10) Drop table Owner
DROP TABLE Owner;
