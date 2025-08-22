package eNotesProvider.eNotesProvider.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class homeController {

      @GetMapping({"/", "/home"})
    public String homePage() {
        // This will look for a file named home.html inside templates folder
        return "home";
    }
}
