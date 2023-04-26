package ru.skypro.homework.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AdsDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer author;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String image;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer pk;
    private Integer price;
    private String title;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String description;
}