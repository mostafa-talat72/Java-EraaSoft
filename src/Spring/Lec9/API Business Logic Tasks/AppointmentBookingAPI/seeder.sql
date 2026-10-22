-- ═══ Run AFTER starting the app once (tables created byddl-auto: update) ═══
-- High ids (1000+) so they never collide with app-generated ids starting at 1.

-- ── Statuses (the dictionary the service resolves BY NAME) ──
INSERT INTO appointment_status (id, name) VALUES (5000, 'BOOKED');
INSERT INTO appointment_status (id, name) VALUES (5001, 'CANCELED');

-- ── Specialties ──
INSERT INTO specialty (id, name) VALUES (1000, 'Cardiology');
INSERT INTO specialty (id, name) VALUES (1001, 'General');

-- ── Doctors ──
INSERT INTO doctor (id, name, specialty_id) VALUES (2000, 'Dr. Karim', 1000);
INSERT INTO doctor (id, name, specialty_id) VALUES (2001, 'Dr. Nada',  1001);

-- ── Patients (unique emails) ──
INSERT INTO patient (id, name, email) VALUES (3000, 'Ahmed', 'ahmed@mail.com');
INSERT INTO patient (id, name, email) VALUES (3001, 'Mona',  'mona@mail.com');
INSERT INTO patient (id, name, email) VALUES (3002, 'Omar',  'omar@mail.com');

-- ── Medical services (rule 3 source) ──
INSERT INTO medical_service (id, name, duration_minutes) VALUES (4000, 'General Checkup', 30);
INSERT INTO medical_service (id, name, duration_minutes) VALUES (4001, 'Surgery',        120);

-- ── Rule 4 demo: Dr. Nada is LEAVING [10 Nov 2025 09:00 → 13:00] ──
INSERT INTO doctor_unavailability (id, doctor_id, start_date_time, end_date_time)
VALUES (6000, 2001, TIMESTAMP '2025-11-10 09:00:00', TIMESTAMP '2025-11-10 13:00:00');

COMMIT;