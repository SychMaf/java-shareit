package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.EmailException;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class UserRepositoryImpl implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> emails = new HashSet<>();
    private Long userId = 1L;

    @Override
    public Map<Long, User> getAllUsers() {
        return users;
    }

    @Override
    public User getUserById(Long id) {
        return users.get(id);
    }

    @Override
    public User saveUser(User user) {
        if (emails.contains(user.getEmail())) {
            throw new EmailException("User already exist");
        }
        user.setId(userId);
        emails.add(user.getEmail());
        users.put(userId, user);
        userId++;
        return user;
    }

    @Override
    public User updateUser(Long patchId, Map<String, Object> updates) {
        User user = users.get(patchId);
        for (String key : updates.keySet()) {
            if (key.equalsIgnoreCase("name")) {
                user.setName(updates.get(key).toString());
            }
            if (key.equalsIgnoreCase("email")) {
                String email = updates.get(key).toString();
                if (emails.contains(email) && !user.getEmail().equals(email)) {
                    throw new EmailException("Email already exist");
                }
                emails.remove(user.getEmail());
                user.setEmail(email);
                emails.add(user.getEmail());
            }
        }
        users.put(patchId, user);
        return users.get(patchId);
    }

    @Override
    public void deleteUser(Long removeId) {
        emails.remove(users.get(removeId).getEmail());
        users.remove(removeId);
    }
}
