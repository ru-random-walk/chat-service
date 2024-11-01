-- Создание схемы 'chat', если она не существует
CREATE SCHEMA IF NOT EXISTS chat;

-- Создание типа 'chat_type', если он не существует
DO
$$
    BEGIN
        PERFORM 1
        FROM pg_type
        WHERE typname = 'chat_type';
        IF NOT FOUND THEN
            CREATE TYPE chat.chat_type AS ENUM ('private', 'group');
        END IF;
    END
$$;

-- Создание типа 'message_type', если он не существует
DO
$$
    BEGIN
        PERFORM 1
        FROM pg_type
        WHERE typname = 'message_type';
        IF NOT FOUND THEN
            CREATE TYPE chat.message_type AS ENUM ('text', 'request_for_walk');
        END IF;
    END
$$;

-- Создание таблицы 'chat', если она не существует
CREATE TABLE IF NOT EXISTS chat.chat
(
    id   uuid DEFAULT gen_random_uuid() PRIMARY KEY,
    type chat.chat_type NOT NULL
);

-- Создание таблицы 'chat_members', если она не существует
CREATE TABLE IF NOT EXISTS chat.chat_members
(
    chat_id uuid NOT NULL,
    user_id uuid NOT NULL,
    FOREIGN KEY (chat_id) REFERENCES chat.chat (id)
);

-- Создание таблицы 'message', если она не существует
CREATE TABLE IF NOT EXISTS chat.message
(
    id             uuid    DEFAULT gen_random_uuid() PRIMARY KEY,
    payload        jsonb                 NOT NULL,
    type           chat.message_type     NOT NULL,
    chat_id        uuid                  NOT NULL,
    marked_as_read boolean DEFAULT false NOT NULL,
    sent_at        timestamp             NOT NULL,
    FOREIGN KEY (chat_id) REFERENCES chat.chat (id)
);