package eNotesProvider.eNotesProvider.Model;



import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor @AllArgsConstructor
@Table(name = "payments")
public class Payments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String transactionId;
    private String email;
    private Double amount;
    private String status; // SUCCESS / FAILED
    private LocalDateTime createdAt = LocalDateTime.now();
    private String noteTitle;  
    private Long noteId;  
   
}
