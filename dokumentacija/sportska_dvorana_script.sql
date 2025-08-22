-- Prvo brišemo sve postojeće tabele (u pravom redosledu)
DROP TABLE IF EXISTS reservation CASCADE;
DROP TABLE IF EXISTS payment CASCADE;
DROP TABLE IF EXISTS hall_sport CASCADE;
DROP TABLE IF EXISTS hall CASCADE;
DROP TABLE IF EXISTS sport CASCADE;
DROP TABLE IF EXISTS status CASCADE;
DROP TABLE IF EXISTS "user" CASCADE;
DROP TABLE IF EXISTS role CASCADE;

-- 1. ROLE
CREATE TABLE role (
    role_id SERIAL PRIMARY KEY,
    role VARCHAR(50) NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

-- 2. USER
CREATE TABLE "user" (
    user_id SERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20),
    birth_date DATE,
    role_id INT NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT fk_user_role FOREIGN KEY (role_id) REFERENCES role(role_id) ON DELETE CASCADE
);

-- 3. STATUS
CREATE TABLE status (
    status_id SERIAL PRIMARY KEY,
    status VARCHAR(50) NOT NULL, 
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

-- 4. HALL
CREATE TABLE hall (
    hall_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    capacity INT NOT NULL,
    price_per_hour NUMERIC(10,2) NOT NULL DEFAULT 0.00,
    description TEXT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

-- 5. SPORT
CREATE TABLE sport (
    sport_id SERIAL PRIMARY KEY,
    sport VARCHAR(100) NOT null,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

-- 6. HALL_SPORT (many-to-many)
CREATE TABLE hall_sport (
    hall_id INT NOT NULL,
    sport_id INT NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    PRIMARY KEY (hall_id, sport_id),
    CONSTRAINT fk_hall FOREIGN KEY (hall_id) REFERENCES hall(hall_id) ON DELETE CASCADE,
    CONSTRAINT fk_sport FOREIGN KEY (sport_id) REFERENCES sport(sport_id) ON DELETE CASCADE
);

-- 7. RESERVATION 
CREATE TABLE reservation (
    reservation_id SERIAL PRIMARY KEY,
    hall_id INT NOT NULL,
    sport_id INT NOT NULL,
    user_id INT NOT NULL,
    status_id INT NOT NULL,
    date DATE NOT NULL,
    time_from TIME(6) NOT NULL,
    time_to TIME(6) NOT NULL,
    final_price NUMERIC(10,2) NOT NULL DEFAULT 0.00,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT fk_reservation_hall FOREIGN KEY (hall_id) REFERENCES hall(hall_id) ON DELETE CASCADE,
    CONSTRAINT fk_reservation_sport FOREIGN KEY (sport_id) REFERENCES sport(sport_id) ON DELETE CASCADE,
    CONSTRAINT fk_reservation_user FOREIGN KEY (user_id) REFERENCES "user"(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_reservation_status FOREIGN KEY (status_id) REFERENCES status(status_id) ON DELETE CASCADE
);

-- 8. PAYMENT
CREATE TABLE payment (
    payment_id SERIAL PRIMARY KEY,
    stripe_id VARCHAR(255) NOT NULL,
    amount NUMERIC(10,2) NOT NULL,
    currency VARCHAR(10) NOT NULL,
    payment_status VARCHAR(50) NOT NULL,
    reservation_id INT UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT fk_payment_reservation FOREIGN KEY (reservation_id) REFERENCES reservation(reservation_id) ON DELETE CASCADE
);

-----------------------------------------------------
-- TESTNI PODACI
-----------------------------------------------------

-- ROLE
INSERT INTO role (role, created_at, updated_at) VALUES
('admin', NOW(), NOW()),
('korisnik', NOW(), NOW());

-- STATUS
INSERT INTO status (status, created_at, updated_at) VALUES
('kreirana', NOW(), NOW()),
('placena', NOW(), NOW()),
('zavrsena', NOW(), NOW()),
('otkazana', NOW(), NOW());

-- HALL
INSERT INTO hall (name, capacity, price_per_hour, description, created_at, updated_at) VALUES
('Velika sala', 50, 3000.00, '...', NOW(), NOW()),
('Mala sala', 40, 1500.00, '...', NOW(), NOW()),
('Sala za džudo', 30, 600.00, '...', NOW(), NOW()),
('Rvačka sala', 20, 700.00, '...', NOW(), NOW());

-- SPORT
INSERT INTO sport (sport, created_at, updated_at) VALUES
('Košarka', NOW(), NOW()),
('Odbojka', NOW(), NOW()),
('Rukomet', NOW(), NOW()),
('Mali fudbal', NOW(), NOW()),
('Džudo', NOW(), NOW()),
('Rvanje', NOW(), NOW());

-- HALL_SPORT 
INSERT INTO hall_sport (hall_id, sport_id, created_at, updated_at) VALUES
(1, 1, NOW(), NOW()),
(1, 2, NOW(), NOW()),
(1, 3, NOW(), NOW()),
(2, 4, NOW(), NOW()),
(2, 2, NOW(), NOW()),
(2, 1, NOW(), NOW()),
(3, 5, NOW(), NOW()),
(4, 6, NOW(), NOW());        

-- USERS
INSERT INTO "user" (first_name, last_name, username, email, password, phone_number, birth_date, role_id, created_at, updated_at) VALUES
('Marko', 'Marković', 'marko.m', 'marko@example.com', 'hashed_password1', '0601234567', '1990-05-10', 2, NOW(), NOW()),
('Jelena', 'Jovanović', 'jelena.j', 'jelena@example.com', 'hashed_password2', '0612345678', '1988-12-15', 2, NOW(), NOW()),
('Petar', 'Petrović', 'petar.p', 'petar@example.com', 'hashed_password3', NULL, '1995-07-20', 2, NOW(), NOW()),
('Sofija', 'Dangubic', 'sofijad', 'sofijad@gmail.com', '$2a$12$dlDej.ij.k7YNeCcj84d0.APY1OSZ8sVcHHRYocTMMyRw1jFl6Pe.', NULL, '2001-02-16', 1, NOW(), NOW());

-- RESERVATIONS (ubaciti pre plaćanja!)
INSERT INTO reservation (hall_id, sport_id, user_id, status_id, date, time_from, time_to, final_price, created_at, updated_at) VALUES
(1, 1, 1, 1, '2025-08-04', '11:00', '12:00', 3000.00, NOW(), NOW()),
(1, 2, 2, 2, '2025-08-04', '15:00', '17:00', 3000.00, NOW(), NOW()),
(1, 1, 2, 1, '2025-08-05', '10:00', '12:00', 6000.00, NOW(), NOW()),
(1, 1, 2, 2, '2025-08-05', '16:00', '17:00', 3000.00, NOW(), NOW()),
(3, 5, 2, 3, '2025-07-16', '16:00', '18:00', 2000.00, NOW(), NOW()),
(2, 3, 3, 4, '2025-07-15', '12:00', '14:00', 3000.00, NOW(), NOW()),
(2, 3, 4, 1, '2025-08-10', '09:00', '11:00', 3000.00, NOW(), NOW()),
(1, 2, 4, 2, '2025-08-12', '14:00', '16:00', 3000.00, NOW(), NOW()),
(3, 5, 4, 4, '2025-08-15', '17:00', '19:00', 2000.00, NOW(), NOW()),
(1, 1, 4, 3, '2025-08-01', '10:00', '11:00', 3000.00, NOW(), NOW());

-- PAYMENT (sada postoji reservation_id!)
INSERT INTO payment (stripe_id, amount, currency, payment_status, reservation_id, created_at, updated_at) VALUES
('pi_1A2B3C', 1500.00, 'RSD', 'PAID', 2, NOW(), NOW()),
('pi_4D5E6F', 1200.00, 'RSD', 'PAID', 4, NOW(), NOW()),
('pi_7G8H9I', 800.00, 'RSD', 'PAID', 5, NOW(), NOW()),
('pi_0J1K2L', 600.00, 'RSD', 'PAID', 6, NOW(), NOW());