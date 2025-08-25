package eNotesProvider.eNotesProvider.Controller;

import eNotesProvider.eNotesProvider.Dto.VideoDto;
import eNotesProvider.eNotesProvider.Service.VideoService;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Controller
@RequestMapping("/videos")
public class VideoController {

    @Autowired
    private VideoService videoService;

    // Show all videos
     @GetMapping("/showall")
    public String listVideos(Model model , HttpSession  session) {
        model.addAttribute("videos", videoService.getAllVideos());
        return "uoloadedvideo"; // Thymeleaf template name
    } 
      
@GetMapping("/add")
public String getMethodName(Model model, HttpSession session) {
    // ✅ Check if admin is logged in
    if (session == null || session.getAttribute("loggedInAdmin") == null) {
        // If not logged in as admin → redirect to admin login
        return "redirect:/user/adminlogin";
    }

    // ✅ If logged in as admin → allow access
    model.addAttribute("videos", videoService.getAllVideos());
    return "videos"; // Thymeleaf template name
}

    // Handle video upload
@PostMapping("/add")
public String addVideo(@ModelAttribute VideoDto dto,
                       @RequestParam("videoFile") MultipartFile videoFile,
                       @RequestParam("thumbnailFile") MultipartFile thumbnailFile) throws IOException {

    videoService.saveVideo(dto, videoFile, thumbnailFile); // ✅ pass both files
    return "videos"; // better to redirect after POST
}


     // Delete video
    @GetMapping("/delete/{id}")
    public String deleteVideo(@PathVariable Long id) {
        videoService.deleteVideo(id);
        return "redirect:/videos/showall";
    } 


}
