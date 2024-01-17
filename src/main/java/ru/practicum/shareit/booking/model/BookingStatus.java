package ru.practicum.shareit.booking.model;

public enum BookingStatus {
    WAITING, // НОВОЕ БРОНИРОВАНИЕ, ОЖИДАЕТ ОДОБРЕНИЯ
    APPROVED, // БРОНИРОВАНИЕ ПОТВЕРЖДЕНО ВЛАДЕЛЬЦЕМ
    REJECTED, // БРОНИРОВАНИЕ ОТКЛОНЕНО ВЛАДЕЛЬЦЕМ
    CANCELED // БРОНИРОВАНИЕ ОТМЕНЕНО СОЗДАТЕЛЕМ
}