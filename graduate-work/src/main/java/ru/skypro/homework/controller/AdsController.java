package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdsDTO;
import ru.skypro.homework.dto.CreateAdsDTO;
import ru.skypro.homework.dto.FullAdsDTO;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.response.ResponseWrapper;

import javax.validation.Valid;
import java.io.IOException;


@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/ads")
@RequiredArgsConstructor
@Tag(name = "Объявления")
public class AdsController {
    private final AdsService adsService;
    private final ImageService imageService;

    @Operation(summary = "Получить все объявления", tags = "Объявления",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "OK",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseWrapper.class))})
            }
    )
    @GetMapping
    public ResponseWrapper<AdsDTO> getAllAds() {
        return ResponseWrapper.of(adsService.getAllAds());
    }
    @Operation(summary = "Добавить объявление", tags = "Объявления",
            responses = {
                    @ApiResponse(
                            responseCode = "201", description = "Created",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AdsDTO.class))}),
                    @ApiResponse(responseCode = "401", description = "Unauthorised", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
            }
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AdsDTO> addAds(@RequestPart("image") MultipartFile imageFile,
                                         @Valid
                                         @RequestPart("properties") CreateAdsDTO createAds,
                                         Authentication authentication) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(adsService.addAds(createAds, imageFile, authentication));
    }
    @Operation(summary = "Получить информацию об объявлении", tags = "Объявления",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "OK",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = FullAdsDTO.class))}),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<FullAdsDTO> getFullAdsInfo(@PathVariable Integer id) throws Exception {
        FullAdsDTO fullAdsDto = adsService.getFullAds(id);
        return ResponseEntity.ok(fullAdsDto);
    }
    @Operation(summary = "Удалить объявление", tags = "Объявления",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content),
                    @ApiResponse(responseCode = "204", description = "No Content", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Unauthorised", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
            }
    )
    @PreAuthorize("@adsService.getFullAds(#id).getEmail() == authentication.name or hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAds(@PathVariable("id") Integer id) {
        adsService.removeAdsById(id);
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "Обновить информацию об объявлении",tags = "Объявления",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "OK",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AdsDTO.class))}),
                    @ApiResponse(responseCode = "401", description = "Unauthorised", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
            }
    )
    @PreAuthorize("@adsService.getFullAds(#id).getEmail() == authentication.name or hasAuthority('ROLE_ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<AdsDTO> updateAds(@PathVariable("id") Integer id,
                                            @RequestBody CreateAdsDTO createAds) {
        return ResponseEntity.ok(adsService.updateAds(id, createAds));
    }
    @Operation(summary = "Получить объявления авторизованного пользователя", tags = "Объявления",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "OK",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseWrapper.class))}),
                    @ApiResponse(responseCode = "401", description = "Unauthorised", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
            }
    )
    @GetMapping("/me")
    public ResponseEntity<?> getAdsAuthorizeUser(Authentication authentication) {
        return ResponseEntity.ok(ResponseWrapper.of(adsService.getAdsAuthorizedUsers(authentication)));
    }
    @Operation(summary = "Обновить картинку объявления",tags = "Объявления",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "OK",
                            content = { @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserDTO.class))}),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
            }
    )
    @PreAuthorize("@adsService.getFullAds(#id).getEmail()== authentication.name or hasAuthority('ROLE_ADMIN')")
    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateImage(@PathVariable("id") Integer id,
                           @RequestPart("image") MultipartFile imageFile) throws IOException {
        adsService.updateAdsImage(id, imageFile);
        return ResponseEntity.ok().build();
    }

    @Operation(hidden = true)
    @GetMapping(value = "/image/{id}")
    public ResponseEntity<byte[]> getAdsImage(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(imageService.getImageById(id).getData());
    }
}
