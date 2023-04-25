package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.AdsCommentDTO;
import ru.skypro.homework.service.CommentService;
import ru.skypro.homework.response.ResponseWrapper;


@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/ads")
@Tag(name = "Комментарии")
public class CommentController {
    private final CommentService commentService;
    @Operation(summary = "Получить комментарии объявления", tags = "Комментарии")
    @GetMapping("/{id}/comments")
    public ResponseWrapper<AdsCommentDTO> getComments(@PathVariable("id") Integer id) {
        return ResponseWrapper.of(commentService.getComments(id));
    }
    @Operation(summary = "Добавление нового комментария к объявлению", tags = "Комментарии",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AdsCommentDTO.class))}),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
            }
    )
    @PostMapping("/{id}/comments")
    public ResponseEntity<AdsCommentDTO> addComments(@PathVariable("id") Integer id,
                 @RequestBody AdsCommentDTO adsCommentDTO,
                 Authentication authentication) throws Exception {
        return ResponseEntity.ok(commentService.addAdsComment(id,adsCommentDTO,authentication));
    }
    @Operation(summary = "Обновить комментарий", tags = "Комментарии",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AdsCommentDTO.class))}),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
            }
    )
    @PreAuthorize("@commentService.getById(#commentId).author.username == authentication.name or hasAuthority('ROLE_ADMIN')")
    @PatchMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<AdsCommentDTO> updateComments(@PathVariable("adId") Integer adId,
                                 @PathVariable("commentId") Integer commentId,
                                 @RequestBody AdsCommentDTO adsCommentDto) throws Exception {
        return ResponseEntity.ok(commentService.updateComments(adId, commentId, adsCommentDto));
    }
    @Operation(summary = "Удалить комментарий", tags = "Комментарии",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
            }
    )
    @PreAuthorize("@commentService.getById(#commentId).author.username == authentication.name or hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable("adId") Integer adId,
                             @PathVariable("commentId") Integer commentId) throws Exception {
        commentService.deleteComment(adId, commentId);
        return ResponseEntity.ok().build();
    }
}

