package ru.practicum.shareit.business.request.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.business.request.dto.ItemRequestDto;
import ru.practicum.shareit.business.request.mapper.RequestMapper;
import ru.practicum.shareit.business.request.model.ItemRequest;
import ru.practicum.shareit.businessInterface.ItemDao;
import ru.practicum.shareit.business.item.mapper.ItemMapper;
import ru.practicum.shareit.business.item.model.Item;
import ru.practicum.shareit.businessInterface.RequestDao;
import ru.practicum.shareit.businessInterface.UserDao;
import ru.practicum.shareit.business.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RequestService {
    private final UserDao userDao;
    private final ItemDao itemDao;
    private final RequestDao requestDao;
    private List<ItemRequestDto> listDto;

    public ItemRequestDto addRequest(ItemRequestDto dto, int requesterId) {
        User user = userDao.getUserById(requesterId);
        ItemRequest request = RequestMapper.toRequest(dto, user);
        return RequestMapper.toRequestDto(requestDao.addRequest(request));
    }

    public List<ItemRequestDto> getAllRequestOneUser(int requesterId) {
        User user = userDao.getUserById(requesterId);
        List<ItemRequest> requests = requestDao.getAllRequestOneUser(requesterId);
        listDto = new ArrayList<>();

        return getItemRequestDtos(requests, listDto);
    }

    public List<ItemRequestDto> getRequestAllUser(int requesterId, int from, int size) {
        User user = userDao.getUserById(requesterId);
        List<ItemRequest> requests = requestDao.getRequestsAllUser(user, from, size);
        List<ItemRequestDto> listDto = new ArrayList<>();

        return getItemRequestDtos(requests, listDto);
    }

    private List<ItemRequestDto> getItemRequestDtos(List<ItemRequest> requests, List<ItemRequestDto> listDto) {
        List<Integer> requestIds = requests.stream()
                .map(ItemRequest::getId)
                .collect(Collectors.toList());

        List<Item> items = itemDao.getAllItemsByMultipleRequests(requestIds);

        for (ItemRequest request : requests) {
            ItemRequestDto dto = RequestMapper.toRequestDto(request);
            setRequestItems(dto, items);
            listDto.add(dto);
        }

        return listDto;
    }

    public ItemRequestDto getRequestById(int requestId, int requesterId) {
        User user = userDao.getUserById(requesterId);
        ItemRequestDto dto = RequestMapper.toRequestDto(requestDao.getRequestById(requestId));

        List<Item> items = itemDao.getAllItemsByOneRequest(requestId);
        setRequestItems(dto, items);

        return dto;
    }

    public void setRequestItems(ItemRequestDto itemRequestDto, List<Item> items) {
        itemRequestDto.setItems(items.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList())
        );
    }

}