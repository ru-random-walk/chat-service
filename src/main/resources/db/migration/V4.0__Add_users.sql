-- Создание таблицы user (если не существует)
create table if not exists chat.user
(
    id        uuid default gen_random_uuid() primary key,
    full_name varchar
);