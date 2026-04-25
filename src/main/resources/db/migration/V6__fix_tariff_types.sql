UPDATE tariffs SET type = 'HOURLY' WHERE type ILIKE 'hourly';
UPDATE tariffs SET type = 'FIXED' WHERE type ILIKE 'fixed' OR type ILIKE 'subscription';
DELETE FROM tariffs WHERE type NOT IN ('HOURLY', 'FIXED', 'MINUTE');
ALTER TABLE tariffs ALTER COLUMN type SET NOT NULL;
