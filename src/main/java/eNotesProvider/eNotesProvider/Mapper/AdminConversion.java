package eNotesProvider.eNotesProvider.Mapper;

import org.mapstruct.Mapper;

import eNotesProvider.eNotesProvider.Dto.AdminDto;
import eNotesProvider.eNotesProvider.Model.AdminModel;

@Mapper(componentModel = "spring")
public interface AdminConversion {
     
    AdminModel toAdminModel(AdminDto adminDto);

    AdminDto toAdminDto(AdminModel adminModel);
}
