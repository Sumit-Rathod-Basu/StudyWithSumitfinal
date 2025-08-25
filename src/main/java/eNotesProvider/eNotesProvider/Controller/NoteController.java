package eNotesProvider.eNotesProvider.Controller;

import eNotesProvider.eNotesProvider.Dto.NoteDto;
import eNotesProvider.eNotesProvider.Service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    @PostMapping("/upload")
    public ResponseEntity<NoteDto> uploadNote(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("subject") String subject,
            @RequestParam("isPaid") boolean isPaid,
             @RequestParam("amount") Long amount ,
            @RequestParam("pdfFile") MultipartFile pdfFile,
            @RequestParam("imageFile") MultipartFile imageFile
    ) throws Exception {

        NoteDto dto = new NoteDto(null, title, description, subject, isPaid, amount,null, null);

        return ResponseEntity.ok(noteService.saveNote(dto, pdfFile, imageFile));
    }
    
}
