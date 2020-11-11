-- upgrade from RMB 24 to RMB 29

-- load uuid functions

DO $uuidftl$
BEGIN
<#include "uuid.ftl">
EXCEPTION WHEN OTHERS THEN NULL;
END $uuidftl$;

-- drop RMB functions that no longer exists in RMB 29, use CASCADE to drop triggers that use them

DO $$ BEGIN DROP FUNCTION count_estimate_smart CASCADE; EXCEPTION WHEN OTHERS THEN END; $$;
DO $$ BEGIN DROP FUNCTION count_estimate_smart_depricated CASCADE; EXCEPTION WHEN OTHERS THEN END; $$;
DO $$ BEGIN DROP FUNCTION set_id_injson_actual_opening_hours CASCADE; EXCEPTION WHEN OTHERS THEN END; $$;
DO $$ BEGIN DROP FUNCTION set_id_injson_exceptional_hours CASCADE; EXCEPTION WHEN OTHERS THEN END; $$;
DO $$ BEGIN DROP FUNCTION set_id_injson_regular_hours CASCADE; EXCEPTION WHEN OTHERS THEN END; $$;

-- delete data from audit_* tables because we don't have code to convert it into the new format

DO $$ BEGIN TRUNCATE audit_openings, audit_regular_hours, audit_exceptions, audit_exceptional_hours, audit_actual_opening_hours; EXCEPTION WHEN OTHERS THEN END; $$;

-- drop columns in audit_* tables

DO $$ BEGIN ALTER TABLE audit_openings DROP COLUMN created_date; EXCEPTION WHEN OTHERS THEN END; $$;
DO $$ BEGIN ALTER TABLE audit_openings DROP COLUMN operation; EXCEPTION WHEN OTHERS THEN END; $$;
DO $$ BEGIN ALTER TABLE audit_openings DROP COLUMN orig_id; EXCEPTION WHEN OTHERS THEN END; $$;

DO $$ BEGIN ALTER TABLE audit_regular_hours DROP COLUMN created_date; EXCEPTION WHEN OTHERS THEN END; $$;
DO $$ BEGIN ALTER TABLE audit_regular_hours DROP COLUMN operation; EXCEPTION WHEN OTHERS THEN END; $$;
DO $$ BEGIN ALTER TABLE audit_regular_hours DROP COLUMN orig_id; EXCEPTION WHEN OTHERS THEN END; $$;

DO $$ BEGIN ALTER TABLE audit_exceptions DROP COLUMN created_date; EXCEPTION WHEN OTHERS THEN END; $$;
DO $$ BEGIN ALTER TABLE audit_exceptions DROP COLUMN operation; EXCEPTION WHEN OTHERS THEN END; $$;
DO $$ BEGIN ALTER TABLE audit_exceptions DROP COLUMN orig_id; EXCEPTION WHEN OTHERS THEN END; $$;

DO $$ BEGIN ALTER TABLE audit_exceptional_hours DROP COLUMN created_date; EXCEPTION WHEN OTHERS THEN END; $$;
DO $$ BEGIN ALTER TABLE audit_exceptional_hours DROP COLUMN operation; EXCEPTION WHEN OTHERS THEN END; $$;
DO $$ BEGIN ALTER TABLE audit_exceptional_hours DROP COLUMN orig_id; EXCEPTION WHEN OTHERS THEN END; $$;

DO $$ BEGIN ALTER TABLE audit_actual_opening_hours DROP COLUMN created_date; EXCEPTION WHEN OTHERS THEN END; $$;
DO $$ BEGIN ALTER TABLE audit_actual_opening_hours DROP COLUMN operation; EXCEPTION WHEN OTHERS THEN END; $$;
DO $$ BEGIN ALTER TABLE audit_actual_opening_hours DROP COLUMN orig_id; EXCEPTION WHEN OTHERS THEN END; $$;

-- rename column _id to id

DO $$ BEGIN ALTER TABLE openings RENAME COLUMN _id TO id; EXCEPTION WHEN OTHERS THEN END; $$;
DO $$ BEGIN ALTER TABLE audit_openings RENAME COLUMN _id TO id; EXCEPTION WHEN OTHERS THEN END; $$;
DO $$ BEGIN ALTER TABLE regular_hours RENAME COLUMN _id TO id; EXCEPTION WHEN OTHERS THEN END; $$;
DO $$ BEGIN ALTER TABLE audit_regular_hours RENAME COLUMN _id TO id; EXCEPTION WHEN OTHERS THEN END; $$;
DO $$ BEGIN ALTER TABLE exceptions RENAME COLUMN _id TO id; EXCEPTION WHEN OTHERS THEN END; $$;
DO $$ BEGIN ALTER TABLE audit_exceptions RENAME COLUMN _id TO id; EXCEPTION WHEN OTHERS THEN END; $$;
DO $$ BEGIN ALTER TABLE exceptional_hours RENAME COLUMN _id TO id; EXCEPTION WHEN OTHERS THEN END; $$;
DO $$ BEGIN ALTER TABLE audit_exceptional_hours RENAME COLUMN _id TO id; EXCEPTION WHEN OTHERS THEN END; $$;
DO $$ BEGIN ALTER TABLE actual_opening_hours RENAME COLUMN _id TO id; EXCEPTION WHEN OTHERS THEN END; $$;
DO $$ BEGIN ALTER TABLE audit_actual_opening_hours RENAME COLUMN _id TO id; EXCEPTION WHEN OTHERS THEN END; $$;

-- drop default for id

DO $$ BEGIN ALTER TABLE actual_opening_hours ALTER COLUMN id DROP DEFAULT; EXCEPTION WHEN OTHERS THEN END; $$;
DO $$ BEGIN ALTER TABLE exceptional_hours ALTER COLUMN id DROP DEFAULT; EXCEPTION WHEN OTHERS THEN END; $$;
DO $$ BEGIN ALTER TABLE openings ALTER COLUMN id DROP DEFAULT; EXCEPTION WHEN OTHERS THEN END; $$;
DO $$ BEGIN ALTER TABLE regular_hours ALTER COLUMN id DROP DEFAULT; EXCEPTION WHEN OTHERS THEN END; $$;
DO $$ BEGIN ALTER TABLE exceptions ALTER COLUMN id DROP DEFAULT; EXCEPTION WHEN OTHERS THEN END; $$;

-- set NOT NULL constraint for jsonb

DO $$ BEGIN ALTER TABLE actual_opening_hours ALTER COLUMN jsonb SET NOT NULL; EXCEPTION WHEN OTHERS THEN END; $$;
DO $$ BEGIN ALTER TABLE exceptional_hours ALTER COLUMN jsonb SET NOT NULL; EXCEPTION WHEN OTHERS THEN END; $$;
DO $$ BEGIN ALTER TABLE openings ALTER COLUMN jsonb SET NOT NULL; EXCEPTION WHEN OTHERS THEN END; $$;
DO $$ BEGIN ALTER TABLE regular_hours ALTER COLUMN jsonb SET NOT NULL; EXCEPTION WHEN OTHERS THEN END; $$;
DO $$ BEGIN ALTER TABLE exceptions ALTER COLUMN jsonb SET NOT NULL; EXCEPTION WHEN OTHERS THEN END; $$;

DO $$ BEGIN ALTER TABLE audit_actual_opening_hours ALTER COLUMN jsonb SET NOT NULL; EXCEPTION WHEN OTHERS THEN END; $$;
DO $$ BEGIN ALTER TABLE audit_exceptional_hours ALTER COLUMN jsonb SET NOT NULL; EXCEPTION WHEN OTHERS THEN END; $$;
DO $$ BEGIN ALTER TABLE audit_openings ALTER COLUMN jsonb SET NOT NULL; EXCEPTION WHEN OTHERS THEN END; $$;
DO $$ BEGIN ALTER TABLE audit_regular_hours ALTER COLUMN jsonb SET NOT NULL; EXCEPTION WHEN OTHERS THEN END; $$;
DO $$ BEGIN ALTER TABLE audit_exceptions ALTER COLUMN jsonb SET NOT NULL; EXCEPTION WHEN OTHERS THEN END; $$;

-- convert creation_date type from timestamp with time zone to timestamp without time zone

DO $$ BEGIN ALTER TABLE actual_opening_hours ALTER COLUMN creation_date TYPE TIMESTAMP WITHOUT TIME ZONE; EXCEPTION WHEN OTHERS THEN END; $$;
DO $$ BEGIN ALTER TABLE exceptional_hours ALTER COLUMN creation_date TYPE TIMESTAMP WITHOUT TIME ZONE; EXCEPTION WHEN OTHERS THEN END; $$;
DO $$ BEGIN ALTER TABLE openings ALTER COLUMN creation_date TYPE TIMESTAMP WITHOUT TIME ZONE; EXCEPTION WHEN OTHERS THEN END; $$;
DO $$ BEGIN ALTER TABLE regular_hours ALTER COLUMN creation_date TYPE TIMESTAMP WITHOUT TIME ZONE; EXCEPTION WHEN OTHERS THEN END; $$;
DO $$ BEGIN ALTER TABLE exceptions ALTER COLUMN creation_date TYPE TIMESTAMP WITHOUT TIME ZONE; EXCEPTION WHEN OTHERS THEN END; $$;