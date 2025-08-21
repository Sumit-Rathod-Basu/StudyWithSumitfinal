package eNotesProvider.eNotesProvider.Service;

import eNotesProvider.eNotesProvider.Dto.UserDto;
import eNotesProvider.eNotesProvider.Mapper.UserCoversion;

import eNotesProvider.eNotesProvider.Model.User;

import eNotesProvider.eNotesProvider.Repository.Userrepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private Userrepo userRepository;

    @Autowired
    private UserCoversion userCoversion;

    @Autowired
    private JavaMailSender mailSender;
    
 
    // for sending email

    // Store verification codes temporarily (email -> code)
    private final Map<String, String> verificationCodes = new HashMap<>();

    // Step 1: Send Verification Code
    public void sendVerificationCode(String email) throws Exception {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new Exception("Email already exists");
        }

        // Generate random 6-digit code
        String code = String.valueOf(100000 + new Random().nextInt(900000));
        verificationCodes.put(email, code);

        // Send Email
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your Verification Code");
        message.setText("Your 6-digit verification code is: " + code);
        mailSender.send(message);
    }

    // Step 2: Verify and Register User
    public Boolean registerUser(UserDto user, String code) throws Exception {
        String savedCode = verificationCodes.get(user.getEmail());

        if (savedCode == null || !savedCode.equals(code)) {
            throw new Exception("Invalid or expired verification code");
        }

        User user1 = userCoversion.toUser(user);
        User savedUser = userRepository.save(user1);

        // Remove code after successful verification
        verificationCodes.remove(user.getEmail());

        return true;
    }

    // Login
    public User login(String email, String password) throws Exception {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            throw new Exception("User not found");
        }
        User user = userOpt.get();
        if (!user.getPassword().equals(password)) {
            throw new Exception("Invalid password");
        }
        return user;
    }
    public boolean existsByEmail(String email) {
    return userRepository.findByEmail(email).isPresent();
}

 // 🔥 get all users
    /**
     * @return
     */
  public List<UserDto> getAllUsers() {
    return userRepository.findAll()
            .stream()
            .map(user -> new UserDto(
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    user.getRole(),
                    user.getPassword()   // ✅ last argument, no extra comma
            ))
            .collect(Collectors.toList());
}

    public void deleteUser(Long id) {
        // TODO Auto-generated method stub
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        }
    }

        public void updateUser(Long id, UserDto userDto) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found!"));
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setRole(userDto.getRole());
        userRepository.save(user);
    }
    public UserDto getUserById(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found!"));
        return userCoversion.toUserDto(user);
    }

}




