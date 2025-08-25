package eNotesProvider.eNotesProvider.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class VideoDto {
    private Long id;
    private String title;
    private String description;
    private String videoUrl;
    private String thumbnailUrl;
}
