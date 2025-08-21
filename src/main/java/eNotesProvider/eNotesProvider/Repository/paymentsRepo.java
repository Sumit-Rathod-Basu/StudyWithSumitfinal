package eNotesProvider.eNotesProvider.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

//import eNotesProvider.eNotesProvider.Dto.paymentsDto;
import eNotesProvider.eNotesProvider.Model.Payments;

public interface paymentsRepo extends JpaRepository<Payments,Long>{

    Payments findByNoteId(Long noteId);

    boolean existsByNoteId(Long id);

   

    Payments findByNoteIdAndEmail(Long noteId, String email);

    static void deleteAllByNoteId(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAllByNoteId'");
    }

    void deleteByNoteId(Long id);

   

    //paymentsDto updateStatusByNoteIdAndEmail(Long noteId, String email, String status);

}
