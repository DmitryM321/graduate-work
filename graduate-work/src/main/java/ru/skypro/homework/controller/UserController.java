package ru.skypro.homework.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import liquibase.repackaged.org.apache.commons.lang3.tuple.Pair;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPasswordDTO;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.UserService;


@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Tag(name = "Пользователи")
public class UserController {
    private final UserService userService;
    private final ImageService imageService;

    @Operation(summary = "Получить информацию об авторизованном пользователе", tags = "Пользователи",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "OK",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserDTO.class))}),
                    @ApiResponse(responseCode = "401", description = "Unauthorised", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
            }
    )
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getUsers(Authentication authentication) {
        return ResponseEntity.ok(userService.getAuthorizedUsers(authentication));
    }
    @Operation(summary = "Обновление пароля", tags = "Пользователи",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "OK",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = NewPasswordDTO.class))}),
                    @ApiResponse(responseCode = "401", description = "Unauthorised", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
            }
    )
    @PostMapping("/set_password")
    public ResponseEntity<?> changePassword(@RequestBody NewPasswordDTO newPasswordDTO,
                                               Authentication authentication) throws Exception {
        userService.updatePassword(newPasswordDTO, authentication);
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "Обновить информацию об авторизованном пользователе", tags = "Пользователи",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "OK",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserDTO.class))}),
                    @ApiResponse(responseCode = "204", description = "No Content", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Unauthorised", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
            }
    )
    @PatchMapping("/me")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDto,
                                              Authentication authentication) throws Exception {
        return ResponseEntity.ok(userService.updateUser(userDto, authentication));
    }
    @Operation(
            summary = "Обновить аватар авторизованного пользователя", tags = "Пользователи",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
            }
    )
    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateUserImage(@RequestPart("image") MultipartFile avatarFile,
                                             Authentication authentication) throws Exception {
        userService.updateUserImage(avatarFile, authentication);
        return ResponseEntity.ok().build();
    }
    @Operation(hidden = true)
    @GetMapping(value = "/image/{id}", produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> getImage(@PathVariable("id") Integer id) {
        Pair<String, byte[]> pair = imageService.getImage(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(pair.getLeft()))
                .contentLength(pair.getRight().length)
                .body(pair.getRight());
    }
}
