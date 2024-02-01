package ru.practicum.shareit.businessInterface;

import ru.practicum.shareit.business.request.model.ItemRequest;
import ru.practicum.shareit.business.user.model.User;

import java.util.List;

public interface RequestDao {
    ItemRequest addRequest(ItemRequest request);

    List<ItemRequest> getAllRequestOneUser(int requesterId);

    List<ItemRequest> getRequestsAllUser(User requester, int from, int size);

    ItemRequest getRequestById(int id);
}
