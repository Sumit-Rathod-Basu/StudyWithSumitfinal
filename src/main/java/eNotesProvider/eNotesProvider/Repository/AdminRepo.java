package eNotesProvider.eNotesProvider.Repository;



import org.springframework.data.jpa.repository.JpaRepository;

import eNotesProvider.eNotesProvider.Model.AdminModel;

public interface AdminRepo extends JpaRepository<AdminModel, Long> {

  //  Object findByEmail(String email);
       //Optional<AdminModel> findByEmail(String email);
        AdminModel findByEmail(String email);

        AdminModel getAdminById(Long id);

}
