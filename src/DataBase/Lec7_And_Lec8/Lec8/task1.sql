
DROP TABLE Locations;
DROP TABLE Countries;

-------------------------------------------------
-- Create COUNTRIES table
-------------------------------------------------

CREATE TABLE Countries (
                           country_id CHAR(2),
                           country_name VARCHAR2(100) NOT NULL,
                           region_id NUMBER NOT NULL,

                           CONSTRAINT PK_COUNTRIES PRIMARY KEY (country_id)
);

-------------------------------------------------
-- Create LOCATIONS table
-------------------------------------------------

CREATE TABLE Locations (
                           location_id NUMBER,
                           street_address VARCHAR2(200),
                           postal_code VARCHAR2(20),
                           city VARCHAR2(100) NOT NULL,
                           state_province VARCHAR2(100),
                           country_id CHAR(2),

                           CONSTRAINT PK_LOCATIONS PRIMARY KEY (location_id),

                           CONSTRAINT FK_LOCATION_COUNTRY
                               FOREIGN KEY (country_id)
                                   REFERENCES Countries(country_id)
);

INSERT INTO Countries (country_id, country_name, region_id)
VALUES ('US', 'United States of America', 2);

INSERT INTO Countries (country_id, country_name, region_id)
VALUES ('UK', 'United Kingdom', 1);

INSERT INTO Countries (country_id, country_name, region_id)
VALUES ('CA', 'Canada', 2);

INSERT INTO Countries (country_id, country_name, region_id)
VALUES ('JP', 'Japan', 3);

INSERT INTO Countries (country_id, country_name, region_id)
VALUES ('IT', 'Italy', 1);

INSERT INTO Countries (country_id, country_name, region_id)
VALUES ('EG', 'Egypt', 4);

INSERT INTO Countries (country_id, country_name, region_id)
VALUES ('DE', 'Germany', 1);

INSERT INTO Countries (country_id, country_name, region_id)
VALUES ('FR', 'France', 1);

INSERT INTO Countries
VALUES ('SA','Saudi Arabia',4);

INSERT INTO Locations (location_id, street_address, postal_code, city, state_province, country_id)
VALUES (1000, '1297 Via Cola di Rie', '00989', 'Roma', NULL, 'IT');

INSERT INTO Locations (location_id, street_address, postal_code, city, state_province, country_id)
VALUES (1100, '93091 Calle della Testa', '10934', 'Venice', NULL, 'IT');

INSERT INTO Locations (location_id, street_address, postal_code, city, state_province, country_id)
VALUES (1200, '2017 Shinjuku-ku', '1689', 'Tokyo', 'Tokyo Prefecture', 'JP');

INSERT INTO Locations (location_id, street_address, postal_code, city, state_province, country_id)
VALUES (1300, '2014 Jabberwocky Rd', '26192', 'Dallas', 'Texas', 'US');

INSERT INTO Locations (location_id, street_address, postal_code, city, state_province, country_id)
VALUES (1400, '2004 Charade Rd', '98199', 'Seattle', 'Washington', 'US');

INSERT INTO Locations (location_id, street_address, postal_code, city, state_province, country_id)
VALUES (1500, '147 Spadina Ave', 'M5V 2L7', 'Toronto', 'Ontario', 'CA');

INSERT INTO Locations (location_id, street_address, postal_code, city, state_province, country_id)
VALUES (1600, '8204 Arthur St', 'SW1A 1AA', 'London', NULL, 'UK');

INSERT INTO Locations (location_id, street_address, postal_code, city, state_province, country_id)
VALUES (1700, 'Marienplatz 1', '80331', 'Munich', 'Bavaria', 'DE');

INSERT INTO Locations (location_id, street_address, postal_code, city, state_province, country_id)
VALUES (1800, '10 Champs Elysees', '75008', 'Paris', NULL, 'FR');

INSERT INTO Locations (location_id, street_address, postal_code, city, state_province, country_id)
VALUES (1900, '15 Tahrir Square', '11511', 'Cairo', 'Cairo', 'EG');


-- natural join

SELECT *
FROM Countries
         NATURAL JOIN Locations;


-- ----------------------------------------------------------------
-- join with  using key word with column COUNTRY_ID


SELECT *
FROM Countries
         JOIN Locations USING (country_id);


-- ----------------------------------------------------------------
-- join with  on key word with column COUNTRY_ID

SELECT *
FROM Countries c
         JOIN Locations l ON c.country_id = l.country_id;

-- ----------------------------------------------------------------
-- inner join with

SELECT *
FROM Countries c
         INNER JOIN Locations l ON c.country_id = l.country_id;


-- ----------------------------------------------------------------
-- left join with

SELECT *
FROM Countries c
         LEFT OUTER JOIN Locations l ON c.country_id = l.country_id;

-- ----------------------------------------------------------------
-- right join with

SELECT *
FROM Countries c
         RIGHT OUTER JOIN Locations l ON c.country_id = l.country_id;

-- ----------------------------------------------------------------
-- fulljoin with

SELECT *
FROM Countries c
         FULL OUTER JOIN Locations l ON c.country_id = l.country_id;


