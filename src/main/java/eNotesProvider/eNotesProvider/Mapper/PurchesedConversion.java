package eNotesProvider.eNotesProvider.Mapper;

import org.mapstruct.Mapper;

import eNotesProvider.eNotesProvider.Dto.PurchesedDto;
import eNotesProvider.eNotesProvider.Model.PurchesedUser;

@Mapper(componentModel = "spring")
public interface PurchesedConversion {
       
    PurchesedUser toPurchesedUser(PurchesedDto dto);
    PurchesedDto toPurchesedDto(PurchesedUser user);
}
