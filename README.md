# Scooter Rental System

Spring Boot-приложение управления прокатом электросамокатов (PostgreSQL + Flyway, DAO на `EntityManager`, REST API без реализации входа/JWT по текущей задаче).

## Запуск PostgreSQL

```bash
docker compose up -d
```

Порт хоста по умолчанию: **5434** (`application.properties`: `jdbc:postgresql://localhost:5434/scooter_rental`).

## Запуск приложения

```bash
mvn spring-boot:run
```

## Основные REST-маршруты

| Префикс | Описание |
|---------|----------|
| `GET/POST/PUT/DELETE /api/roles` | Роли |
| `POST /api/users` | Регистрация пользователя (`roleName`: например `USER`, `MANAGER`) |
| `GET/GET id/PUT/DELETE /api/users` | Пользователи и профиль |
| `GET/POST/PUT/DELETE /api/scooter-models` | Модели самокатов |
| `GET/POST/PUT/PATCH …/status/DELETE /api/scooters` | Самокаты |
| `GET/POST/PUT/DELETE /api/rental-points` | Иерархия точек проката (`GET /roots`, `GET /{parentId}/children`). **Широта/долгота обязательны** (WGS-84: широта −90…90, долгота −180…180) |
| `GET/POST/PUT/DELETE /api/tariffs` | Тарифы (`type`: например `HOURLY`, `SUBSCRIPTION`) |
| `POST /api/rentals/start`, `POST /api/rentals/finish` | Начало / завершение аренды |
| `GET /api/rentals/history/user/{userId}` | История аренд клиента |
| `GET /api/rentals/history/scooter/{scooterId}` | История по самокату (для администрирования) |

Spring Security включён с `permitAll()` для всех запросов (без авторизации по ролям).

## Миграции БД

- `V1__init_schema.sql` — схема
- `V2__seed_roles.sql` — начальные роли `USER`, `MANAGER`
- `V3__rental_points_coordinates_not_null.sql` — координаты точек проката NOT NULL в БД (строки с NULL получают `0` перед ограничением — при необходимости поправьте данные вручную)
