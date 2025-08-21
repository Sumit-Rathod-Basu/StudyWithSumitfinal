package eNotesProvider.eNotesProvider.Dto;

import java.time.LocalDateTime;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor @AllArgsConstructor
public class PurchesedDto {
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
