package eNotesProvider.eNotesProvider.Model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor @AllArgsConstructor
@Table(name = "purcheseduser")
public class PurchesedUser {

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
