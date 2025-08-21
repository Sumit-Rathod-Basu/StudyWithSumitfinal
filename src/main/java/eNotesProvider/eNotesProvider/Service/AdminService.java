package eNotesProvider.eNotesProvider.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eNotesProvider.eNotesProvider.Model.AdminModel;
import eNotesProvider.eNotesProvider.Repository.AdminRepo;
@Service
public class AdminService {


    @Autowired
    private AdminRepo adminRepo;

 public void registerAdmin(AdminModel admin) {
        if (adminRepo.findByEmail(admin.getEmail()) != null) {
            throw new RuntimeException("❌ Email already registered!");
        }
        admin.setRole("ADMIN");
        adminRepo.save(admin);
    }

    // 👇 Add this method
    public AdminModel login(String email, String password) {
        AdminModel admin = adminRepo.findByEmail(email);

        if (admin == null) {
            throw new RuntimeException("❌ Admin not found");
        }

        if (!admin.getPassword().equals(password)) {
            throw new RuntimeException("❌ Invalid password");
        }

        return admin;
    }
    
    public List<AdminModel> getAllAdmins() {
    return adminRepo.findAll();

    
}

 public void updateAdmin(Long id, AdminModel updatedAdmin) {
        AdminModel existingAdmin =adminRepo.getAdminById(id);
        existingAdmin.setName(updatedAdmin.getName());
        existingAdmin.setEmail(updatedAdmin.getEmail());

        // Only update password if provided
        if (updatedAdmin.getPassword() != null && !updatedAdmin.getPassword().isEmpty()) {
            existingAdmin.setPassword(updatedAdmin.getPassword());
        }

        existingAdmin.setRole(updatedAdmin.getRole());
        adminRepo.save(existingAdmin);
    }

    public void deleteAdmin(Long id) {
        AdminModel admin = adminRepo.getAdminById(id);
        adminRepo.delete(admin);
    }

    public AdminModel getAdminById(Long id) {
        // TODO Auto-generated method stub

        return adminRepo.getAdminById(id);
       // throw new UnsupportedOperationException("Unimplemented method 'getAdminById'");
    }
}


