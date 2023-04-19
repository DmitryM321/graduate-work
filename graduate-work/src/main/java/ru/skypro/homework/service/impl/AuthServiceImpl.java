package ru.skypro.homework.service.impl;

import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.RegisterReq;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.service.UserService;

@Service
@Data
public class AuthServiceImpl implements AuthService {
  private final UserRepository userRepository;
  private final PasswordEncoder encoder;
  private final UserMapper userMapper;
  private final UserService userService;


  @Override
  public boolean login(String userName, String password) {
    if (!manager.userExists(userName)) {
      return false;
    }
    UserDetails userDetails = manager.loadUserByUsername(userName);
    return encoder.matches(password, userDetails.getPassword());
  }


  @Override
  public boolean register(RegisterReq registerReq, Role role) {
    if (manager.userExists(registerReq.getUserName())) {
      return false;
    }
    manager.createUser(
        User.builder()
            .passwordEncoder(this.encoder::encode)
            .password(registerReq.getPassword())
            .username(registerReq.getUserName())
            .roles(role.name())
            .build());
    return true;


}
}
