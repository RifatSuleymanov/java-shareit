package ru.practicum.shareit.business.request.dao.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.business.exception.NotFoundException;
import ru.practicum.shareit.business.request.model.ItemRequest;
import ru.practicum.shareit.business.request.repository.RequestRepository;
import ru.practicum.shareit.businessInterface.RequestDao;
import ru.practicum.shareit.business.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class RequestDaoImpl implements RequestDao {
    private final RequestRepository requestRepository;

    @Transactional
    @Override
    public ItemRequest addRequest(ItemRequest request) {
        request.setCreated(LocalDateTime.now());
        return requestRepository.save(request);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemRequest> getAllRequestOneUser(int requesterId) {
        return requestRepository.findAllByRequesterIdOrderByCreatedAsc(requesterId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemRequest> getRequestsAllUser(User requester, int from, int size) {
        return requestRepository
                .findAllByRequesterNotLikeOrderByCreatedAsc(requester, PageRequest.of(from, size))
                .stream()
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public ItemRequest getRequestById(int id) {
        return requestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("По вашему id не был найден запрос"));
    }
}