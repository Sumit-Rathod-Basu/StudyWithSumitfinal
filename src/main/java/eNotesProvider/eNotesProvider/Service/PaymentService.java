package eNotesProvider.eNotesProvider.Service;



import eNotesProvider.eNotesProvider.Dto.PurchesedDto;
import eNotesProvider.eNotesProvider.Dto.paymentsDto;
import eNotesProvider.eNotesProvider.Mapper.PaymentsConversion;
import eNotesProvider.eNotesProvider.Mapper.PurchesedConversion;
import eNotesProvider.eNotesProvider.Model.Payments;
import eNotesProvider.eNotesProvider.Model.PurchesedUser;
import eNotesProvider.eNotesProvider.Repository.PurchesedRepo;
import eNotesProvider.eNotesProvider.Repository.paymentsRepo;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    @Autowired
    private paymentsRepo paymentRepository;
  
     @Autowired
     PaymentsConversion paymentsConversion;

     @Autowired
     PurchesedConversion purchesedConversion;

     @Autowired
        PurchesedRepo purchesedRepo;

 
    // Save new payment
    public paymentsDto savePayment(paymentsDto dto) {
        Payments payment = paymentsConversion.toPayments(dto);
        payment.setCreatedAt(LocalDateTime.now()); // ensure created time
        Payments saved = paymentRepository.save(payment);
        return paymentsConversion.toPaymentsDto(saved);
    }

    // Get payment by ID
 

    // Get all payments
    public List<paymentsDto> getAllPayments() {
        List<Payments> notes = paymentRepository.findAll(); // fetch all notes from DB
        return notes.stream()
                .map(paymentsConversion::toPaymentsDto)
                .collect(Collectors.toList());
    }
    // Update payment status
@Transactional
public paymentsDto updateStatusByNoteId(Long noteId, String stat) {
    Payments payment = paymentRepository.findByNoteId(noteId);
    if (payment == null) throw new RuntimeException("Payment not found");

    // only update status
    System.out.println(payment);
    payment.setStatus(stat);
  //  Payments updatedPayment = paymentRepository.save(payment);
   // paymentRepository.flush();

    return  paymentsConversion.toPaymentsDto(payment);
}

/*     // Delete payment
@Transactional
public void deletePayment(Long id) {
    if (paymentRepository.existsByNoteId(id)) {
        paymentRepository.deleteByNoteId(id);
    } else {
        throw new RuntimeException("Payment not found with ID: " + id);
    }
} */
@Transactional
public void deletePayment(Long id) {
    if (paymentRepository.existsById(id)) {
        paymentRepository.deleteById(id);
    } else {
        throw new RuntimeException("Payment not found with ID: " + id);
    }
}



    // Find payment by noteId
    public Payments findByPayment(Long noteId) {
      //  Long id=(long) 1;
        Payments payment = paymentRepository.findByNoteId(noteId);
        if (payment == null) {
            throw new RuntimeException("Payment with Note ID " + noteId + " not found");
        }
        return payment;
    }

        public List<PurchesedDto> findAlldata() {
        List<PurchesedUser> purchasedUsers = purchesedRepo.findAll();
        return purchasedUsers.stream()
                .map(purchesedConversion::toPurchesedDto)
                .collect(Collectors.toList());
    }
      @Transactional
    public void deletePayment1(Long paymentId) {
        // Optional: Check if payment exists before deleting
        PurchesedUser payment = purchesedRepo.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + paymentId));
        purchesedRepo.deleteById(paymentId);
    }
    // Optional: Update payment status

     @Transactional
public paymentsDto updateStatusByNoteIdAndEmail(Long noteId, String email, String stat) {
    Payments payment = paymentRepository.findByNoteIdAndEmail(noteId, email);
    if (payment == null) {
        throw new RuntimeException("Payment not found for noteId=" + noteId + " and email=" + email);
    }

    payment.setStatus(stat);
    paymentRepository.save(payment);

    return paymentsConversion.toPaymentsDto(payment);
}


}