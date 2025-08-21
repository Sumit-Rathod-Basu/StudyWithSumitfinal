package eNotesProvider.eNotesProvider.Mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import eNotesProvider.eNotesProvider.Dto.UserDto;
import eNotesProvider.eNotesProvider.Model.User;

@Component
@Mapper(componentModel = "spring")
public interface UserCoversion {

    UserDto toUserDto(User user);
    User toUser(UserDto userDto);
}
