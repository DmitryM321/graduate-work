package ru.skypro.homework.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdsDTO;
import ru.skypro.homework.dto.CreateAdsDTO;
import ru.skypro.homework.dto.FullAdsDTO;
import ru.skypro.homework.mapper.AdsMapper;
import ru.skypro.homework.model.Ads;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdsServiceTest {
    @Mock
    private AdsRepository adsRepository;
    @Mock
    private AdsMapper adsMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ImageService imageService;
    @InjectMocks
    private AdsService adsService;
    @Test
    public void testGetAllAds() {
        List<Ads> adsList = new ArrayList<>();
        Ads ads = new Ads();
        adsList.add(ads);
        when(adsRepository.findAll()).thenReturn(adsList);
        AdsDTO adsDto = new AdsDTO();
        when(adsMapper.toDto(ads)).thenReturn(adsDto);
        List<AdsDTO> result = adsService.getAllAds();
        assertEquals(1, result.size());
        assertEquals(adsDto, result.get(0));
    }
    @Test
    public void testAddAds() throws Exception {
        User user = new User();
        CreateAdsDTO createAdsDto = new CreateAdsDTO();
        MultipartFile adsImage = mock(MultipartFile.class);
        Authentication authentication = mock(Authentication.class);
        when(userRepository.findByUsernameIgnoreCase(authentication.getName())).thenReturn(Optional.of(user));
        Ads ads = new Ads();
        when(adsMapper.toEntity(createAdsDto)).thenReturn(ads);
        Image image = new Image();
        when(imageService.uploadImage(adsImage)).thenReturn(image);
        AdsDTO adsDto = new AdsDTO();
        when(adsMapper.toDto(ads)).thenReturn(adsDto);
        when(adsRepository.save(ads)).thenReturn(ads);
        AdsDTO result = adsService.addAds(createAdsDto, adsImage, authentication);
        assertEquals(adsDto, result);
        assertEquals(user, ads.getAuthor());
        assertEquals(image, ads.getImage());
    }
    @Test
    public void testGetFullAds() throws Exception {
        Ads ads = new Ads();
        Integer adId = 1;
        when(adsRepository.findById(adId)).thenReturn(Optional.of(ads));
        FullAdsDTO fullAdsDto = new FullAdsDTO();
        when(adsMapper.toFullAdsDto(ads)).thenReturn(fullAdsDto);
        FullAdsDTO result = adsService.getFullAds(adId);
        assertEquals(fullAdsDto, result);
    }
    @Test
    public void testGetAdsById() {
        Integer id = 1;
        Ads ads = new Ads();
        Mockito.when(adsRepository.findById(id)).thenReturn(Optional.of(ads));
        Ads result = adsService.getAdsById(id);
        assertEquals(ads, result);
    }
}
