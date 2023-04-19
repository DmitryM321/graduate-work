package ru.skypro.homework.service;

import ru.skypro.homework.dto.RegisterReq;
import ru.skypro.homework.dto.Role;

public interface AuthService {
//    boolean login(String userName, String password);
    boolean login(String userName, String password) throws Exception;
    boolean register(RegisterReq registerReq, Role role);
}
