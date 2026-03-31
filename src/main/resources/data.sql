INSERT INTO parking_lots (lot_id, location, capacity, occupied_spaces, cost_per_minute)
VALUES
('LOT1', 'Manila', 5, 0, 2.5),
('LOT2', 'Cebu', 3, 0, 3.0),
('LOT3', 'Davao', 4, 0, 2.0);

INSERT INTO vehicles (license_plate, type, owner_name)
VALUES
('AAA-111', 'CAR', 'Justine Encar'),
('BBB-222', 'MOTORCYCLE', 'Jtee Encar'),
('CCC-333', 'TRUCK', 'Justi Encar'),
('DDD-444', 'CAR', 'Justi Encarnacion'),
('EEE-555', 'MOTORCYCLE', 'Just Encar');

INSERT INTO parking_sessions (lot_id, license_plate, entry_time, exit_time, total_cost)
VALUES
('LOT1', 'AAA-111', CURRENT_TIMESTAMP(), NULL, NULL),
('LOT2', 'BBB-222', '2026-03-31T09:10:00', NULL, NULL),
('LOT1', 'CCC-333', '2026-03-30T08:00:00', '2026-03-30T10:00:00', 240.0),
('LOT3', 'DDD-444', '2026-03-29T14:30:00', '2026-03-29T15:30:00', 120.0);