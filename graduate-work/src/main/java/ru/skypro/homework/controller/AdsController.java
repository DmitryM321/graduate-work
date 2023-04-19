package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdsDTO;
import ru.skypro.homework.dto.CreateAdsDTO;
import ru.skypro.homework.dto.FullAdsDTO;
import ru.skypro.homework.mapper.AdsMapper;
import ru.skypro.homework.model.Ads;
import ru.skypro.homework.response.ResponseWrapper;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.ImageService;


@RestController()
@RequiredArgsConstructor
@CrossOrigin(value = "http://localhost:3000")
@RequestMapping("/ads")
public class AdsController {
    private final AdsService adsService;
    private final ImageService imageService;
    private final AdsMapper adsMapper;

    @GetMapping
    public ResponseWrapper<AdsDTO> getAllAds() {
        return ResponseWrapper.of(adsMapper.toDto(adsService.getAllAds()));
    }


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AdsDTO> addAds1(@RequestPart(value = "image", required = true) MultipartFile image,
                                         @RequestPart(value = "properties", required = true) CreateAdsDTO createAdsDto) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Ads addedAds = adsService.addAds(createAdsDto, image, authentication.getName());
        AdsDTO adsDTO = adsMapper.toDto(addedAds);
        return ResponseEntity.ok(adsDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FullAdsDTO> getFullAd(@PathVariable int id) throws Exception {
        FullAdsDTO fullAdsDTO = adsService.getFullAds(id);
        return ResponseEntity.ok(fullAdsDTO);
    }
}
