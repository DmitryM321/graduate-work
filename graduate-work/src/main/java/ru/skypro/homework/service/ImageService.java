package ru.skypro.homework.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.repository.ImageRepository;

import java.io.IOException;

@Service
public class ImageService {
    private final Logger logger = LoggerFactory.getLogger(ImageService.class);
    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public Image uploadImage(MultipartFile adsImage) throws IOException {

        Image image = new Image();
        image.setData(adsImage.getBytes());
        image.setFileSize(adsImage.getSize());
        image.setMediaType(adsImage.getContentType());
        image.setData(adsImage.getBytes());
        return imageRepository.save(image);


    }}