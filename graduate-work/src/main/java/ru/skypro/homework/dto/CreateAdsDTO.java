package ru.skypro.homework.dto;


import lombok.Data;

@Data
public class CreateAdsDTO {
    private String title;
    private String description;
    private int price;

}
