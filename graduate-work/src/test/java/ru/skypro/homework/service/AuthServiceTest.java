package ru.skypro.homework.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.skypro.homework.dto.RegisterReq;
import ru.skypro.homework.service.impl.AuthService;
import ru.skypro.homework.model.User;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private PasswordEncoder encoder;
    @InjectMocks
    private AuthService authService;
    @Mock
    private UserDetailsManager manager;
    @Test
    public void testAuthenticateUserOnCallingLoginMethod() throws Exception {
        String userName = "AuthServiceTest";
        String password = "password";
        String encodedPassword = "passwordPassword";
        User user = new User();
        user.setUsername(userName);
        user.setPassword(encodedPassword);
        org.springframework.security.core.userdetails.User userDetails =
                new org.springframework.security.core.userdetails.User(
                        user.getUsername(),
                        user.getPassword(),
                        new ArrayList<>()
                );
        when(manager.loadUserByUsername(userName)).thenReturn(userDetails);
        when(encoder.matches(password, encodedPassword)).thenReturn(true);
        boolean result = authService.login(userName, password);
        assertTrue(result);
        verify(manager, times(1)).loadUserByUsername(userName);
        verify(encoder, times(1)).matches(password, encodedPassword);
    }
    @Test
    public void testRegisterUser() throws Exception {
        RegisterReq registerReq = new RegisterReq();
        registerReq.setFirstName("Four");
        registerReq.setLastName("Four");
        registerReq.setPhone("+79297175544");
        registerReq.setUsername("Four@Four");
        registerReq.setPassword("password");
        doNothing().when(manager).createUser(registerReq);
        boolean result = authService.register(registerReq);
        assertTrue(result);
        verify(manager).createUser(registerReq);
    }
}


