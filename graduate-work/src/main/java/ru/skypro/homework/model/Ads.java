package ru.skypro.homework.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "ads")
public class Ads {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @NotNull
    @NotEmpty
    @NotBlank
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @NotNull
    @NotEmpty
    @NotBlank
    private Integer price;
    @ManyToOne(fetch = FetchType.LAZY)
    private User author;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private Image image;
    @JsonIgnore
    @OneToMany(mappedBy = "ads", cascade = CascadeType.ALL)
    private List<Comment> comments;
}
