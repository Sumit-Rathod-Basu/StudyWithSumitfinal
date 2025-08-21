package eNotesProvider.eNotesProvider.Mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import eNotesProvider.eNotesProvider.Dto.NoteDto;
import eNotesProvider.eNotesProvider.Model.Note;

@Mapper(componentModel = "spring")
@Component()
public interface NoteConversion {
  
      NoteDto toNoteDto(Note note);
      Note  toNote(NoteDto noteDto);
      static Note map(NoteDto noteDto, Class<Note> class1) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'map'");
      }
}
