package ru.practicum.shareit.business.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.business.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    List<Item> findAllByRequestId(int requestId);

    Page<Item> findAllByOwnerId(Integer ownerId, Pageable pageable);
}