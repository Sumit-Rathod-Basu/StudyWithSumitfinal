package eNotesProvider.eNotesProvider.Service;

import eNotesProvider.eNotesProvider.Controller.CloudinaryConfig;
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

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
@Service
public class NoteService {

    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    private Cloudinary cloudinary;

    @Autowired
    private NoteRepo noteRepository;

    @Autowired
    private NoteConversion noteConversion;

    @Autowired
    private PurchesedRepo purchesedRepo;
   
    @Autowired
    private paymentsRepo paymentsRepoposuitory;

  @Autowired
    public NoteService(Cloudinary cloudinary,
                       NoteRepo noteRepository,
                       NoteConversion noteConversion) {
        this.cloudinary = cloudinary;
        this.noteRepository = noteRepository;
        this.noteConversion = noteConversion;
    }

    public NoteDto saveNote(NoteDto dto, MultipartFile pdfFile, MultipartFile imageFile) throws IOException {

        String pdfUrl = null;
        String imageUrl = null;

        // Generate a unique ID for file naming
        String uniqueId = UUID.randomUUID().toString();

 if (pdfFile != null && !pdfFile.isEmpty()) {

String originalFilename = pdfFile.getOriginalFilename();  // e.g. "mydocument.pdf"
String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
String publicId = UUID.randomUUID().toString() + "-note" + extension;

Map uploadResult = cloudinary.uploader().upload(
    pdfFile.getBytes(),
    ObjectUtils.asMap(
        "folder", "notes",
        "resource_type", "raw",
        "public_id", publicId
    )
);

pdfUrl = uploadResult.get("secure_url").toString();

}


        // Upload Image (auto resource type)
        if (imageFile != null && !imageFile.isEmpty()) {
            Map uploadResult = cloudinary.uploader().upload(
                    imageFile.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "notes",
                            "resource_type", "auto",
                            "public_id", uniqueId + "-preview"
                    )
            );
            imageUrl = uploadResult.get("secure_url").toString();
        }

        // Convert DTO to entity and set URLs
        Note note = noteConversion.toNote(dto);
        note.setDownloadUrl(pdfUrl);
        note.setPreviewUrl(imageUrl);

        // Save entity to database
        Note savedNote = noteRepository.save(note);

        System.out.println("Note saved with ID: " + savedNote.getId());

        return noteConversion.toNoteDto(savedNote);
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
