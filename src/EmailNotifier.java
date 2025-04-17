import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
/**
 * A) Class name: EmailNotifier
 * B) Date of the Code: March 18 ,2025
 * C) Programmer's name: Alexis Anguiano
 * D) Brief description: Utility class for sending emails using the JavaMail API.
 * E) Brief explanation of important function: sendEmail sends generic email given parameters.
 * sendReservationConfirmation sens detaitled confirmation email. sendCancelConfirmation sends a email after cancellation.
 * F) Important data structures: SMTP setting stored as properties.
 * G) Algorithm used: Stateless static methods abstract JavaMail setup and sending to avoid instance creation.
 */

public class EmailNotifier {
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String EMAIL_USER = System.getenv("EMAIL_USER");
    private static final String EMAIL_PASS = System.getenv("EMAIL_PASS");

    /**
     * Sends an email using SMTP credential (made email for storage unit company)
     * @param recipient email address to send to
     * @param subject subject line
     * @param body text email body
     */

    public static void sendEmail(String recipient, String subject, String body) {
        if (recipient == null || recipient.trim().isEmpty()) {
            System.err.println("Error: Recipient email is null or empty. Email not sent.");
            return;
        }
        if (EMAIL_USER == null || EMAIL_PASS == null) {
            System.err.println("Error: Email credentials are missing. Check environment variables.");
            return;
        }
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_USER, EMAIL_PASS);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_USER));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            System.out.println("Email sent to: " + recipient);
        } catch (MessagingException e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }

    /**
     * Sends reservation confirmation email with rental info.
     *
     * @param recipient email of customer
     * @param unitId    storage unit ID
     * @param startDate rental start date
     */
    public static void sendReservationConfirmation(String recipient, int unitId, Date startDate) {
        var details = MySQL.getStorageInformation(unitId);
        if (details.isEmpty()) {
            System.err.println("Failed to fetch unit info.");
            return;
        }
        int pricePerMonth = (Integer) details.get(2);
        String size = (String) details.get(1);
        String location = (String) details.get(3);

        SimpleDateFormat fmt = new SimpleDateFormat("MM-dd-yyyy");

        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        cal.add(Calendar.DATE, 30);
        Date billingEndDate = cal.getTime();

        StringBuilder body = new StringBuilder();
        body.append("Reservation Confirmed!\n\n")
                .append("Storage Unit ID: ").append(unitId).append("\n")
                .append("Size: ").append(size).append("\n")
                .append("Price per Month: $").append(pricePerMonth).append("\n")
                .append("Location: ").append(location).append("\n\n")
                .append("Start Date: ").append(fmt.format(startDate)).append("\n");

        body.append("\nBilling Statement:\n")
                .append("Rental Period: ").append(fmt.format(startDate))
                .append(" - ").append(fmt.format(billingEndDate)).append("\n")
                .append("Total for this period: $").append(pricePerMonth).append("\n\n")
                .append("Thank you for choosing us!");

        sendEmail(recipient, "Storage Reservation Confirmed", body.toString());
    }

    /**
     * Sends cancelation confirmation email
     * @param recipient email of customer
     * @param unitId storage ID
     */
    public static void sendCancellationConfirmation(String recipient, int unitId) {
        var details = MySQL.getStorageInformation(unitId);
        if (details.isEmpty()) {
            System.err.println("Failed to fetch unit info.");
            return;
        }
        String size = (String) details.get(1);
        int price = (Integer) details.get(2);
        String location = (String) details.get(3);

        String body = String.format(
                "Reservation Cancelled.\n\n" +
                        "Storage Unit ID: %d\n" +
                        "Size: %s\n" +
                        "Price per Month: $%d\n" +
                        "Location: %s\n\n" +
                        "We hope to serve you again!",
                unitId, size, price, location
        );

        sendEmail(recipient, "Storage Reservation Cancelled", body);
    }
}
