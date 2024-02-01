package ru.practicum.shareit.business.item.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor(force = true)
public class CommentDto {
    private int id;
    @NotBlank
    private String text;
    private String authorName;
    private LocalDateTime created;
}