package ru.skypro.homework.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skypro.homework.dto.Role;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue()
    private Long id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    private Role role;
  //  private String email;


}
