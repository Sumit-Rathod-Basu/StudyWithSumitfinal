package eNotesProvider.eNotesProvider.Controller;



import eNotesProvider.eNotesProvider.Dto.UserDto;
import eNotesProvider.eNotesProvider.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class Userregitration {

    @Autowired
    private UserService userService;

    // Step 1: Send verification code
    @PostMapping("/send-code")
public String sendCode(@RequestParam String email, Model model) {
    try {
        userService.sendVerificationCode(email);
        model.addAttribute("success", "Verification code sent to your email!");
    } catch (Exception e) {
        model.addAttribute("error", e.getMessage());
    }

    // 👇 Always add a user object for Thymeleaf binding
    UserDto userDto = new UserDto();
    userDto.setEmail(email); // keep email in hidden input
    model.addAttribute("user", userDto);

    return "register"; // Thymeleaf can now find ${user}
}


    // Step 2: Verify code & Register
/*     @PostMapping("/UserCheck")
    public String register(@ModelAttribute UserDto user,
                           @RequestParam String code,
                           Model model) {
        try {
            userService.registerUser(user, code);
            model.addAttribute("success", "Registration successful!");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        //    model.addAttribute("user", new UserDto());
    return "registerCheck";
       // return "register";
    } */
   @PostMapping("/UserCheck")
public String register(@ModelAttribute UserDto user,
                       @RequestParam String code,
                       Model model) {
    try {
        // ✅ Step 1: Check if email already exists


        // ✅ Step 2: Register the user
       Boolean value= userService.registerUser(user, code);

        // ✅ Step 3: Show success only after user is stored
       
        model.addAttribute("success", "🎉 Registration successful!");
        return "registerCheck"; // Show celebration page
        

    } catch (Exception e) {
        model.addAttribute("error", e.getMessage());
        return "register"; // Stay on registration page if any error
    }
}

}
