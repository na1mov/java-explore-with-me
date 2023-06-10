package ru.practicum.explore.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.dto.UserDto;
import ru.practicum.explore.exception.NotFoundException;
import ru.practicum.explore.mapper.UserMapper;
import ru.practicum.explore.model.User;
import ru.practicum.explore.repository.UserRepository;
import ru.practicum.explore.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto save(UserDto userDto) {
        log.info(String.format("Сохранение пользователя с email:%s", userDto.getEmail()));

        return userMapper.userToUserDto(userRepository.save(userMapper.userDtoToUser(userDto)));
    }

    @Override
    @Transactional
    public void delete(Long userId) {
        log.info(String.format("Удаление пользователя с ID:%d", userId));

        User user = findById(userId);
        userRepository.deleteById(user.getId());
    }

    @Override
    public List<UserDto> findAll(List<Long> usersIds, Pageable pageable) {
        log.info(String.format("Поиск всех пользователей по ID из списка: %s", usersIds));

        if (usersIds == null || usersIds.isEmpty()) {
            return userRepository.findAll(pageable).stream()
                    .map(userMapper::userToUserDto)
                    .collect(Collectors.toList());
        } else {
            return userRepository.findAllById(usersIds).stream()
                    .map(userMapper::userToUserDto)
                    .collect(Collectors.toList());
        }
    }

    private User findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format("Пользотеля с ID:%d нет в базе", userId)));
    }
}
