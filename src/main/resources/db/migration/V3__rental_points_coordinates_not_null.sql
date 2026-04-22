-- Обязательные координаты для точек проката (ранее колонки допускали NULL)
UPDATE rental_points SET latitude = 0 WHERE latitude IS NULL;
UPDATE rental_points SET longitude = 0 WHERE longitude IS NULL;

ALTER TABLE rental_points ALTER COLUMN latitude SET NOT NULL;
ALTER TABLE rental_points ALTER COLUMN longitude SET NOT NULL;
