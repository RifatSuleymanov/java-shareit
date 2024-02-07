package ru.practicum.shareit.request.model;

import lombok.Data;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */

@Entity
@Data
@Table(name = "requests")
public class ItemRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    public int id;

    @Column(length = 1000, nullable = false)
    private String description;

    @Column(nullable = false)
    public LocalDateTime created;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "requester_id")
    public User requester;
}