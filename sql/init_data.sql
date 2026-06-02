DROP SCHEMA IF EXISTS kursovaya4sem CASCADE;
CREATE SCHEMA kursovaya4sem;

-- Справочники
CREATE TABLE kursovaya4sem.prorab (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    phone       VARCHAR(50)
);

CREATE TABLE kursovaya4sem.material_card (
    id              BIGSERIAL PRIMARY KEY,
    name            VARCHAR(255) NOT NULL,
    article         INTEGER,
    unit            VARCHAR(50),
    write_off_rate  INTEGER
);

CREATE TABLE kursovaya4sem.supplier (
    id              BIGSERIAL PRIMARY KEY,
    name            VARCHAR(255) NOT NULL,
    contact_person  VARCHAR(255),
    phone           VARCHAR(50),
    email           VARCHAR(100),
    address         VARCHAR(255)
);

CREATE TABLE kursovaya4sem.tool (
    id                BIGSERIAL PRIMARY KEY,
    name              VARCHAR(255) NOT NULL,
    inventory_number  VARCHAR(50),
    condition         VARCHAR(100)
);

CREATE TABLE kursovaya4sem.building_object (
    id        BIGSERIAL PRIMARY KEY,
    name      VARCHAR(255) NOT NULL,
    address   VARCHAR(255),
    prorab_id BIGINT REFERENCES kursovaya4sem.prorab(id)
);

-- Склад и документы
CREATE TABLE kursovaya4sem.material_stock (
    id           BIGSERIAL PRIMARY KEY,
    material_id  BIGINT NOT NULL REFERENCES kursovaya4sem.material_card(id),
    quantity     NUMERIC(12, 2) NOT NULL DEFAULT 0,
    last_updated TIMESTAMP
);

CREATE TABLE kursovaya4sem.material_receipt (
    id           BIGSERIAL PRIMARY KEY,
    material_id  BIGINT NOT NULL REFERENCES kursovaya4sem.material_card(id),
    supplier_id  BIGINT NOT NULL REFERENCES kursovaya4sem.supplier(id),
    quantity     NUMERIC(12, 2) NOT NULL,
    price        NUMERIC(12, 2) NOT NULL,
    receipt_date DATE NOT NULL
);

CREATE TABLE kursovaya4sem.invoice (
    id              BIGSERIAL PRIMARY KEY,
    invoice_number  VARCHAR(50) NOT NULL,
    object_id       BIGINT NOT NULL REFERENCES kursovaya4sem.building_object(id),
    issue_date      DATE NOT NULL
);

CREATE TABLE kursovaya4sem.invoice_item (
    id              BIGSERIAL PRIMARY KEY,
    invoice_id      BIGINT NOT NULL REFERENCES kursovaya4sem.invoice(id),
    material_id     BIGINT NOT NULL REFERENCES kursovaya4sem.material_card(id),
    quantity        NUMERIC(12, 2) NOT NULL,
    price_at_moment NUMERIC(12, 2) NOT NULL
);

CREATE TABLE kursovaya4sem.material_return (
    id           BIGSERIAL PRIMARY KEY,
    invoice_id   BIGINT NOT NULL REFERENCES kursovaya4sem.invoice(id),
    material_id  BIGINT NOT NULL REFERENCES kursovaya4sem.material_card(id),
    quantity     NUMERIC(12, 2) NOT NULL,
    return_date  DATE NOT NULL
);

CREATE TABLE kursovaya4sem.tool_issue (
    id                  BIGSERIAL PRIMARY KEY,
    tool_id             BIGINT NOT NULL REFERENCES kursovaya4sem.tool(id),
    object_id           BIGINT NOT NULL REFERENCES kursovaya4sem.building_object(id),
    issued_to           VARCHAR(255) NOT NULL,
    issue_date          DATE NOT NULL,
    return_date         DATE,
    actual_return_date  DATE
);

-- Прорабы
INSERT INTO kursovaya4sem.prorab (name, phone) VALUES
    ('Иванов И.И.', '+7-900-111-22-33'),
    ('Петров П.П.', '+7-900-444-55-66');

-- Материалы
INSERT INTO kursovaya4sem.material_card (name, article, unit, write_off_rate) VALUES
    ('Цемент М500', 101, 'кг', 5),
    ('Песок речной', 102, 'м³', 3),
    ('Кирпич красный', 103, 'шт', 2),
    ('Арматура 12мм', 104, 'м', 1);

-- Поставщики
INSERT INTO kursovaya4sem.supplier (name, contact_person, phone, email, address) VALUES
    ('ООО СтройСнаб', 'Сидоров А.В.', '+7-495-100-20-30', 'info@stroysnab.ru', 'г. Москва, ул. Складская, 1'),
    ('ИП Козлов', 'Козлов В.М.', '+7-916-200-30-40', 'kozlov@mail.ru', 'г. Москва, ул. Торговая, 5'),
    ('ЗАО БетонПром', 'Николаев С.П.', '+7-495-300-40-50', 'sales@betonprom.ru', 'г. Химки, ш. Ленинградское, 10');

-- Инструменты
INSERT INTO kursovaya4sem.tool (name, inventory_number, condition) VALUES
    ('Перфоратор Bosch', 'INV-001', 'Исправен'),
    ('Болгарка Makita', 'INV-002', 'Исправен'),
    ('Уровень строительный', 'INV-003', 'Исправен'),
    ('Лопата штыковая', 'INV-004', 'Исправен');

-- Объекты стройки
INSERT INTO kursovaya4sem.building_object (name, address, prorab_id) VALUES
    ('ЖК Ремстрой', 'г. Муром, ул. Орловская, 19', 1),
    ('Склад №2', 'г. Муром, ул. Лакина, 3', 2),
    ('Инстутиут МИВлГУ (капремонт)', 'г. Муром, ул. Орловская, 23', 1);

-- Остатки на складе (чтобы можно было выдавать материалы)
INSERT INTO kursovaya4sem.material_stock (material_id, quantity, last_updated) VALUES
    (1, 5000.00, NOW()),
    (2, 120.50, NOW()),
    (3, 10000.00, NOW()),
    (4, 800.00, NOW());

-- Пример прихода
INSERT INTO kursovaya4sem.material_receipt (material_id, supplier_id, quantity, price, receipt_date) VALUES
    (1, 1, 1000.00, 45.50, '2025-09-01'),
    (2, 2, 50.00, 1200.00, '2025-09-05');

-- Пример выдачи инструмента (ещё не возвращён - для отчёта "должники")
INSERT INTO kursovaya4sem.tool_issue (tool_id, object_id, issued_to, issue_date, return_date) VALUES
    (1, 1, 'Смирнов О.Л.', '2025-10-01', '2025-10-15'),
    (2, 2, 'Кузнецов Д.А.', '2025-10-10', '2025-10-25');
