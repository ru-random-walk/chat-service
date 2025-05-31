package ru.random.walk.chat_service.service;

import ru.random.walk.chat_service.model.entity.MessageEntity;

public interface NotificationSender {
    void notifyAboutNewMessage(MessageEntity message);
}
