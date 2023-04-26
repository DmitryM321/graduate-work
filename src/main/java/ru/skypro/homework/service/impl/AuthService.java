package ru.skypro.homework.service.impl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.RegisterReq;
import ru.skypro.homework.service.UserDetailsManager;


@Service
@RequiredArgsConstructor
public class AuthService  {

    private final PasswordEncoder encoder;
    private final UserDetailsManager manager;

    public boolean login(String userName, String password) throws Exception {
        UserDetails userDetails = manager.loadUserByUsername(userName);
        boolean passwordMatch = encoder.matches(password, userDetails.getPassword());
        if (!passwordMatch) {
            throw new Exception("Password not match");
        }
        return true;
    }
    public boolean register(RegisterReq registerReq) throws Exception {
        manager.createUser(registerReq);
        return true;

    }
}
