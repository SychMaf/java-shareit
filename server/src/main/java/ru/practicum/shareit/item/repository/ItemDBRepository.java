package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemDBRepository extends JpaRepository<Item, Long> {
    Page<Item> findByOwner_Id(long userId, Pageable pageable);

    List<Item> findAllByRequest(Long requestId);

    @Query(" select i from Item i " +
            "where lower(i.available) like 'true' and" +
            " (upper(i.name) like upper(concat('%', ?1, '%')) " +
            " or upper(i.description) like upper(concat('%', ?1, '%')))")
    Page<Item> search(String text, Pageable pageable);
}
