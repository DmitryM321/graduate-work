package ru.skypro.homework.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skypro.homework.model.Ads;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdsDTO {
    private Long author;// id автора объявления
    private String image;
    private Long pk; //     id объявления
    private Integer price;
    private String title;
    @JsonIgnore
    private String description;

}
