package ru.skypro.homework.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import lombok.Data;

@Data
public class FullAdsDTO {

    private String image;
    private String authorLastName;
    private String authorFirstName;
    private String phone;
    private int price;
    private String description;
    private Integer pk;
    private String title;
    private String email;
}
