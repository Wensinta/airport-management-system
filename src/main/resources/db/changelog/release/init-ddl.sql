--liquibase formatted sql

--changeset yaroslav:init-tables-1
-- таблица пользователей
CREATE TABLE IF NOT EXISTS users (
	id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	username VARCHAR(50) NOT NULL,
	password VARCHAR(100) NOT NULL,
	role VARCHAR(20) NOT NULL,
	first_name VARCHAR(50) NOT NULL,
	last_name VARCHAR(50) NOT NULL,
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT users_username_uniqueness UNIQUE(username),
	CONSTRAINT users_role_check CHECK (role IN ('ADMIN', 'PASSENGER', 'AIRPORT_STAFF', 'BORDER_GUARD', 'CUSTOMS_OFFICER'))
);


-- Таблица рейсов
CREATE TABLE IF NOT EXISTS flights (
	id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	flight_number VARCHAR(10) UNIQUE NOT NULL,
	departure_city VARCHAR(50) NOT NULL,
	arrival_city VARCHAR(50) NOT NULL,
	departure_time TIMESTAMP NOT NULL,
	arrival_time TIMESTAMP NOT NULL,
	total_seats INTEGER NOT NULL,
	available_seats INTEGER NOT NULL,
	status VARCHAR(20) DEFAULT 'SCHEDULED' NOT NULL,
	created_by INTEGER REFERENCES users(id),

    CONSTRAINT flights_flight_number_uniqueness UNIQUE(flight_number),
	CONSTRAINT flights_status_check CHECK (status IN ('SCHEDULED', 'BOARDING', 'DEPARTED', 'ARRIVED'))
);


-- Таблица пассажиров (связь с пользователями)
CREATE TABLE IF NOT EXISTS passengers (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id INTEGER UNIQUE REFERENCES users(id) ON DELETE CASCADE,
    passport_number VARCHAR(20) UNIQUE NOT NULL,
    phone VARCHAR(20),
    email VARCHAR(100),
    luggage_checked BOOLEAN DEFAULT FALSE
);

-- Таблица билетов
CREATE TABLE IF NOT EXISTS tickets (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    flight_id INTEGER REFERENCES flights(id) ON DELETE CASCADE,
    passenger_id INTEGER REFERENCES passengers(id) ON DELETE CASCADE,
    seat_number VARCHAR(10) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    ticket_number VARCHAR(20) NOT NULL,
    status VARCHAR(20) DEFAULT 'BOOKED',
    booking_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT ticket_number_uniqueness UNIQUE(ticket_number),
    CONSTRAINT tickets_status_check CHECK (status IN ('BOOKED', 'CHECKED_IN', 'BOARDED')),
    CONSTRAINT tickets_seat_number_uniqueness UNIQUE(flight_id, seat_number)
);

-- Таблица посадочных талонов
CREATE TABLE IF NOT EXISTS boarding_passes (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    ticket_id INTEGER UNIQUE REFERENCES tickets(id) ON DELETE CASCADE,
    check_in_time TIMESTAMP,
    passport_verified BOOLEAN DEFAULT FALSE,
    luggage_verified BOOLEAN DEFAULT FALSE,
    boarded BOOLEAN DEFAULT FALSE,
    verified_by_border_guard INTEGER REFERENCES users(id),
    verified_by_customs INTEGER REFERENCES users(id)
);

-- Индексы для оптимизации запросов
CREATE INDEX idx_flights_departure ON flights(departure_city, departure_time);
CREATE INDEX idx_flights_status ON flights(status);
CREATE INDEX idx_tickets_passenger ON tickets(passenger_id);
CREATE INDEX idx_tickets_flight ON tickets(flight_id);
CREATE INDEX idx_tickets_status ON tickets(status);
CREATE INDEX idx_passengers_passport ON passengers(passport_number);
CREATE INDEX idx_passengers_user ON passengers(user_id);
CREATE INDEX idx_boarding_passes_ticket ON boarding_passes(ticket_id);
