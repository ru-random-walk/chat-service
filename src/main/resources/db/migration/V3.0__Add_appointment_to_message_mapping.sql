create table if not exists chat.appointments
(
    appointment_id  uuid not null unique,
    message_id      uuid not null unique,
    foreign key (message_id) references chat.message (id)
);