package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.LoginReq;
import ru.skypro.homework.dto.RegisterReq;
import ru.skypro.homework.model.Role;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ObjectMapper objectMapper;

    private final LoginReq loginReq = new LoginReq();
    private final User user = new User();
    private final RegisterReq registerReq = new RegisterReq();
    @BeforeEach
    void setUp() {
        user.setUsername("authtest@mail.ru");
        user.setFirstName("authtest");
        user.setLastName("authtestauthtest");
        user.setPhone("+79297175334");
        user.setPassword(passwordEncoder.encode("password"));
        user.setRole(Role.USER);
        user.setEnabled(true);
        userRepository.save(user);
        loginReq.setUsername("authtest@mail.ru");
        loginReq.setPassword("password");
        registerReq.setUsername("2a2uthtest@mail.ru");
        registerReq.setFirstName("authtest2");
        registerReq.setLastName("authtestauthtestauthtest");
        registerReq.setPhone("+79297175344");
        registerReq.setPassword("password2");
        registerReq.setRole(Role.ADMIN);
    }
    @AfterEach
    void clearAfterTest() {
        userRepository.delete(user);
    }
    @Test
    public void testReturnsCredentialWhenLoginSucceeds() throws Exception {
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginReq)))
                .andExpect(status().isOk());
    }
    @Test
    public void testReturnsCredentialWhenRegisterSuccess() throws Exception {
        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerReq)))
                .andExpect(status().isOk());
        User savedUser = userRepository.findByUsernameIgnoreCase(registerReq.getUsername()).orElseThrow(Exception::new);
        userRepository.delete(savedUser);
    }
}