-- Аудит изменений

CREATE TABLE IF NOT EXISTS kursovaya4sem.audit_log (
    id          BIGSERIAL PRIMARY KEY,
    table_name  VARCHAR(64)  NOT NULL,
    operation   VARCHAR(10)  NOT NULL,
    record_id   BIGINT,
    old_data    JSONB,
    new_data    JSONB,
    changed_at  TIMESTAMP    NOT NULL DEFAULT NOW(),
    db_user     VARCHAR(64)  NOT NULL DEFAULT current_user
);

CREATE OR REPLACE FUNCTION kursovaya4sem.fn_audit_trigger()
RETURNS TRIGGER
LANGUAGE plpgsql
AS $$
DECLARE
    v_record_id BIGINT;
    v_old       JSONB;
    v_new       JSONB;
BEGIN
    IF TG_OP = 'DELETE' THEN
        v_old := to_jsonb(OLD);
        v_record_id := (v_old ->> 'id')::BIGINT;
    ELSIF TG_OP = 'UPDATE' THEN
        v_old := to_jsonb(OLD);
        v_new := to_jsonb(NEW);
        v_record_id := (v_new ->> 'id')::BIGINT;
    ELSE
        v_new := to_jsonb(NEW);
        v_record_id := (v_new ->> 'id')::BIGINT;
    END IF;

    INSERT INTO kursovaya4sem.audit_log (table_name, operation, record_id, old_data, new_data, db_user)
    VALUES (TG_TABLE_NAME, TG_OP, v_record_id, v_old, v_new, current_user);

    IF TG_OP = 'DELETE' THEN
        RETURN OLD;
    END IF;
    RETURN NEW;
END;
$$;

DO $$
DECLARE
    t TEXT;
BEGIN
    FOREACH t IN ARRAY ARRAY[
        'material_card', 'supplier', 'prorab', 'building_object', 'tool',
        'material_stock', 'material_receipt', 'invoice', 'invoice_item',
        'material_return', 'tool_issue'
    ]
    LOOP
        EXECUTE format(
            'DROP TRIGGER IF EXISTS tr_audit_%I ON kursovaya4sem.%I',
            t, t
        );
        EXECUTE format(
            'CREATE TRIGGER tr_audit_%I
             AFTER INSERT OR UPDATE OR DELETE ON kursovaya4sem.%I
             FOR EACH ROW EXECUTE FUNCTION kursovaya4sem.fn_audit_trigger()',
            t, t
        );
    END LOOP;
END;
$$;
