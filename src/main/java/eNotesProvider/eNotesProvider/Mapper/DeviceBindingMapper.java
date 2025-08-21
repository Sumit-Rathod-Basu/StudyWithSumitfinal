package eNotesProvider.eNotesProvider.Mapper;

import eNotesProvider.eNotesProvider.Model.DeviceBinding;
import eNotesProvider.eNotesProvider.Dto.DeviceBindingDto;
import org.mapstruct.Mapper;
//import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DeviceBindingMapper {

   // DeviceBindingMapper INSTANCE = Mappers.getMapper(DeviceBindingMapper.class);

    // Entity → DTO
    DeviceBindingDto toDto(DeviceBinding entity);

    // DTO → Entity
    DeviceBinding toEntity(DeviceBindingDto dto);
}