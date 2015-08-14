package kudos.services;


import freemarker.template.TemplateException;
import kudos.model.Email;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.mail.Transport;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

/**
 * Created by chc on 15.8.4.
 */
@Component
public class EmailService {

    private static final String DEFAULT_MESSAGE_LINK = "http://localhost:8080/confirm-email?id=";

    private static final String DEFAULT_EMAIL_SUBJECT = "confirmationMail";

    private Logger LOG = Logger.getLogger(EmailService.class);

    @Autowired
    TemplatingService templatingService;

    public void send(Email email) throws MessagingException, IOException, TemplateException {

        Properties tMailServerProperties = setupProperties();
        Session tSession = Session.getDefaultInstance(tMailServerProperties, null);
        Message tMsg = createMessage(tSession, System.getProperty("senderEmail"), email.getRecipientAddress(), /*email.getSubject()*/email.getSubject());
        tMsg.setContent(/*DEFAULT_EMAIL_MESSAGE + email.getMessage()*/templatingService.getHtml("Welcome to kudos App",email.getSubject(),
                 email.getMessage()), "text/html");
        Transport.send(tMsg);

        LOG.info(templatingService.getHtml("Welcome to kudos App",email.getSubject(),
                DEFAULT_MESSAGE_LINK + email.getMessage()));

    }

    private static Properties setupProperties() {
        String mailServerPropertiesKey = System.getProperty("mailServerKey");
        String mailServerPropertiesValue = System.getProperty("mailServerValue");
        Properties tMailServerProperties = System.getProperties();
        tMailServerProperties.put(mailServerPropertiesKey,mailServerPropertiesValue);
        return tMailServerProperties;
    }

    private MimeMessage createMessage(Session session, String fromAddress, String toAddress, String subject) throws AddressException, MessagingException {
        MimeMessage tMessage = new MimeMessage(session);
        tMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));
        tMessage.setSender(new InternetAddress(fromAddress));
        tMessage.setSubject(subject);
        tMessage.setSentDate(new Date());

        return tMessage;

    }

}
