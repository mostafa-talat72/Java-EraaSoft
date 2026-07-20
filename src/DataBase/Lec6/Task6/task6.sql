
-- Enable the FOREIGN KEY constraint fk_customer_order on the Orders table.

ALTER TABLE Orders ENABLE CONSTRAINT fk_customer_order;

-- ------------------------------------------------------------------------------------------
-- Re-enable all constraints on the Products table after a data load.

-- Show all constraints on the Products table.
SELECT constraint_name, constraint_type
FROM all_constraints
WHERE owner = 'ITEM'
  AND table_name = 'PRODUCTS';

-- Generate SQL statements to ENABLE all constraints.
SELECT
    'ALTER TABLE ITEM.PRODUCTS ENABLE CONSTRAINT '
        || constraint_name || ';' AS enable_sql
FROM all_constraints
WHERE owner = 'ITEM'
  AND table_name = 'PRODUCTS';

-- Execute the generated ALTER TABLE statements
ALTER TABLE ITEM.PRODUCTS ENABLE CONSTRAINT CH_PRICE;
ALTER TABLE ITEM.PRODUCTS ENABLE CONSTRAINT NN_PRODUCT_NAME;
ALTER TABLE ITEM.PRODUCTS ENABLE CONSTRAINT PK_PRODUCTS;
ALTER TABLE ITEM.PRODUCTS ENABLE CONSTRAINT UQ_PRODUCTS_NAME;

-- Note:
-- Another approach is to use a PL/SQL block with EXECUTE IMMEDIATE.
-- This method generates and executes the ALTER TABLE statements automatically,
-- so there is no need to copy and run the generated SQL manually.

BEGIN
FOR c IN (
        SELECT constraint_name
        FROM all_constraints
        WHERE owner = 'ITEM'
          AND table_name = 'PRODUCTS'
          AND constraint_name NOT LIKE 'SYS_%'
    ) LOOP
        EXECUTE IMMEDIATE
            'ALTER TABLE ITEM.PRODUCTS ENABLE CONSTRAINT ' || c.constraint_name;
END LOOP;
END;


-- ------------------------------------------------------------------------------------------
-- Write SQL to enable a CHECK constraint on salary in the Staff table.

ALTER TABLE Staff ENABLE CONSTRAINT CH_SALARY;

-- ------------------------------------------------------------------------------------------
-- Enable the PRIMARY KEY constraint on Departments(dept_id) after it was disabled.

ALTER TABLE Departments ENABLE CONSTRAINT PK_DEPARTMENTS;

-- ------------------------------------------------------------------------------------------
-- How do you enable a constraint only if it's currently disabled?

-- Method 1:
-- Generate ENABLE statements only for constraints that are currently disabled.
-- Execute the generated SQL statements manually to enable the constraints.

SELECT
    'ALTER TABLE ITEM.PRODUCTS ENABLE CONSTRAINT '
        || constraint_name || ';' AS enable_sql
FROM all_constraints
WHERE owner = 'ITEM'
  AND table_name = 'PRODUCTS'
  AND status = 'DISABLED';

-- Execute the generated SQL statements to enable the disabled constraints.

ALTER TABLE ITEM.PRODUCTS ENABLE CONSTRAINT CH_PRICE;
ALTER TABLE ITEM.PRODUCTS ENABLE CONSTRAINT NN_PRODUCT_NAME;
ALTER TABLE ITEM.PRODUCTS ENABLE CONSTRAINT PK_PRODUCTS;
ALTER TABLE ITEM.PRODUCTS ENABLE CONSTRAINT UQ_PRODUCTS_NAME;

-- Method 2:
-- Automatically enable all currently disabled constraints using PL/SQL.
-- This approach generates and executes the ALTER TABLE statements automatically.

BEGIN
FOR c IN (
        SELECT constraint_name
        FROM all_constraints
        WHERE owner = 'ITEM'
          AND table_name = 'PRODUCTS'
          AND status = 'DISABLED'
    ) LOOP
        EXECUTE IMMEDIATE
            'ALTER TABLE ITEM.PRODUCTS ENABLE CONSTRAINT ' || c.constraint_name;
END LOOP;
END;



