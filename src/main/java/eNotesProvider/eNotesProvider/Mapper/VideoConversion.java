package eNotesProvider.eNotesProvider.Mapper;

import org.mapstruct.Mapper;

import eNotesProvider.eNotesProvider.Dto.VideoDto;
import eNotesProvider.eNotesProvider.Model.Video;

@Mapper(componentModel = "spring")
public interface VideoConversion {
     
          VideoDto toDto(Video savedVideo);
            Video toEntity(VideoDto videoDto);
}
