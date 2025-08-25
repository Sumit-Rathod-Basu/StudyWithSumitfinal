package eNotesProvider.eNotesProvider.Controller;



import eNotesProvider.eNotesProvider.Dto.AdminDto;
import eNotesProvider.eNotesProvider.Dto.NoteDto;
import eNotesProvider.eNotesProvider.Dto.PurchesedDto;
import eNotesProvider.eNotesProvider.Dto.UserDto;
import eNotesProvider.eNotesProvider.Dto.paymentsDto;
import eNotesProvider.eNotesProvider.Model.AdminModel;
import eNotesProvider.eNotesProvider.Model.Note;
import eNotesProvider.eNotesProvider.Model.Payments;
import eNotesProvider.eNotesProvider.Model.PurchesedUser;
import eNotesProvider.eNotesProvider.Model.User;
import eNotesProvider.eNotesProvider.Repository.NoteRepo;
import eNotesProvider.eNotesProvider.Repository.PurchesedRepo;
import eNotesProvider.eNotesProvider.Repository.paymentsRepo;
import eNotesProvider.eNotesProvider.Service.AdminService;
import eNotesProvider.eNotesProvider.Service.EmailService;
import eNotesProvider.eNotesProvider.Service.NoteService;
import eNotesProvider.eNotesProvider.Service.PaymentService;
import eNotesProvider.eNotesProvider.Service.UserService;
import eNotesProvider.eNotesProvider.Service.VideoService;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;


@Controller   // ✅ for Thymeleaf views
@RequestMapping("/user")
@CrossOrigin()
public class UserControllerhandler {

    @Autowired
    private UserService userService;

    @Autowired
    private NoteService noteService;

    @Autowired
    private NoteRepo Noterepo;

    @Autowired
    private PaymentService paymentService;
     
    @Autowired
    private EmailService emailService;

    @Autowired
    private PurchesedRepo purchesedRepo;

    @Autowired
    private paymentsRepo paymentsRepo;

   @Autowired
    private AdminService adminService;

    @Autowired
    private VideoService videoService;
 
    @GetMapping({"/", "/home"})
    public String homePage() {
        // This will look for a file named home.html inside templates folder
        return "home";
    }
         @GetMapping("/about")
    public String aboutPage() {
        return "about"; // Thymeleaf template name: about.html
    }

    // ✅ Show registration form
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new UserDto()); // empty object for form binding
        return "register"; // -> looks for templates/register.html
    }

@GetMapping("/MyNotes")
public String viewPurchasedNotes(HttpSession session, Model model) {
    // Retrieve logged-in user from session
    User user = (User) session.getAttribute("loggedInUser"); 
    System.out.println("session is :" + session);
    System.out.println("user is :" + user);

    if (user == null) {
        model.addAttribute("message", "Please log in to view your purchased notes.");
        return "pur"; // Thymeleaf template
    }

    String userEmail = user.getEmail();
    System.out.println("userEmail is :" + userEmail);

    // Fetch purchased notes for this user
    List<PurchesedUser> purchasedNotes = purchesedRepo.findByEmail(userEmail);
    System.out.println("purchasedNotes is :" + purchasedNotes);

    if (purchasedNotes.isEmpty()) {
        model.addAttribute("message", "You haven't purchased any notes yet.");
    } else {
        // Collect all noteIds from purchasedNotes
        List<Long> noteIds = purchasedNotes.stream()
                                           .map(PurchesedUser::getNoteId) // get noteId from each purchased record
                                           .toList();

        // Fetch all notes by IDs
        List<Note> notes = Noterepo.findAllById(noteIds);

        // Send to Thymeleaf
        model.addAttribute("notes", notes);
    }

    return "pur"; // Thymeleaf template
}




    // ✅ Show login form
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new UserDto());
        return "login"; // -> templates/login.html
    }

    // ✅ Handle login
    @PostMapping("/Dashboard")
    public String loginUser(@ModelAttribute("user") UserDto user, Model model, HttpSession session) {
        try {
            var loggedInUser = userService.login(user.getEmail(), user.getPassword());
            model.addAttribute("user", loggedInUser);
             session.setAttribute("loggedInUser", loggedInUser);
                List<NoteDto> notes = noteService.getAllNotes(); // fetch all notes from DB
                   model.addAttribute("notes", notes);
            return "dashbord"; // -> templates/dashboard.html
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "login"; // -> stay on login page with error message
        }

            // 👉 Upload page
 
    }
       @GetMapping("/upload")
    public String uploadPage() {
        return "note";  // loads upload.html
    }
     @PostMapping("/upload")
    public String uploadNote(
            @ModelAttribute NoteDto note,
            @RequestParam("pdfFile") MultipartFile pdfFile,
            @RequestParam("imageFile") MultipartFile imageFile,
            Model model,HttpSession session
    ) {
            if (session.getAttribute("loggedInAdmin") == null) {
            return "redirect:/user/adminlogin"; // Redirect to login if not logged in
        }

        try {
            NoteDto savedNote1 = noteService.saveNote(note, pdfFile, imageFile);
            model.addAttribute("success", "Note uploaded successfully!");
            model.addAttribute("note", new NoteDto()); // reset form
        } catch (Exception e) {
            model.addAttribute("error", "Error: " + e.getMessage());
        }
        return "note-upload";
    }
    @GetMapping("/notes")
    public String viewAllNotes(Model model) {
        model.addAttribute("notes", noteService.getAllNotes());
        return "notes"; // Thymeleaf template: notes.html
    }
    @GetMapping("/payments/{id}")
public String paymentsPage(@PathVariable("id") String id, Model model) throws IOException {
    // Fetch the note by ID
   Long noteId = Long.valueOf(id);
    Optional<Note> optionalNote = Noterepo.findById(noteId);

    if (optionalNote.isPresent()) {
        model.addAttribute("note", optionalNote.get());
        return "payments";
    } else {
        return "error"; // or redirect to 404 page
    }
}

    @PostMapping("/confirm-payment")
    public String confirmPayment(@ModelAttribute("payment") paymentsDto dto, Model model) {
        dto.setStatus("Pending"); 
      
        paymentsDto savedPayment = paymentService.savePayment(dto);
            // paymentsDto savedPayment = paymentService.savePayment(dto);


        model.addAttribute("payment", savedPayment);
        model.addAttribute("amount", savedPayment.getAmount());
        model.addAttribute("transactionId", savedPayment.getTransactionId());
        model.addAttribute("email", savedPayment.getEmail());
        model.addAttribute("name", savedPayment.getName());
       // model.addAttribute("phone", savedPayment.getphone());
 
           
    // 1. Send email to USER (acknowledgement)
    emailService.sendUserPendingEmail(savedPayment);

    // 2. Send email to ADMIN (verification link)
    emailService.sendAdminVerificationEmail(savedPayment,"sumitrathod808085@gmail.com");
    
         // ✅ new page that shows success message
        return "payment-success"; 
    }
@PostMapping("/adminlogin")
public String loginAdmin(@ModelAttribute("admin") AdminDto admin,
                         Model model, HttpSession session) {
    try {
        AdminModel loggedInAdmin = adminService.login(admin.getEmail(), admin.getPassword());
        
        // store admin in session
        session.setAttribute("loggedInAdmin", loggedInAdmin);
        
        return "redirect:/user/admin"; // your admin dashboard mapping
    } catch (RuntimeException e) {
        model.addAttribute("error", e.getMessage());
        return "admin-login"; // back to login page
    }
}

      
/*     @GetMapping("/admin")
    public String dashboard(Model model) {
         List<NoteDto> notes = noteService.getAllNotes(); // fetch notes
    model.addAttribute("notes", notes);
        model.addAttribute("notesCount", 5);
        model.addAttribute("usersCount", 10);
        model.addAttribute("paymentsCount", 3);
        model.addAttribute("plansCount", 2);
        return "admin"; // matches AdminDash.html
    } */
      @GetMapping("/admin")
    public String dashboard(Model model, HttpSession session) {

        if (session.getAttribute("loggedInAdmin") == null) {
            return "redirect:/user/adminlogin"; // Redirect to login if not logged in
        }

        List<NoteDto> notes = noteService.getAllNotes();
        model.addAttribute("notes", notes);
        model.addAttribute("notesCount", notes.size());
            model.addAttribute("video", videoService.getAllVideos().size()); // Placeholder for video count
        model.addAttribute("usersCount", userService.getAllUsers().size());
        model.addAttribute("paymentsCount", paymentService.getAllPayments().size());
        model.addAttribute("plansCount", purchesedRepo.count()); // Assuming you have a method to count plans

        return "admin"; // admin.html (your dashboard page)
    }

     @GetMapping("/admin/users")
    public String showAdminUsersPage(Model model,HttpSession session) {
            if (session.getAttribute("loggedInAdmin") == null) {
            return "redirect:/user/adminlogin"; // Redirect to login if not logged in
        }

        // Fetch all users from the database
        List<UserDto> users = userService.getAllUsers();

        // Add to model for Thymeleaf template
        model.addAttribute("users", users);
        model.addAttribute("usersCount", users.size());

        // Return Thymeleaf template name
        return "users"; // admin-users.html
    }

    @GetMapping("/admin/payments")
    public String showPaymentsPage(Model model, HttpSession session) {

            if (session.getAttribute("loggedInAdmin") == null) {
            return "redirect:/user/adminlogin"; // Redirect to login if not logged in
        }

        // Fetch all payments
        List<paymentsDto> payments = paymentService.getAllPayments();

        // Add to Thymeleaf model
        model.addAttribute("payments", payments);
        model.addAttribute("paymentsCount", payments.size());

        // Return template
        return "paymentsuser"; // Thymeleaf file: admin-payments.html
    }

@GetMapping("/details/{noteId}/{email}")
public String viewNoteDetails(@PathVariable Long noteId,
                              @PathVariable String email,
                              Model model) {
    // Query payment by noteId + email
    Payments payment = paymentsRepo.findByNoteIdAndEmail(noteId, email);

    if (payment == null) {
        model.addAttribute("message", "No payment found for this note and email.");
        return "error"; 
    }

    // Fetch note
    Optional<Note> optionalNote = Noterepo.findById(noteId);

    model.addAttribute("note", optionalNote.orElse(null));
    model.addAttribute("payment", payment);

    return "userdetails"; // Thymeleaf template
}

@PostMapping("/approve")
public String approveNote(@RequestParam("id") Long noteId,
                          @RequestParam("email") String email,
                          RedirectAttributes redirectAttributes) 
{   
    String status = "Approved";
    System.out.println("noteId: " + noteId + ", email: " + email);

    paymentsDto payment = paymentService.updateStatusByNoteIdAndEmail(noteId, email, status);

    String emailBody = "Dear User,\n\n"
        + "Your payment with Transaction ID: " + payment.getTransactionId() + " has been successfully verified.\n\n"
        + "You can now access your selected notes.\n\n"
        + "Thank you for trusting StudyWithSumit Platform!";

    PurchesedUser purchasedUser = new PurchesedUser();
    purchasedUser.setNoteId(payment.getNoteId());
    purchasedUser.setNoteTitle(payment.getNoteTitle());
    purchasedUser.setEmail(payment.getEmail());
    purchasedUser.setAmount(payment.getAmount());
    purchasedUser.setTransactionId(payment.getTransactionId());
    purchasedUser.setName(payment.getName());
    purchasedUser.setStatus("SUCCESS");
    purchasedUser.setCreatedAt(LocalDateTime.now());

    purchesedRepo.save(purchasedUser);

    emailService.approvePayment(payment.getTransactionId(), payment.getEmail());

    redirectAttributes.addFlashAttribute("message", "Note approved successfully!");
    return "redirect:/user/admin/payments";
}

@PostMapping("/reject")
public String rejectNote(@RequestParam("id") Long noteId,
                         @RequestParam("email") String email,
                         RedirectAttributes redirectAttributes) 
{
    String status = "Rejected";
    System.out.println("noteId: " + noteId + ", email: " + email);

    // Update the payment status to Rejected
    paymentsDto payment = paymentService.updateStatusByNoteIdAndEmail(noteId, email, status);

    // Prepare rejection email content
    String emailBody = "Dear User,\n\n"
        + "We regret to inform you that your payment with Transaction ID: " + payment.getTransactionId() + " has been rejected.\n\n"
        + "If you have any questions or need assistance, please contact our support team.\n\n"
        + "Thank you for using StudyWithSumit Platform!";

    // Send rejection email
    emailService.rejectPayment(payment.getTransactionId(), payment.getEmail());

    redirectAttributes.addFlashAttribute("message", "Note rejected successfully!");
    return "redirect:/user/admin/payments";
}

  @PostMapping("/delete")
    public String deletePaymentNote(@RequestParam("id") Long noteId, RedirectAttributes redirectAttributes) {
         
            paymentService.deletePayment(noteId);
            redirectAttributes.addFlashAttribute("message", "Note deleted successfully!");
        
        return "redirect:/user/admin/payments"; 
    }

        @GetMapping("/payments/list")
    public String listPayments(Model model,HttpSession session) {
            if (session.getAttribute("loggedInAdmin") == null) {
            return "redirect:/user/adminlogin"; // Redirect to login if not logged in
        }

        List<PurchesedDto> payments = paymentService.findAlldata();
        model.addAttribute("payments", payments);
        return "purchesedUsers"; // Thymeleaf template path
    }

        @PostMapping("/delete-payment")
    public String deletePayment(@RequestParam("paymentId") Long paymentId,
                                RedirectAttributes redirectAttributes) {
        try {
            paymentService.deletePayment1(paymentId);
            redirectAttributes.addFlashAttribute("message", "Payment deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting payment: " + e.getMessage());
        }
        return "redirect:/user/payments/list"; // Redirect back to payments list page
    }


     @PostMapping("/admin/notes/delete/{id}")
public String deleteNote(@PathVariable Long id, RedirectAttributes redirectAttributes) {
    try {
        noteService.deleteNoteById(id);
        redirectAttributes.addFlashAttribute("success", "Note deleted successfully!");
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("error", "Failed to delete note!");
    }
    return "redirect:/user/admin"; // Redirect back to dashboard
}

    @GetMapping("/Admin-Login")
    public String loginPage(Model model) {
        model.addAttribute("admin", new AdminDto());
        return "admin-login"; // -> templates/admin-login.html
    }

    @GetMapping("/adminregister")
    public String showRegisterPage(Model model) {
        model.addAttribute("admin", new AdminModel());
        return "admin-register"; // Thymeleaf page
    }

        @PostMapping("/register")
    public String registerAdmin(@ModelAttribute("admin") AdminModel admin, Model model) {
        try {
            adminService.registerAdmin(admin);
            model.addAttribute("success", "✅ Admin registered successfully! Please login.");
            model.addAttribute("admin", new AdminModel()); // reset form
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "admin-register";
    }
    
        @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/user/admin/users"; // back to list
    }

        // ✅ Edit form
    @GetMapping("/edit/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {
        UserDto user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "admin-user-edit"; // admin-user-edit.html
    }

    // ✅ Update user
    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable Long id,
                             @ModelAttribute("user") UserDto user) {
        userService.updateUser(id, user);
        return "redirect:/user/admin/users"; // back to list
    }

    @GetMapping("/admins")
public String listAdmins(Model model) {
    List<AdminModel> admins = adminService.getAllAdmins();
    model.addAttribute("admins", admins);
    return "admin-list"; // Thymeleaf page
}
  // ✅ Edit form
    @GetMapping("/admin/edit/{id}")
    public String editAdminForm(@PathVariable Long id, Model model) {
        AdminModel admin = adminService.getAdminById(id);
        model.addAttribute("admin", admin);
        return "admin-edit"; // edit form page
    }

    // ✅ Update admin
    @PostMapping("/admin/update/{id}")
    public String updateAdmin(@PathVariable Long id,
                              @ModelAttribute("admin") AdminModel updatedAdmin,
                              RedirectAttributes redirectAttributes) {
        try {
            adminService.updateAdmin(id, updatedAdmin);
            redirectAttributes.addFlashAttribute("success", "✅ Admin updated successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/user/admins";
    }

    // ✅ Delete admin
    @GetMapping("/Admin/delete/{id}")
    public String deleteAdmin(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            adminService.deleteAdmin(id);
            redirectAttributes.addFlashAttribute("success", "🗑 Admin deleted successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/user/admins";
    }
    
         // Logout endpoint
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // Invalidate the session to terminate user login
        session.invalidate();
        // Redirect to login page
        return "redirect:/user/";
    }

     // Serve the Coming Soon page
    @GetMapping("/comming")
    public String comingSoonPage() {
        return "comming-soon"; // Thymeleaf template name (coming-soon.html)
    }
}

