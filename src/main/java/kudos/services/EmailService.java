package kudos.services;


import kudos.model.Email;
import org.springframework.stereotype.Component;

import javax.mail.Transport;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

/**
 * Created by chc on 15.8.4.
 */
@Component
public class EmailService {

    private static final String DEFAULT_EMAIL_MESSAGE = "Welcome to KUDOS app. To verify your email, please paste this" +
            " link to your browser: localhost:8080/confirm-email?hashedMail=";

    private static final String DEFAULT_EMAIL_SUBJECT = "confirmationMail";

    public static void send(Email email) throws MessagingException {

        Properties tMailServerProperties = setupProperties();
        Session tSession = Session.getDefaultInstance(tMailServerProperties, null);
        Message tMsg = createMessage(tSession, System.getProperty("senderEmail"), email.getRecipientAddress(), /*email.getSubject()*/DEFAULT_EMAIL_SUBJECT);
        tMsg.setContent(DEFAULT_EMAIL_MESSAGE + email.getMessage(),"text/html");
        Transport.send(tMsg);

    }

    private static Properties setupProperties() {
        String mailServerPropertiesKey = System.getProperty("mailServerKey");
        String mailServerPropertiesValue = System.getProperty("mailServerValue");
        Properties tMailServerProperties = System.getProperties();
        tMailServerProperties.put(mailServerPropertiesKey,mailServerPropertiesValue);
        return tMailServerProperties;
    }

    private static MimeMessage createMessage(Session session, String fromAddress, String toAddress, String subject) throws AddressException, MessagingException {
        MimeMessage tMessage = new MimeMessage(session);
        tMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));
        tMessage.setSender(new InternetAddress(fromAddress));
        tMessage.setSubject(subject);
        tMessage.setSentDate(new Date());

        return tMessage;

    }

}
