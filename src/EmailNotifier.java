import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailNotifier {
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String EMAIL_USER = System.getenv("EMAIL_USER");
    private static final String EMAIL_PASS = System.getenv("EMAIL_PASS");

    public static void sendEmail(String recipient, String subject, String body) {
        if (recipient == null || recipient.trim().isEmpty()) {
            System.err.println(" Error: Recipient email is null or empty. Email not sent.");
            return;
        }

        if (EMAIL_USER == null || EMAIL_PASS == null) {
            System.err.println(" Error: Email credentials are missing. Check environment variables.");
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
            System.out.println(" Email sent to: " + recipient);
        } catch (MessagingException e) {
            System.err.println(" Failed to send email: " + e.getMessage());
        }
    }
}
