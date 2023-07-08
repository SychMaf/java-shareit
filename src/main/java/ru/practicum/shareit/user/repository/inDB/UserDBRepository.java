package ru.practicum.shareit.user.repository.inDB;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.model.User;

public interface UserDBRepository extends JpaRepository<User, Long> {
}
