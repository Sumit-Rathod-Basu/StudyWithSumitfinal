package eNotesProvider.eNotesProvider.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import eNotesProvider.eNotesProvider.Model.Note;

public interface NoteRepo extends JpaRepository<Note,Long> {

  

    

    List<Note> findAllById(Long noteId);

}
