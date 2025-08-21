package eNotesProvider.eNotesProvider.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import eNotesProvider.eNotesProvider.Model.PurchesedUser;

public interface PurchesedRepo extends JpaRepository<PurchesedUser,Long>{

    List<PurchesedUser> findByEmail(String email);

}
