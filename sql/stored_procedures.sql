-- ========== Складские операции ==========

CREATE OR REPLACE FUNCTION kursovaya4sem.fn_register_material_receipt(
    p_material_id  BIGINT,
    p_supplier_id  BIGINT,
    p_quantity     NUMERIC,
    p_price        NUMERIC,
    p_receipt_date DATE
) RETURNS BIGINT
LANGUAGE plpgsql
AS $$
DECLARE
    v_receipt_id BIGINT;
    v_stock_id   BIGINT;
BEGIN
    IF p_quantity <= 0 THEN
        RAISE EXCEPTION 'Количество должно быть больше нуля';
    END IF;

    INSERT INTO kursovaya4sem.material_receipt (material_id, supplier_id, quantity, price, receipt_date)
    VALUES (p_material_id, p_supplier_id, p_quantity, p_price, p_receipt_date)
    RETURNING id INTO v_receipt_id;

    SELECT id INTO v_stock_id
    FROM kursovaya4sem.material_stock
    WHERE material_id = p_material_id
    FOR UPDATE;

    IF FOUND THEN
        UPDATE kursovaya4sem.material_stock
        SET quantity = quantity + p_quantity, last_updated = NOW()
        WHERE id = v_stock_id;
    ELSE
        INSERT INTO kursovaya4sem.material_stock (material_id, quantity, last_updated)
        VALUES (p_material_id, p_quantity, NOW());
    END IF;

    RETURN v_receipt_id;
END;
$$;

CREATE OR REPLACE FUNCTION kursovaya4sem.fn_issue_material(
    p_invoice_number VARCHAR,
    p_object_id      BIGINT,
    p_issue_date     DATE,
    p_material_id    BIGINT,
    p_quantity       NUMERIC,
    p_price          NUMERIC
) RETURNS BIGINT
LANGUAGE plpgsql
AS $$
DECLARE
    v_stock_id    BIGINT;
    v_current     NUMERIC;
    v_invoice_id  BIGINT;
BEGIN
    IF p_quantity <= 0 THEN
        RAISE EXCEPTION 'Количество должно быть больше нуля';
    END IF;

    SELECT id, quantity INTO v_stock_id, v_current
    FROM kursovaya4sem.material_stock
    WHERE material_id = p_material_id
    FOR UPDATE;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'Материала нет на складе';
    END IF;
    IF v_current < p_quantity THEN
        RAISE EXCEPTION 'Недостаточно материала на складе';
    END IF;

    INSERT INTO kursovaya4sem.invoice (invoice_number, object_id, issue_date)
    VALUES (p_invoice_number, p_object_id, p_issue_date)
    RETURNING id INTO v_invoice_id;

    INSERT INTO kursovaya4sem.invoice_item (invoice_id, material_id, quantity, price_at_moment)
    VALUES (v_invoice_id, p_material_id, p_quantity, p_price);

    UPDATE kursovaya4sem.material_stock
    SET quantity = quantity - p_quantity, last_updated = NOW()
    WHERE id = v_stock_id;

    RETURN v_invoice_id;
END;
$$;

CREATE OR REPLACE FUNCTION kursovaya4sem.fn_return_material(
    p_invoice_id   BIGINT,
    p_material_id  BIGINT,
    p_quantity     NUMERIC,
    p_return_date  DATE
) RETURNS BIGINT
LANGUAGE plpgsql
AS $$
DECLARE
    v_return_id BIGINT;
    v_stock_id  BIGINT;
BEGIN
    IF p_quantity <= 0 THEN
        RAISE EXCEPTION 'Количество должно быть больше нуля';
    END IF;

    INSERT INTO kursovaya4sem.material_return (invoice_id, material_id, quantity, return_date)
    VALUES (p_invoice_id, p_material_id, p_quantity, p_return_date)
    RETURNING id INTO v_return_id;

    SELECT id INTO v_stock_id
    FROM kursovaya4sem.material_stock
    WHERE material_id = p_material_id
    FOR UPDATE;

    IF FOUND THEN
        UPDATE kursovaya4sem.material_stock
        SET quantity = quantity + p_quantity, last_updated = NOW()
        WHERE id = v_stock_id;
    ELSE
        INSERT INTO kursovaya4sem.material_stock (material_id, quantity, last_updated)
        VALUES (p_material_id, p_quantity, NOW());
    END IF;

    RETURN v_return_id;
END;
$$;

CREATE OR REPLACE FUNCTION kursovaya4sem.fn_issue_tool(
    p_tool_id      BIGINT,
    p_object_id    BIGINT,
    p_issued_to    VARCHAR,
    p_issue_date   DATE,
    p_return_date  DATE
) RETURNS BIGINT
LANGUAGE plpgsql
AS $$
DECLARE
    v_id BIGINT;
BEGIN
    INSERT INTO kursovaya4sem.tool_issue (tool_id, object_id, issued_to, issue_date, return_date)
    VALUES (p_tool_id, p_object_id, p_issued_to, p_issue_date, p_return_date)
    RETURNING id INTO v_id;
    RETURN v_id;
END;
$$;

CREATE OR REPLACE FUNCTION kursovaya4sem.fn_return_tool(
    p_issue_id            BIGINT,
    p_actual_return_date  DATE,
    p_condition           VARCHAR
) RETURNS VOID
LANGUAGE plpgsql
AS $$
DECLARE
    v_tool_id BIGINT;
BEGIN
    UPDATE kursovaya4sem.tool_issue
    SET actual_return_date = p_actual_return_date
    WHERE id = p_issue_id
    RETURNING tool_id INTO v_tool_id;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'Выдача инструмента не найдена';
    END IF;

    UPDATE kursovaya4sem.tool
    SET condition = p_condition
    WHERE id = v_tool_id;
END;
$$;

-- ========== Справочники ==========

CREATE OR REPLACE FUNCTION kursovaya4sem.fn_insert_material_card(
    p_name VARCHAR, p_article INTEGER, p_unit VARCHAR, p_write_off_rate INTEGER
) RETURNS BIGINT
LANGUAGE sql
AS $$
    INSERT INTO kursovaya4sem.material_card (name, article, unit, write_off_rate)
    VALUES (p_name, p_article, p_unit, p_write_off_rate)
    RETURNING id;
$$;

CREATE OR REPLACE FUNCTION kursovaya4sem.fn_insert_supplier(
    p_name VARCHAR, p_contact_person VARCHAR, p_phone VARCHAR, p_email VARCHAR, p_address VARCHAR
) RETURNS BIGINT
LANGUAGE sql
AS $$
    INSERT INTO kursovaya4sem.supplier (name, contact_person, phone, email, address)
    VALUES (p_name, p_contact_person, p_phone, p_email, p_address)
    RETURNING id;
$$;

CREATE OR REPLACE FUNCTION kursovaya4sem.fn_insert_prorab(p_name VARCHAR, p_phone VARCHAR)
RETURNS BIGINT
LANGUAGE sql
AS $$
    INSERT INTO kursovaya4sem.prorab (name, phone) VALUES (p_name, p_phone) RETURNING id;
$$;

CREATE OR REPLACE FUNCTION kursovaya4sem.fn_insert_building_object(
    p_name VARCHAR, p_address VARCHAR, p_prorab_id BIGINT
) RETURNS BIGINT
LANGUAGE sql
AS $$
    INSERT INTO kursovaya4sem.building_object (name, address, prorab_id)
    VALUES (p_name, p_address, p_prorab_id)
    RETURNING id;
$$;

CREATE OR REPLACE FUNCTION kursovaya4sem.fn_insert_tool(
    p_name VARCHAR, p_inventory_number VARCHAR, p_condition VARCHAR
) RETURNS BIGINT
LANGUAGE sql
AS $$
    INSERT INTO kursovaya4sem.tool (name, inventory_number, condition)
    VALUES (p_name, p_inventory_number, p_condition)
    RETURNING id;
$$;
