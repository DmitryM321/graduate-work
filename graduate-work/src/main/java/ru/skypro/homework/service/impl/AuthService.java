package ru.skypro.homework.service.impl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.RegisterReq;
import ru.skypro.homework.config.security.UserDetailsManager;


@Service
@RequiredArgsConstructor
public class AuthService  {

    private final PasswordEncoder encoder;
    private final UserDetailsManager manager;

    public boolean login(String userName, String password) {
        UserDetails userDetails = manager.loadUserByUsername(userName);
        return encoder.matches(password, userDetails.getPassword());
    }
    public boolean register(RegisterReq registerReq) throws Exception {
        manager.createUser(registerReq);
        return true;

    }
}
