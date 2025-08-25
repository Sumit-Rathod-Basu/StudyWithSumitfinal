package eNotesProvider.eNotesProvider.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "videos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Video{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    // store video URL (YouTube embed / Cloudinary / S3 link)
    private String videoUrl;

    private String thumbnailUrl; // optional preview image
}