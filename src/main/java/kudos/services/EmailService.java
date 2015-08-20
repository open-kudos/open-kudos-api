package kudos.services;


import freemarker.template.TemplateException;
import kudos.model.Email;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.net.PasswordAuthentication;
import java.util.Date;
import java.util.Properties;

/**
 * Created by chc on 15.8.4.
 */
@Component
public class EmailService {

    private static final String DEFAULT_EMAIL_SUBJECT = "confirmationMail";

    private final Logger LOG = Logger.getLogger(EmailService.class);

    @Value("${email.sender}")
    String sender;
    @Value("${email.username}")
    String emailUsername;
    @Value("${email.password}")
    String emailPassword;

    @Autowired
    TemplatingService templatingService;

    public void send(Email email) throws MessagingException, IOException, TemplateException {

        MimeMessage message = new MimeMessage(getSession());
        message.setFrom(sender);
        message.setRecipient(Message.RecipientType.TO,
                new InternetAddress((email.getRecipientAddress())));
        message.setSubject(email.getSubject());
        message.setText(templatingService.getHtml("Welcome to kudos App", email.getMessage(),
                email.getSubject()), "text/html");
        Transport.send(message);

        LOG.info(templatingService.getHtml("Welcome to kudos App",email.getSubject(), email.getMessage()));
    }

    private Session getSession() {
        Properties props = System.getProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.port","465");
        props.put("mail.smtp.socketFactory.class","javax,net.ssl.SSLSocketFactory");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator(){
                     protected javax.mail.PasswordAuthentication getPasswordAuthentication(){
                        return new javax.mail.PasswordAuthentication(emailUsername,emailPassword);
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
    }

}
