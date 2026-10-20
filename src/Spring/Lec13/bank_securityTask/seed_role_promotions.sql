-- ============================================================
-- ONE-TIME setup AFTER registering the privileged users via the API
-- (Postman folder "1 - Registration" registers them with the default
--  CUSTOMER role, because registration always assigns CUSTOMER).
--
-- How to use:
--   1) Run the app (seeder creates roles/permissions/grants).
--   2) In Postman run folder 0 + 1 (registers all 5 users).
--   3) Run THIS script (promo the 3 personas below).
--   4) In Postman continue from folder 2 to the end.
-- ============================================================

-- Promote admin@bank.com to ADMIN
UPDATE customer_role
   SET role_id = (SELECT id FROM role WHERE name = 'ADMIN')
 WHERE customer_id = (SELECT id FROM customer WHERE email = 'admin@bank.com');

-- Promote manager@bank.com to BANK_MANAGER
UPDATE customer_role
   SET role_id = (SELECT id FROM role WHERE name = 'BANK_MANAGER')
 WHERE customer_id = (SELECT id FROM customer WHERE email = 'manager@bank.com');

-- Promote auditor@bank.com to AUDITOR
UPDATE customer_role
   SET role_id = (SELECT id FROM role WHERE name = 'AUDITOR')
 WHERE customer_id = (SELECT id FROM customer WHERE email = 'auditor@bank.com');

-- Verify (expect ADMIN/BANK_MANAGER/AUDITOR/CUSTOMER rows for the 5 users)
SELECT c.email, r.name AS role_name
  FROM customer c
  JOIN customer_role cr ON cr.customer_id = c.id
  JOIN role r           ON r.id = cr.role_id
 ORDER BY c.email;

-- ============================================================
-- Sanity check — account/transfer statuses must already exist
-- (they were seeded in the earlier database setup).
-- If the SELECT returns nothing, tell me and you will get INSERT
-- statements for them too (they need sequence-aware ids).
-- ============================================================
SELECT name FROM account_status;
SELECT name FROM transfer_status;

COMMIT;
