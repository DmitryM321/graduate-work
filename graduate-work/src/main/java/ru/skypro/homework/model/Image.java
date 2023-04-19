package ru.skypro.homework.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.awt.*;
import java.util.Arrays;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="image")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "file_size")
    private Long fileSize;
    @Column(name = "media_type")
    private String mediaType;
    @Lob

    @Column(name = "data", columnDefinition = "OID")
  //  @Type(type="org.hibernate.type.BinaryType")
    @Type(type="binary")
    private byte[] data;

}
