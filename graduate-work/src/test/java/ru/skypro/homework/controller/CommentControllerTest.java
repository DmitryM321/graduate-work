package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.config.security.UserDetailsManager;
import ru.skypro.homework.dto.AdsCommentDTO;
import ru.skypro.homework.model.Role;
import ru.skypro.homework.model.Ads;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;

import java.time.Instant;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserDetailsManager userDetailsManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdsRepository adsRepository;
    @Autowired
    private CommentRepository commentRepository;

    private Authentication authentication;
    private User user = new User();
    private Ads ads = new Ads();
    private Comment comment = new Comment();
    private AdsCommentDTO commentDto = new AdsCommentDTO();
    @BeforeEach
    void setUp() {
        user.setUsername("first@mail.ru");
        user.setFirstName("First");
        user.setLastName("First");
        user.setPhone("+79297175511");
        user.setPassword(passwordEncoder.encode("password"));
        user.setRole(Role.USER);
        user.setEnabled(true);
        userRepository.save(user);
        UserDetails userDetails = userDetailsManager.loadUserByUsername(user.getUsername());
        authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(),
                userDetails.getPassword(),
                userDetails.getAuthorities());
        ads.setTitle("product");
        ads.setDescription("description");
        ads.setPrice(111);
        ads.setAuthor(user);
        adsRepository.save(ads);
        comment.setText("text");
        comment.setAds(ads);
        comment.setCreatedAt(Instant.now());
        comment.setAuthor(user);
        commentRepository.save(comment);
        commentDto.setText("text2");
    }
    @AfterEach
    void clearAfterTest() {
        commentRepository.delete(comment);
        adsRepository.delete(ads);
        userRepository.delete(user);
    }
    @Test
    public void testReturnsListComments() throws Exception {
        mockMvc.perform(get("/ads/{id}/comments", ads.getId())
                        .with(authentication(authentication)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.count").isNumber())
                .andExpect(jsonPath("$.results").isArray())
                .andExpect(jsonPath("$.results[0].text").value(comment.getText()));
    }
    @Test
    public void testAddCommentAndReturnAddedComment() throws Exception {
        mockMvc.perform(post("/ads/{id}/comments", ads.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDto))
                        .with(authentication(authentication)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pk").isNumber())
                .andExpect(jsonPath("$.text").value(commentDto.getText()))
                .andExpect(jsonPath("$.authorFirstName").value(user.getFirstName()));
    }
    @Test
    public void testDeleteAdsComment() throws Exception {
        mockMvc.perform(delete("/ads/{adId}/comments/{commentId}", ads.getId(), comment.getId())
                        .with(authentication(authentication)))
                .andExpect(status().isOk());
    }
    @Test
    public void testUpdateComments() throws Exception {
        String newText = "New Text";
        comment.setText(newText);
        commentRepository.save(comment);
        mockMvc.perform(patch("/ads/{adId}/comments/{commentId}", ads.getId(), comment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comment))
                        .with((authentication(authentication))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value(comment.getText()));
    }
}