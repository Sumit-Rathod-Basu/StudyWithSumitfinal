package eNotesProvider.eNotesProvider.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor @AllArgsConstructor
@Table(name = "notes")
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private String subject;

    private boolean isPaid;
      private Long amount;
  //  private String previewUrl;     // URL to show thumbnail in frontend
   // private String downloadUrl;
    @Column(columnDefinition = "TEXT")
private String previewUrl;

@Column(columnDefinition = "TEXT")
private String downloadUrl;

}