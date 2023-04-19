package ru.skypro.homework.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ru.skypro.homework.dto.AdsDTO;
import ru.skypro.homework.dto.CreateAdsDTO;
import ru.skypro.homework.dto.FullAdsDTO;
import ru.skypro.homework.mapper.AdsMapper;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.model.Ads;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.repository.UserRepository;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Collection;
@Transactional
@AllArgsConstructor
@Service
public class AdsService {
    private final AdsRepository adsRepository;
    private final CommentRepository commentRepository;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final ImageService imageService;
    private final AdsMapper adsMapper;
    private final CommentMapper commentMapper;

    public Collection<Ads> getAllAds() {
        return adsRepository.findAll();
    }
    public Ads addAds(CreateAdsDTO createAdsDTO, MultipartFile multipartFile, String userName) throws Exception {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new Exception("Not found user"));
        Ads ads = adsMapper.toEntity(createAdsDTO);
        ads.setAuthor(user);
        ads.setImage(imageService.uploadImage(multipartFile));
        return adsRepository.save(ads);
    }
        public FullAdsDTO getFullAds(long id) throws Exception {
            return adsMapper.toFullAdsDto(adsRepository.findById(id)
                    .orElseThrow(() -> new Exception("Not found ad")));
    }



    }