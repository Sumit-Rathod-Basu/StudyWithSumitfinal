package eNotesProvider.eNotesProvider.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import eNotesProvider.eNotesProvider.Model.User;

public interface Userrepo extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);

}
