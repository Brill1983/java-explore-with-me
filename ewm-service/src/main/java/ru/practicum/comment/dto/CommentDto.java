package ru.practicum.comment.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CommentDto {

    @NotBlank(message = "Текст комментария обязателен для заполнения")
    @Size(min = 20, max = 2500, message = "Текст комментария должн содержать не менее 20 и не более 2500 симоволов")
    private String text;
}
