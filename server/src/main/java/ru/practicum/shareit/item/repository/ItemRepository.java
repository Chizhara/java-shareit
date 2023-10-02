package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findAllByOwnerIdOrderById(long ownerId, Pageable pageable);

    Optional<Item> findItemByIdAndOwnerId(long itemId, long ownerId);

    @Query("select it " +
            "from Item as it " +
            "where (upper(it.name) like upper(concat('%', ?1, '%')) " +
            "or upper(it.description) like upper(concat('%', ?1, '%'))) " +
            "and it.available = true")
    List<Item> findItemsByText(String text, Pageable pageable);
}
