package ru.random.walk.chat_service.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.random.walk.chat_service.model.entity.UserEntity;
import ru.random.walk.chat_service.repository.UserRepository;
import ru.random.walk.chat_service.service.UserService;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public void add(UserEntity user) {
        userRepository.saveAndFlush(user);
    }
}
