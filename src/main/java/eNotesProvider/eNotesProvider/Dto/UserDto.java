package eNotesProvider.eNotesProvider.Dto;

import lombok.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String email;
    private String password;  // 🔑 Added password field
    private String role;
    private String name;      // ADMIN / STUDENT
}
