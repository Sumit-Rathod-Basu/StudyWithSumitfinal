package eNotesProvider.eNotesProvider.Service;

import eNotesProvider.eNotesProvider.Dto.NoteDto;
import eNotesProvider.eNotesProvider.Dto.PurchesedDto;
import eNotesProvider.eNotesProvider.Mapper.NoteConversion;
//import eNotesProvider.eNotesProvider.Mapper.NoteMapper;
import eNotesProvider.eNotesProvider.Model.Note;
import eNotesProvider.eNotesProvider.Model.PurchesedUser;
import eNotesProvider.eNotesProvider.Repository.NoteRepo;
import eNotesProvider.eNotesProvider.Repository.PurchesedRepo;
import eNotesProvider.eNotesProvider.Repository.paymentsRepo;

//import eNotesProvider.eNotesProvider.Repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
@Service
public class NoteService {

    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    @Autowired
    private NoteRepo noteRepository;

    @Autowired
    private NoteConversion noteConversion;

    @Autowired
    private PurchesedRepo purchesedRepo;
   
    @Autowired
    private paymentsRepo paymentsRepoposuitory;
  public NoteDto saveNote(NoteDto dto, MultipartFile pdfFile, MultipartFile imageFile) throws IOException {
    // Create upload directory if not exists
    File dir = new File(UPLOAD_DIR);
    if (!dir.exists()) dir.mkdirs();

    String pdfFileName = null;
    String imageFileName = null;

    // Save PDF
    if (pdfFile != null && !pdfFile.isEmpty()) {
        pdfFileName = System.currentTimeMillis() + "_" + pdfFile.getOriginalFilename();
        Path pdfPath = Paths.get(UPLOAD_DIR + pdfFileName);
        Files.copy(pdfFile.getInputStream(), pdfPath, StandardCopyOption.REPLACE_EXISTING);
    }

    // Save Image
    if (imageFile != null && !imageFile.isEmpty()) {
        imageFileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
        Path imagePath = Paths.get(UPLOAD_DIR + imageFileName);
        Files.copy(imageFile.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
    }

    // Map DTO → Entity
    Note note = noteConversion.toNote(dto);
    note.setDownloadUrl(pdfFileName);    // store only file name
    note.setPreviewUrl(imageFileName);   // store only file name

    // Save in DB
    Note saved = noteRepository.save(note);

    return noteConversion.toNoteDto(saved);
}

     public List<NoteDto> getAllNotes() {
        List<Note> notes = noteRepository.findAll(); // fetch all notes from DB
        return notes.stream()
                .map(noteConversion::toNoteDto)
                .collect(Collectors.toList());
    }

     public Note findNoteByEmail(String email) {
        System.out.println("Finding note for email: " + email);
 
        List<PurchesedUser> users = purchesedRepo.findByEmail("sumitrathod8080@gmail.com");
    
    if (users.isEmpty()) {
        throw new RuntimeException("No purchased notes found for email: " + email);
    }
    
    PurchesedUser user = users.get(0); // get the first purchased note
    Optional<Note> note = noteRepository.findById(user.getNoteId()); 
    
    return note.orElseThrow(() -> new RuntimeException("Note not found for email: " + email));
}

   public List<Note> findNotesByPurchasedList(List<PurchesedDto> purchasedNotes) {
    return purchasedNotes.stream()
                         .map(dto -> noteRepository.findById(dto.getNoteId())
                                                    .orElse(null))
                         .filter(Objects::nonNull)
                         .collect(Collectors.toList());
}

   public void deleteNoteById(Long id) {
    // TODO Auto-generated method stub
    paymentsRepoposuitory.deleteByNoteId(id);
     noteRepository.deleteById(id);
  //  paymentsRepoposuitory.deleteByNoteId(id);

    // noteRepository.deleteById(id);
   }

}
