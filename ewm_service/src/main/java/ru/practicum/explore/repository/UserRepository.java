package ru.practicum.explore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explore.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
