package eNotesProvider.eNotesProvider.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import eNotesProvider.eNotesProvider.Dto.paymentsDto;

import java.util.*;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // Store payment verification (txnId -> status)
    private final Map<String, String> paymentVerification = new HashMap<>();

    // Step 1: Send Email to User (Pending)
    @Async
    public void sendUserPendingEmail(paymentsDto payment) {
        String status = "Pending";
        paymentVerification.put(payment.getTransactionId(), status);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(payment.getEmail());
        message.setSubject("📩 Payment Request Received – StudyWithSumit");



        
        String body = "Welcome to StudyWithSumit Platform – This is a Trusted Platform !\n\n"
                + "Dear " + payment.getName() + ",\n\n"
                + "We have received your payment request. Our admin will cross-check your transaction and confirm soon.\n\n"
                + "Payment Details:\n"
                + "Name: " + payment.getName() + "\n"
                + "Email: " + payment.getEmail() + "\n"
                // + "Phone: " + payment.getPhone() + "\n"
                + "Amount: ₹" + payment.getAmount() + "\n"
                + "Transaction ID: " + payment.getTransactionId() + "\n"
                + "Notes: " + payment.getNoteTitle() + "\n\n"
                + "✅ Please wait while we verify your payment.\n";

        message.setText(body);
        mailSender.send(message);
    }

    // Step 2: Send Email to Admin for Verification
    @Async
    public void sendAdminVerificationEmail(paymentsDto payment, String adminEmail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(adminEmail);
        message.setSubject("🔔 New Payment Pending Verification");

        String body = "hello Sumit Rathod A new payment has been submitted and requires verification.\n\n"
                + "Payment Details:\n"
                + "Name: " + payment.getName() + "\n"
                + "Email: " + payment.getEmail() + "\n"
                //   + "Phone: " + payment.getPhone() + "\n"
                + "Amount: ₹" + payment.getAmount() + "\n"
                + "Transaction ID: " + payment.getTransactionId() + "\n"
                + "Notes: " + payment.getNoteTitle() + "\n\n"
                + "⚠ Status: Pending\n\n"
                + "Please cross-check the Transaction ID in your UPI dashboard and approve/reject.";

        message.setText(body);
        mailSender.send(message);
    }

    // Step 3: Approve Payment (Admin action)
    @Async
    public void approvePayment(String txnId, String userEmail) {
        paymentVerification.put(txnId, "Approved");

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(userEmail);
        message.setSubject("✅ Payment Approved – StudyWithSumit");

        String body = "Dear User,\n\n"
                + "Your payment with Transaction ID: " + txnId + " has been successfully verified.\n\n"
                + "You can now access your selected notes.\n\n"
                + "Thank you for trusting StudyWithSumit Platform!";

        message.setText(body);
        mailSender.send(message);
    }

    // Step 4: Reject Payment (if invalid)
    @Async
    public void rejectPayment(String txnId, String userEmail) {
        paymentVerification.put(txnId, "Rejected");

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(userEmail);
        message.setSubject("❌ Payment Rejected – StudyWithSumit");

        String body = "Dear User,\n\n"
                + "Unfortunately, your payment with Transaction ID: " + txnId + " could not be verified.\n"
                + "Please recheck the Transaction ID or contact support.\n\n"
                + "Regards,\nStudyWithSumit Platform";

        message.setText(body);
        mailSender.send(message);
    }
}
