package ru.skypro.homework.mapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.skypro.homework.dto.AdsDTO;
import ru.skypro.homework.dto.CreateAdsDTO;
import ru.skypro.homework.dto.FullAdsDTO;
import ru.skypro.homework.model.Ads;
import ru.skypro.homework.model.Image;

@Mapper(componentModel = "spring")
public interface AdsMapper {

    @Mapping(source = "id", target = "pk")
    @Mapping(source = "author.id", target = "author")
    @Mapping(source = "image", target = "image", qualifiedByName = "imageMapping")
        //  @Mapping(target = "description", source = "description") // Необходимый маппинг
    AdsDTO toDto(Ads ads);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "image", ignore = true)
    Ads toEntity(CreateAdsDTO  createAdsDTO);

    @Mapping(source = "author.firstName", target = "authorFirstName")
    @Mapping(source = "author.lastName", target = "authorLastName")
    @Mapping(source = "author.phone", target = "phone")
    @Mapping(source = "author.username", target = "email")
    @Mapping(source = "image", target = "image", qualifiedByName = "imageMapping")
    @Mapping(source = "id", target = "pk")
    FullAdsDTO toFullAdsDto(Ads ads);

    @Named("imageMapping")
    default String imageMapping(Image image) {
        if (image == null) {
            return null;
        }
        return "/ads/image/" +image.getId();
    }
}

