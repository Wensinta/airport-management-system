--liquibase formatted sql

--changeset yaroslav:init-dml-1
-- 1. Вставка пользователей
INSERT INTO users (username, password, role, first_name, last_name) VALUES
-- Администраторы (пароль: admin123 -> $2a$10$pGqlJTo1OXbmWzPpr0sgxOKD.8sT9wUVp.L1AqvUPRjmfssNYoooW)
('admin', '$2a$10$pGqlJTo1OXbmWzPpr0sgxOKD.8sT9wUVp.L1AqvUPRjmfssNYoooW', 'ADMIN', 'Иван', 'Петров'),
('admin2', '$2a$10$pGqlJTo1OXbmWzPpr0sgxOKD.8sT9wUVp.L1AqvUPRjmfssNYoooW', 'ADMIN', 'Ольга', 'Семенова'),

-- Сотрудники аэропорта (пароль: staff123 -> $2a$10$9ih0/Cd3Y93xzGkqvEVdZe2mTMka8NZEiYBd1h1m3UnkISv8SgIna)
('staff1', '$2a$10$9ih0/Cd3Y93xzGkqvEVdZe2mTMka8NZEiYBd1h1m3UnkISv8SgIna', 'AIRPORT_STAFF', 'Мария', 'Сидорова'),
('staff2', '$2a$10$9ih0/Cd3Y93xzGkqvEVdZe2mTMka8NZEiYBd1h1m3UnkISv8SgIna', 'AIRPORT_STAFF', 'Сергей', 'Васильев'),
('staff3', '$$2a$10$9ih0/Cd3Y93xzGkqvEVdZe2mTMka8NZEiYBd1h1m3UnkISv8SgIna', 'AIRPORT_STAFF', 'Анна', 'Кузнецова'),

-- Пограничники (пароль: border123 -> $2a$10$f6Cfb1wqiUm/6iV1k/6Bt.8BqzqmV1Zw/pupQHVVjQoZHvCfCCao2)
('border1', '$2a$10$f6Cfb1wqiUm/6iV1k/6Bt.8BqzqmV1Zw/pupQHVVjQoZHvCfCCao2', 'BORDER_GUARD', 'Алексей', 'Козлов'),
('border2', '$2a$10$f6Cfb1wqiUm/6iV1k/6Bt.8BqzqmV1Zw/pupQHVVjQoZHvCfCCao2', 'BORDER_GUARD', 'Дмитрий', 'Орлов'),
('border3', '$2a$10$f6Cfb1wqiUm/6iV1k/6Bt.8BqzqmV1Zw/pupQHVVjQoZHvCfCCao2', 'BORDER_GUARD', 'Наталья', 'Волкова'),

-- Таможенники (пароль: customs123 -> $2a$10$REu44wOXYaj/mZqZTkT7qu.jxB9Hp/G8oVTeCldvGwFFh38Z/We5O)
('customs1', '$2a$10$REu44wOXYaj/mZqZTkT7qu.jxB9Hp/G8oVTeCldvGwFFh38Z/We5O', 'CUSTOMS_OFFICER', 'Елена', 'Николаева'),
('customs2', '$2a$10$REu44wOXYaj/mZqZTkT7qu.jxB9Hp/G8oVTeCldvGwFFh38Z/We5O', 'CUSTOMS_OFFICER', 'Артем', 'Зайцев'),
('customs3', '$2a$10$REu44wOXYaj/mZqZTkT7qu.jxB9Hp/G8oVTeCldvGwFFh38Z/We5O', 'CUSTOMS_OFFICER', 'Ирина', 'Павлова'),

-- Пассажиры (пароль: pass123 -> $2a$10$A5iZR/zGii6K6iMexWylj.z1nYlszqKZSIC9AgqibGUWfVt3.9qBS)
('passenger1', '$2a$10$A5iZR/zGii6K6iMexWylj.z1nYlszqKZSIC9AgqibGUWfVt3.9qBS', 'PASSENGER', 'Анна', 'Смирнова'),
('passenger2', '$2a$10$A5iZR/zGii6K6iMexWylj.z1nYlszqKZSIC9AgqibGUWfVt3.9qBS', 'PASSENGER', 'Михаил', 'Попов'),
('passenger3', '$2a$10$A5iZR/zGii6K6iMexWylj.z1nYlszqKZSIC9AgqibGUWfVt3.9qBS', 'PASSENGER', 'Екатерина', 'Новикова'),
('passenger4', '$2a$10$A5iZR/zGii6K6iMexWylj.z1nYlszqKZSIC9AgqibGUWfVt3.9qBS', 'PASSENGER', 'Александр', 'Морозов'),
('passenger5', '$2a$10$A5iZR/zGii6K6iMexWylj.z1nYlszqKZSIC9AgqibGUWfVt3.9qBS', 'PASSENGER', 'Дарья', 'Васнецова'),
('passenger6', '$2a$10$A5iZR/zGii6K6iMexWylj.z1nYlszqKZSIC9AgqibGUWfVt3.9qBS', 'PASSENGER', 'Павел', 'Соколов'),
('passenger7', '$2a$10$A5iZR/zGii6K6iMexWylj.z1nYlszqKZSIC9AgqibGUWfVt3.9qBS', 'PASSENGER', 'Юлия', 'Лебедева'),
('passenger8', '$2a$10$A5iZR/zGii6K6iMexWylj.z1nYlszqKZSIC9AgqibGUWfVt3.9qBS', 'PASSENGER', 'Роман', 'Ковалев'),
('passenger9', '$2a$10$A5iZR/zGii6K6iMexWylj.z1nYlszqKZSIC9AgqibGUWfVt3.9qBS', 'PASSENGER', 'Светлана', 'Федорова'),
('passenger10', '$2a$10$A5iZR/zGii6K6iMexWylj.z1nYlszqKZSIC9AgqibGUWfVt3.9qBS', 'PASSENGER', 'Виктор', 'Медведев');

-- ... остальная часть DML скрипта остается без изменений