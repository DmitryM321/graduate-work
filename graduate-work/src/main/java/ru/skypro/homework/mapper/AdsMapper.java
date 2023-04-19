package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.skypro.homework.dto.AdsDTO;
import ru.skypro.homework.dto.CreateAdsDTO;
import ru.skypro.homework.dto.FullAdsDTO;
import ru.skypro.homework.model.Ads;
import ru.skypro.homework.model.Image;
//@Mapper(uses = {Ads.class})
//@Mapper(componentModel = "spring")
//AdsDTO INSTANCE = Mappers.getMapper( AdsDTO.class );
@Mapper(componentModel = "spring")
public interface AdsMapper extends MapperScheme<AdsDTO, Ads> {
// из AdsDTO в Ads
    @Mapping(source = "pk", target = "id")
    @Mapping( source = "author", target = "author.id")
    @Mapping(ignore = true, target = "image")
    Ads toEntity(AdsDTO dto);
    @Mapping( source = "id", target = "pk")
    @Mapping( source = "author.id", target = "author")
    @Mapping(source = "entity.image", target = "image", qualifiedByName = "imageMapping")
    AdsDTO toDto(Ads entity);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "image", ignore = true)
    Ads toEntity(CreateAdsDTO  dto);
    @Mapping( source = "id", target = "pk")
    @Mapping( source = "author.firstName", target = "firstName")
    @Mapping(source = "author.lastName", target = "lastName")
    @Mapping(source = "author.phone", target = "phone")
    @Mapping(source = "author.userName", target = "userName")
    @Mapping(source = "entity.image", target = "image", qualifiedByName = "imageMapping")
    FullAdsDTO toFullAdsDto(Ads entity);
    @Named("imageMapping")
    default String imageMapping(Image image) {
        if (image == null) {
            return null;
        }
        return "/ads/image/" +image.getId();
    }




}

