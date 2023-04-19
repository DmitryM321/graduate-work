package ru.skypro.homework.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FullAdsDTO  {
    private Long pk;
    private String image;
    private String firstName;
    private String lastName;
    private String phone;
    private Integer price;
    private String title;
    private String description;
    private String userName;
}
