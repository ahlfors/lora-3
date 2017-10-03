package yaruliy.email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import yaruliy.datastore.UserService;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Properties;

@Component
public class Sender {
    private static String loginEmail = "lora.service.17@gmail.com";
    private static String passwordEmail = "14111993qw_";
    private static String receiver = "l.yaruk1993@gmail.com";
    private static String hostSMTP = "smtp.gmail.com";
    private Properties properties;
    private UserService userService;
    @Autowired public void setUserService(UserService userService){ this.userService = userService; }

    public Sender(){
        this.properties = System.getProperties();
        properties.setProperty("mail.smtp.user", loginEmail);
        properties.setProperty("mail.smtp.password", passwordEmail);
        properties.setProperty("mail.smtp.protocol", "smtp");
        properties.setProperty("mail.smtp.host", hostSMTP);
        properties.setProperty("mail.smtp.port", "465");
        properties.setProperty("mail.smtp.starttls.enable","false");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.socketFactory.port", "465");
        properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.smtp.socketFactory.fallback", "false");
    }

    public void sendHTMLMessage(String subject, String m){
        Runnable runnable = () -> {
            Session emailSession = Session.getDefaultInstance(properties, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(loginEmail, passwordEmail);
                }
            });
            try {
                List<String> list = userService.findByLogin("login").getEmails();
                InternetAddress[] recipients = new InternetAddress[list.size()];
                for (int i=0; i<list.size(); i++) {
                    recipients[i] = new InternetAddress(list.get(i));
                }

                Message message = new MimeMessage(emailSession);
                message.setFrom(new InternetAddress(loginEmail));
                message.setRecipients(Message.RecipientType.TO, recipients);
                message.setSubject(subject);
                String content = "<!DOCTYPE html>\n" +
                        "<html lang=\"ru\">\n" +
                        "<head>\n" +
                        "    <base target=\"_blank\">\n" +
                        "</head><body>\n" +
                        "\t<div style=\"padding-top: 5px; margin-top: 0;\" id=\"result\">" + m + "</div>\n" +
                        "</body></html>";
                message.setContent(content, "text/html;");
                Transport.send(message);
            }
            catch (Exception ex) { ex.printStackTrace(); }
        };

        runnable.run();
    }
}