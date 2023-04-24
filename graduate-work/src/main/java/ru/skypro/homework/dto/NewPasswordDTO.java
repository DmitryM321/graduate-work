package ru.skypro.homework.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NewPasswordDTO {
    private String currentPassword;
    private String newPassword;

    public NewPasswordDTO() {

    }
}
