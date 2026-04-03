-- 1. Таблица ролей
CREATE TABLE roles
(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- Таблица пользователей
CREATE TABLE users
(
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(100),
    last_name  VARCHAR(100),
    role_id BIGINT NOT NULL REFERENCES roles (id)
);

-- Точки проката (с иерархией: parent_id ссылается на эту же таблицу)
CREATE TABLE rental_points
(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    latitude DECIMAL(10, 8),
    longitude DECIMAL(11, 8),
    parent_id BIGINT REFERENCES rental_points (id)
);

-- Справочник моделей самокатов
CREATE TABLE scooter_models
(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    max_weight INT
);

-- Физические самокаты
CREATE TABLE scooters
(
    id BIGSERIAL PRIMARY KEY,
    model_id BIGINT NOT NULL REFERENCES scooter_models (id),
    rental_point_id BIGINT REFERENCES rental_points (id),
    status VARCHAR(50) NOT NULL,
    mileage DECIMAL(8, 2) DEFAULT 0.0
);

-- Тарифы
CREATE TABLE tariffs
(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(50) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    discount DECIMAL(10, 2) DEFAULT 0.0
);

-- История аренд
CREATE TABLE rentals
(
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users (id),
    scooter_id BIGINT NOT NULL REFERENCES scooters (id),
    tariff_id BIGINT NOT NULL REFERENCES tariffs (id),
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP,
    start_point_id BIGINT NOT NULL REFERENCES rental_points (id),
    end_point_id BIGINT REFERENCES rental_points (id),
    total_cost DECIMAL(10, 2)
);