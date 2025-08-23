package eNotesProvider.eNotesProvider.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

//import ch.qos.logback.core.model.Model;
import eNotesProvider.eNotesProvider.Dto.NoteDto;
import org.springframework.ui.Model; 

@Controller
@RequestMapping("/")
public class homeController {
   
    @Autowired
    private eNotesProvider.eNotesProvider.Service.NoteService noteService;
      @GetMapping({"/", "/home"})
    public String homePage() {
        // This will look for a file named home.html inside templates folder
        return "home";
    }
        @GetMapping("/demo")
    public String getNotes(Model model) {
        List<NoteDto> notes = noteService.getAllNotes(); // fetch all notes
        model.addAttribute("notes", notes);
        return "demo"; // Thymeleaf template name: notes.html
    }
}
