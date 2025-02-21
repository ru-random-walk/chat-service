-- создание типа 'chat_type', если он не существует
do
$$
    begin
        perform 1
        from pg_type
        where typname = 'chat_type';
        if not found then
            create type chat.chat_type as enum ('PRIVATE', 'GROUP');
        end if;
    end
$$;

-- Автоматический каст строки в тип chat_type
DO
$$
    BEGIN
        IF NOT EXISTS (SELECT 1
                       FROM pg_cast
                       WHERE castsource = 'character varying'::regtype
                         AND casttarget = 'chat.chat_type'::regtype) THEN
            CREATE CAST (character varying AS chat.chat_type) WITH INOUT AS IMPLICIT;
        END IF;
    END
$$;

-- создание таблицы 'chat', если она не существует
create table if not exists chat.chat
(
    id   uuid default gen_random_uuid() primary key,
    type chat.chat_type not null
);

-- создание таблицы 'chat_members', если она не существует
create table if not exists chat.chat_members
(
    chat_id uuid not null,
    user_id uuid not null,
    foreign key (chat_id) references chat.chat (id),
    unique (chat_id, user_id)
);

-- создание таблицы 'message', если она не существует
create table if not exists chat.message
(
    id             uuid      default gen_random_uuid() primary key,
    payload        jsonb not null,
    chat_id        uuid  not null,
    sender         uuid  not null,
    recipient      uuid  not null,
    marked_as_read boolean   default false,
    sent_at        timestamp default now(),
    foreign key (chat_id) references chat.chat (id)
);