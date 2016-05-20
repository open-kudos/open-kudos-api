package kudos.services;


import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by chc on 15.8.4.
 */
@Component
public class EmailService {

   /* private static final String DEFAULT_EMAIL_SUBJECT = "confirmationMail";

    private final Logger LOG = Logger.getLogger(EmailService.class);

    @Value("${email.sender}")
    String sender;
   // @Value("${email.username}")
   // String emailUsername;
   // @Value("${email.password}")
   // String emailPassword;

    @Autowired
    TemplatingService templatingService;

    public void send(Email email) throws MessagingException, IOException, TemplateException {

        MimeMessage message = new MimeMessage(getSession());
        message.setFrom(sender);
        message.setRecipient(Message.RecipientType.TO,
                new InternetAddress((email.getRecipientAddress())));
        message.setSubject(email.getSubject());
        //message.setText(templatingService.getHtml("Welcome to kudos App", email.getMessage(),
        //        email.getSubject()), "text/html");

        message.setText("Zdarova");

        Transport.send(message);

        LOG.info(templatingService.getHtml("Welcome to kudos App",email.getSubject(), email.getMessage()));
    }

    private Session getSession() {
        Properties props = System.getProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.port","587");
        props.put("mail.smtp.socketFactory.class","javax,net.ssl.SSLSocketFactory");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator(){
                     protected javax.mail.PasswordAuthentication getPasswordAuthentication(){
                        return new javax.mail.PasswordAuthentication("vineripe25@gmail.com","vytautas25");
            }
        });

        return session;
    }

    private MimeMessage createMessage(Session session, String fromAddress, String toAddress, String subject) throws MessagingException {
        MimeMessage tMessage = new MimeMessage(session);
        tMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));
        tMessage.setSender(new InternetAddress(fromAddress));
        tMessage.setSubject(subject);
        tMessage.setSentDate(new Date());
        return tMessage;
    } */

    static Properties mailServerProperties;
    static Session getMailSession;
    static MimeMessage generateMailMessage;

    public static void generateAndSendEmail(String email, String message) throws AddressException, MessagingException {

        mailServerProperties = System.getProperties();
        mailServerProperties.put("mail.smtp.port", "587");
        mailServerProperties.put("mail.smtp.auth", "true");
        mailServerProperties.put("mail.smtp.starttls.enable", "true");
        System.out.println("Mail Server Properties have been setup successfully..");

        getMailSession = Session.getDefaultInstance(mailServerProperties, null);
        generateMailMessage = new MimeMessage(getMailSession);
        generateMailMessage.addRecipient(Message.RecipientType.CC, new InternetAddress(email));
        generateMailMessage.setSubject("Greetings from Acorns app");
        String emailBody = message;
        //String emailBody = "Test email by Crunchify.com JavaMail API example. " + "<br><br> Regards, <br>Crunchify Admin";
        generateMailMessage.setContent(emailBody, "text/html");
        System.out.println("Mail Session has been created successfully..");

        // Step3
        System.out.println("\n\n 3rd ===> Get Session and Send mail");
        Transport transport = getMailSession.getTransport("smtp");

        // Enter your correct gmail UserID and Password
        // if you have 2FA enabled then provide App Specific Password
        transport.connect("smtp.gmail.com", "vilniuscckudos@gmail.com", "kudosapp");
        transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
        transport.close();
    }
}


