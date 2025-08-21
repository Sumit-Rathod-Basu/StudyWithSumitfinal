package eNotesProvider.eNotesProvider.Dto;



import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class paymentsDto {
    private Long id;                  // Payment ID
    private String transactionId;     // Transaction reference
    private String email; 
    private String name;          // User email who made the payment
    private Double amount;            // Payment amount
    private String status;            // SUCCESS / FAILED
    private LocalDateTime createdAt;  // Timestamp of payment
    private Long noteId;              // ID of the note purchased
    private String noteTitle;         // Optional: title of the purchased note
}
