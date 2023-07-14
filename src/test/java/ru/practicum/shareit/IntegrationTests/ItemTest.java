package ru.practicum.shareit.IntegrationTests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoLong;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ItemTest {
    private final EntityManager em;
    private final ItemService itemService;

    @Test
    void saveItemTest() {
        User testUser = new User(
                null,
                "John",
                "first@user.com"
        );
        em.persist(testUser);
        em.flush();
        ItemDto itemDto = new ItemDto(
                null,
                "Дрель",
                "Простая дрель",
                true,
                null
        );
        itemService.saveItem(itemDto, testUser.getId());
        TypedQuery<Item> query = em.createQuery("Select i from Item i", Item.class);
        List<Item> getItems = query.getResultList();
        assertEquals(itemDto.getName(), getItems.get(0).getName());
        assertEquals(itemDto.getDescription(), getItems.get(0).getDescription());
        assertEquals(testUser, getItems.get(0).getOwner());
        em.clear();
    }

    @Test
    void getItemByIdTest() {
        User testUser = new User(
                null,
                "John",
                "first@user.com"
        );
        em.persist(testUser);
        em.flush();
        Item item = new Item(
                null,
                "Дрель",
                "Простая дрель",
                true,
                testUser,
                null
        );
        em.persist(item);
        em.flush();
        ItemDtoLong itemDtoLong = new ItemDtoLong(
                item.getId(),
                "Дрель",
                "Простая дрель",
                true,
                null,
                null,
                null,
                List.of()
        );
        ItemDtoLong getItemDtoLong = itemService.getItemById(item.getId(), testUser.getId());
        assertEquals(itemDtoLong, getItemDtoLong);
        em.clear();
    }

    @Test
    void writeCommentToItemTest() {
        User testUser = new User(
                null,
                "John",
                "first@user.com"
        );
        em.persist(testUser);
        em.flush();
        Item item = new Item(
                null,
                "Дрель",
                "Простая дрель",
                true,
                testUser,
                null
        );
        em.persist(item);
        em.flush();
        Booking booking = new Booking(
                null,
                LocalDateTime.of(2013, 10, 10, 10, 10, 10),
                LocalDateTime.of(2020, 10, 10, 10, 10, 10),
                item,
                testUser,
                BookingStatus.APPROVED
        );
        em.persist(booking);
        em.flush();
        CommentDto commentDto = new CommentDto(
                null,
                "comment",
                "authorName",
                null
        );
        CommentDto getCommentDto = itemService.writeCommentToItem(testUser.getId(), item.getId(), commentDto);
        CommentDto testCommentDto = new CommentDto(
                getCommentDto.getId(),
                "comment",
                "John",
                getCommentDto.getCreated()
        );
        assertEquals(testCommentDto, getCommentDto);
        em.clear();
    }
}
