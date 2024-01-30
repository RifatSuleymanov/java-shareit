package ru.practicum.shareit.request.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.request.dao.RequestDao;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RequestService {
    private final UserDao userDao;
    private final ItemDao itemDao;
    private final RequestDao requestDao;

    public ItemRequestDto addRequest(ItemRequestDto dto, int requesterId) {
        User user = userDao.getUserById(requesterId);
        ItemRequest request = RequestMapper.toRequest(dto, user);
        return RequestMapper.toRequestDto(requestDao.addRequest(request));
    }

    public List<ItemRequestDto> getAllRequestOneUser(int requesterId) {
        User user = userDao.getUserById(requesterId);
        List<ItemRequest> requests = requestDao.getAllRequestOneUser(requesterId);
        List<ItemRequestDto> listDto = new ArrayList<>();
        for (ItemRequest request : requests) {
            ItemRequestDto dto = RequestMapper.toRequestDto(request);
            setRequestItems(dto);
            listDto.add(dto);
        }
        return listDto;
    }

    public List<ItemRequestDto> getRequestAllUser(int requesterId, int from, int size) {
        User user = userDao.getUserById(requesterId);
        List<ItemRequest> requests = requestDao.getRequestsAllUser(user, from, size);
        List<ItemRequestDto> listDto = new ArrayList<>();
        for (ItemRequest request : requests) {
            ItemRequestDto dto = RequestMapper.toRequestDto(request);
            setRequestItems(dto);
            listDto.add(dto);
        }
        return listDto;
    }

    public ItemRequestDto getRequestById(int requestId, int requesterId) {
        User user = userDao.getUserById(requesterId);
        ItemRequestDto dto = RequestMapper.toRequestDto(requestDao.getRequestById(requestId));
        setRequestItems(dto);
        return dto;
    }

    public void setRequestItems(ItemRequestDto itemRequestDto) {
        itemRequestDto.setItems(itemDao.getAllItemsByOneRequest(itemRequestDto.getId())
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList())
        );
    }
}
