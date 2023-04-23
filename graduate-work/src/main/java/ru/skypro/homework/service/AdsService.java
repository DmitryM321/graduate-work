package ru.skypro.homework.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ru.skypro.homework.dto.AdsDTO;
import ru.skypro.homework.dto.CreateAdsDTO;
import ru.skypro.homework.dto.FullAdsDTO;
import ru.skypro.homework.mapper.AdsMapper;
import ru.skypro.homework.model.Ads;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.repository.UserRepository;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class AdsService {
    private final AdsRepository adsRepository;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final ImageService imageService;
    private final AdsMapper adsMapper;

    public List<AdsDTO> getAllAds() {
        return adsRepository.findAll()
                .stream()
                .map(adsMapper::toDto)
                .collect(Collectors.toList());
    }
    public AdsDTO addAds(CreateAdsDTO createAdsDto, MultipartFile adsImage, Authentication authentication) throws Exception {
        User user = userRepository.findByUsernameIgnoreCase(authentication.getName()).orElseThrow(() -> new Exception("User no found"));
        Ads ads = adsMapper.toEntity(createAdsDto);
        ads.setAuthor(user);
        Image image = imageService.uploadImage(adsImage);
        ads.setImage(image);
        return adsMapper.toDto(adsRepository.save(ads));
    }
    public FullAdsDTO getFullAds(Integer id) throws Exception {
        return adsMapper.toFullAdsDto(adsRepository.findById(id).orElseThrow(() -> new Exception("Ad not found")));

    }
    public Ads getAdsById(Integer id) {
        return adsRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Not found"));
    }
    public void removeAdsById(Integer adsId) {
        Ads ads = getAdsById(adsId);
        adsRepository.delete(ads);
    }
    public AdsDTO updateAds(Integer id, CreateAdsDTO createAdsDto) {
        Ads ads = getAdsById(id);
        ads.setTitle(createAdsDto.getTitle());
        ads.setDescription(createAdsDto.getDescription());
        ads.setPrice(createAdsDto.getPrice());
        adsRepository.save(ads);
        return adsMapper.toDto(ads);
    }
    public List<AdsDTO> getAdsAuthorizedUsers(Authentication authentication) {
        return adsRepository.findAllByAuthor_Username(authentication.getName())
                .stream()
                .map(adsMapper::toDto)
                .collect(Collectors.toList());
    }
    public void updateAdsImage(Integer id, MultipartFile image) throws IOException {
        Ads ads = getAdsById(id);
        imageRepository.delete(ads.getImage());
        ads.setImage(imageService.uploadImage(image));
        adsRepository.save(ads);
    }
}

