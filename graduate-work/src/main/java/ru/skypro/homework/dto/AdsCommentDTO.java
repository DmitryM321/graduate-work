package ru.skypro.homework.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdsCommentDTO {
    private Long pk;
    private Long author;
    private String createdAt;
    private String text;

}
