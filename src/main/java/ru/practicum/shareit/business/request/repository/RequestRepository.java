package ru.practicum.shareit.business.request.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.business.request.model.ItemRequest;
import ru.practicum.shareit.business.user.model.User;

import java.util.List;

public interface RequestRepository extends JpaRepository<ItemRequest, Integer> {
    List<ItemRequest> findAllByRequesterIdOrderByCreatedAsc(Integer requester);

    Page<ItemRequest> findAllByRequesterNotLikeOrderByCreatedAsc(User requester, Pageable pageable);
}
