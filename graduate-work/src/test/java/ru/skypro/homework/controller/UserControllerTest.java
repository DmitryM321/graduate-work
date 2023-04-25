package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockPart;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.service.UserDetailsManager;
import ru.skypro.homework.dto.NewPasswordDTO;
import ru.skypro.homework.model.Role;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.repository.UserRepository;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserDetailsManager userDetailsManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ImageRepository imageRepository;
    private Authentication authentication;
    private final User user = new User();
    private final MockPart imageFile
            = new MockPart("image", "image", "image".getBytes());
    private final Image image = new Image();

    @BeforeEach
    void setUp() {
        user.setUsername("sekond@mail.ru");
        user.setFirstName("Sekond");
        user.setLastName("SekondSekond");
        user.setPhone("+79297175533");
        user.setPassword(passwordEncoder.encode("password"));
        user.setRole(Role.USER);
        user.setEnabled(true);
        userRepository.save(user);

        UserDetails userDetails = userDetailsManager.loadUserByUsername(user.getUsername());
        authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(),
                userDetails.getPassword(),
                userDetails.getAuthorities());
    }
    @AfterEach
    void clearAfterTest() {
        userRepository.delete(user);
        imageRepository.delete(image);
    }
    @Test
    public void testSetPasswordReturnsOkWhenPasswordUpdated() throws Exception {
        NewPasswordDTO newPasswordDTO = new NewPasswordDTO();
        newPasswordDTO.setCurrentPassword("password");
        newPasswordDTO.setNewPassword("password2");

        mockMvc.perform(post("/users/set_password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPasswordDTO))
                        .with(authentication(authentication)))
                .andExpect(status().isOk());
    }
    @Test
    public void testGetUserReturnsUserFromDataBase() throws Exception {
        mockMvc.perform(get("/users/me")
                        .with(authentication(authentication)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(user.getUsername()));
    }
    @Test
    public void testUpdateUserReturnsUpdatedUser() throws Exception {
        String newFirstName = "Resu";
        String newLastName = "Tset";
        user.setFirstName(newFirstName);
        user.setLastName(newLastName);

        mockMvc.perform(patch("/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))
                        .with(authentication(authentication)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(newFirstName))
                .andExpect(jsonPath("$.lastName").value(newLastName));
    }
    @Test
    public void testGetUserImage() throws Exception {
        image.setData("image".getBytes());
        image.setMediaType("image/jpeg");
        imageRepository.save(image);
        user.setImage(image);
        userRepository.save(user);
        mockMvc.perform(get("/users/image/{id}", image.getId())
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .with(authentication(authentication)))
                .andExpect(status().isOk())
                .andExpect(content().bytes(image.getData()));
    }
}