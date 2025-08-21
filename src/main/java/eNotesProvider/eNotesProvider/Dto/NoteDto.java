package eNotesProvider.eNotesProvider.Dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteDto {
    private Long id;
    private String title;
    private String description;
    private String subject;
    private boolean isPaid;
  private Long amount;
    private String previewUrl;     // URL to show thumbnail in frontend
    private String downloadUrl;    // Secure URL to download PDF
    //private String uploadedByEmail; // Optional: uploader info
}
