package eNotesProvider.eNotesProvider.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "device_bindings")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class DeviceBinding {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String deviceFingerprint; // fingerprint registered on first secure open
    private String noteAccessScope;   // e.g., "ALL" or CSV of note ids
}