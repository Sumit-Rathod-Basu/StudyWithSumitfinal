package eNotesProvider.eNotesProvider.Controller;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dnxpr5ldm",  // तुझा cloud_name टाक
                "api_key", "466619173339999",
                "api_secret", "ipUU60Zkshtw7ZxJZ6ekomaDv_A"
        ));
    }
}