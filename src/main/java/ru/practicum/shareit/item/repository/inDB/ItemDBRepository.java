package ru.practicum.shareit.item.repository.inDB;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Set;

public interface ItemDBRepository extends JpaRepository<Item, Long> {
    List<Item> findByOwner_Id(long userId);

    @Query(" select i from Item i " +
            "where lower(i.available) like 'true' and" +
            " (upper(i.name) like upper(concat('%', ?1, '%')) " +
            " or upper(i.description) like upper(concat('%', ?1, '%')))")
    List<Item> search(String text);

    @Modifying(clearAutomatically = true)
    @Query("update Item i set i.name = :name where i.id = :patchId")
    void updateName(@Param(value = "patchId") long patchId, @Param(value = "name") String name);

    @Modifying(clearAutomatically = true)
    @Query("update Item i set i.description = :description where i.id = :id")
    void updateDescription(@Param(value = "id") long id, @Param(value = "description") String description);

    @Modifying(clearAutomatically = true)
    @Query("update Item i set i.available = :available where i.id = :id")
    void updateAvailable(@Param(value = "id") long id, @Param(value = "available") Boolean available);
}
