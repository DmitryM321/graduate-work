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
import ru.skypro.homework.dto.CreateAdsDTO;
import ru.skypro.homework.model.Role;
import ru.skypro.homework.model.Ads;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AdsControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDetailsManager userDetailsManager;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private AdsRepository adsRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PasswordEncoder encoder;

    private Authentication authentication;
    private final MockPart imageFile = new MockPart("image", "image", "image".getBytes());
    private final CreateAdsDTO createAdsDTO = new CreateAdsDTO();
    private final Ads ads = new Ads();
    private final User user = new User();
    private final Image image = new Image();

    @BeforeEach
    void setUp() {
        user.setFirstName("adstest");
        user.setLastName("adstestadstest");
        user.setPhone("+79297175311");
        user.setUsername("adstest@mail.ru");
        user.setPassword(encoder.encode("password"));
        user.setRole(Role.USER);
        userRepository.save(user);
        UserDetails userDetails = userDetailsManager.loadUserByUsername(user.getUsername());
        authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(),
                userDetails.getPassword(),
                userDetails.getAuthorities());
        createAdsDTO.setTitle("product");
        createAdsDTO.setDescription("description");
        createAdsDTO.setPrice(111);

        ads.setTitle("product");
        ads.setDescription("description");
        ads.setPrice(111);
        ads.setAuthor(user);
        adsRepository.save(ads);
    }

    @AfterEach
    void clearAfterTest() {
        userRepository.delete(user);
    }

    @Test
    public void testGetAllCorrectAds() throws Exception {
        mockMvc.perform(get("/ads"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.count").isNumber())
                .andExpect(jsonPath("$.results").isArray());
    }
    @Test
    public void testAddAdsCreatesNewAdWithCorrectDetailsInDatabase() throws Exception {
        MockPart created = new MockPart("properties", objectMapper.writeValueAsBytes(createAdsDTO));
        System.out.println(objectMapper.writeValueAsString(createAdsDTO)); // Отладочный вывод
        mockMvc.perform(multipart("/ads")
                        .part(imageFile)
                        .part(created)
                        .with(authentication(authentication)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.pk").isNotEmpty())
                .andExpect(jsonPath("$.pk").isNumber())
                .andExpect(jsonPath("$.title").value(createAdsDTO.getTitle()))
                .andExpect(jsonPath("$.description").value(createAdsDTO.getDescription()))
                .andExpect(jsonPath("$.price").value(createAdsDTO.getPrice()))
                .andDo(print());
    }
    @Test
    public void testGetAdsByIdCorrectAdsInfo() throws Exception {
        mockMvc.perform(get("/ads/{id}", ads.getId())
                        .with(authentication(authentication)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pk").value(ads.getId()))
                .andExpect(jsonPath("$.title").value(ads.getTitle()))
                .andExpect(jsonPath("$.description").value(ads.getDescription()))
                .andExpect(jsonPath("$.price").value(ads.getPrice()))
                .andExpect(jsonPath("$.authorFirstName").value(user.getFirstName()))
                .andExpect(jsonPath("$.authorLastName").value(user.getLastName()))
                .andExpect(jsonPath("$.email").value(user.getUsername()))
                .andExpect(jsonPath("$.phone").value(user.getPhone()));
    }
    @Test
    void testRemoveAdsById() throws Exception {
        mockMvc.perform(delete("/ads/{id}", ads.getId())
                        .with(authentication(authentication)))
                .andExpect(status().isOk());
        assertFalse(adsRepository.findById(ads.getId()).isPresent());
    }
    @Test
    public void testReturnsListUserAds() throws Exception {
        mockMvc.perform(get("/ads/me")
                        .with(authentication(authentication)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.count").isNumber())
                .andExpect(jsonPath("$.results").isArray());
    }
    @Test
        public void testGetAdsImageCorrectImage() throws Exception {
        image.setData("image".getBytes());
        image.setMediaType("image/jpeg");
        imageRepository.save(image);
        ads.setImage(image);
        adsRepository.save(ads);
        mockMvc.perform(get("/ads/image/{id}", image.getId())
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .with(authentication(authentication)))
                .andExpect(status().isOk())
                .andExpect(content().bytes(image.getData()));
    }

}
