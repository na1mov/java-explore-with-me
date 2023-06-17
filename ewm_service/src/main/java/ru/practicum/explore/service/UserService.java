package ru.practicum.explore.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.explore.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto save(UserDto userDto);

    void delete(Long userId);

    List<UserDto> findAll(List<Long> usersIds, Pageable pageable);
}
