package ru.practicum.shareit.repositoryTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestDBRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
public class RequestRepositoryTest {
    @Autowired
    private TestEntityManager em;
    @Autowired
    private ItemRequestDBRepository requestRepository;
    Pageable pageable = PageRequest.of(0 / 10, 10);

    @Test
    void findAllByRequesterTest() {
        User user = new User(
                null,
                "John",
                "first@user.com"
        );
        em.persist(user);
        em.flush();
        ItemRequest itemRequest = new ItemRequest(
                null,
                "description",
                user,
                LocalDateTime.of(2025, 10, 10, 10, 10, 10)
        );
        em.persist(itemRequest);
        em.flush();
        Iterable<ItemRequest> requests = requestRepository.findAllByRequester(user);
        assertThat(requests).hasSize(1).contains(itemRequest);
        em.clear();
    }

    @Test
    void findAllByRequesterNotTest() {
        User user = new User(
                null,
                "John",
                "user@user.com"
        );
        em.persist(user);
        em.flush();
        ItemRequest itemRequest = new ItemRequest(
                null,
                "description",
                user,
                LocalDateTime.of(2025, 10, 10, 10, 10, 10)
        );
        em.persist(itemRequest);
        em.flush();
        User anotherUser = new User(
                null,
                "John",
                "anotherUser@user.com"
        );
        em.persist(anotherUser);
        em.flush();
        Iterable<ItemRequest> requests = requestRepository.findAllByRequesterNot(anotherUser, pageable);
        assertThat(requests).hasSize(1).contains(itemRequest);
        em.clear();
    }
}
