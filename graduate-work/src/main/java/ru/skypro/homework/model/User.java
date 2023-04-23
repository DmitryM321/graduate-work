package ru.skypro.homework.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    @NotEmpty
    @NotBlank
    private String firstName;
    @NotNull
    @NotEmpty
    @NotBlank
    private String lastName;
    @NotNull
    @NotEmpty
    @NotBlank
    private String phone;
    @NotNull
    @NotEmpty
    @NotBlank
    private String username;
    @NotNull
    @NotEmpty
    @NotBlank
    private String password;
    private Boolean enabled;
    @OneToOne
    @JoinColumn(name = "image_id")
    private Image image;
    @Enumerated(EnumType.STRING)
    private Role role;
    @OneToMany(mappedBy = "author")
    private List<Ads> ads;
    @OneToMany(mappedBy = "author")
    private List<Comment> comments;
}
