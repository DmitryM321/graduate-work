package ru.skypro.homework.mapper;


import org.mapstruct.Mapper;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper extends MapperScheme<UserDTO, User> {



}