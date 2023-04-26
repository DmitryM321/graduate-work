package ru.skypro.homework.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.skypro.homework.dto.AdsCommentDTO;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.Image;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "author", ignore = true)
    @Mapping(target = "id", source="pk")
    @Mapping(target = "createdAt", ignore = true)
    Comment toEntity(AdsCommentDTO dto);
    @Mapping(source = "author.id", target = "author")
    @Mapping(source = "author.firstName", target = "authorFirstName")
    @Mapping(source = "author.image", target = "authorImage", qualifiedByName = "imageMapping")
    @Mapping(target = "pk", source = "id")
    @Mapping(target = "createdAt", expression = "java((int) entity.getCreatedAt().getEpochSecond())")
    AdsCommentDTO toDto(Comment entity);
    @Named("imageMapping")
    default String imageMapping(Image image) {
        if (image == null) {
            return null;
        }
        return "/users/image/" + image.getId();
    }
}