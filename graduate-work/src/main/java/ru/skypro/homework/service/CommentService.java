package ru.skypro.homework.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.AdsCommentDTO;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;
    private final AdsRepository adsRepository;

    public AdsCommentDTO addAdsComment(Integer id, AdsCommentDTO adsCommentDTO, Authentication authentication) throws Exception {
        User user = userRepository.findByUsernameIgnoreCase(authentication.getName())
                .orElseThrow(() -> new Exception("User not found"));
        Comment comment = commentMapper.toEntity(adsCommentDTO);
        comment.setAuthor(user);
        comment.setAds(adsRepository.findById(id).
                orElseThrow(() -> new Exception("Ads not found")));
        comment.setCreatedAt(Instant.now());
        commentRepository.save(comment);
        return commentMapper.toDto(comment);
    }
    public List<AdsCommentDTO> getComments(Integer id) {
        return commentRepository.findAllByAdsId(id)
                .stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList());
    }
    public void deleteComment(Integer adId, Integer commentId) throws Exception {
        Comment comment = getComment( commentId,adId);
        commentRepository.delete(comment);
    }
    public AdsCommentDTO updateComments( Integer adId,Integer commentId, AdsCommentDTO adsCommentDTO) throws Exception {
        Comment comment = getComment(commentId,adId);
        comment.setText(adsCommentDTO.getText());
        commentRepository.save(comment);
        return commentMapper.toDto(comment);
    }
    public Comment getComment(Integer commentId, Integer adId) throws Exception {
        return commentRepository.findByIdAndAdsId(commentId, adId).orElseThrow(Exception::new);
    }
}

