package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.AdsCommentDTO;
import ru.skypro.homework.model.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper extends MapperScheme<AdsCommentDTO, Comment> {


}