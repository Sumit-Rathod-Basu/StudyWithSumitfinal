package eNotesProvider.eNotesProvider.Dto;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceBindingDto {
    private Long id;
    private Long userId;
    private String deviceFingerprint; // fingerprint of the device
    private String noteAccessScope;   // notes this device can access
}