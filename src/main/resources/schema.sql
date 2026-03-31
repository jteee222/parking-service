CREATE TABLE parking_lots (
    lot_id VARCHAR(50) PRIMARY KEY,
    location VARCHAR(255),
    capacity INT NOT NULL,
    occupied_spaces INT NOT NULL,
    cost_per_minute DOUBLE
);

CREATE TABLE vehicles (
    license_plate VARCHAR(20) PRIMARY KEY,
    type VARCHAR(20),
    owner_name VARCHAR(100)
);

CREATE TABLE parking_sessions (
    session_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    lot_id VARCHAR(50),
    license_plate VARCHAR(20),
    entry_time TIMESTAMP,
    exit_time TIMESTAMP,
    total_cost DOUBLE,
    CONSTRAINT fk_parking_lot FOREIGN KEY (lot_id) REFERENCES parking_lots(lot_id),
    CONSTRAINT fk_vehicle FOREIGN KEY (license_plate) REFERENCES vehicles(license_plate)
);