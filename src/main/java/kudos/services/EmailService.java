package kudos.services;


import freemarker.template.TemplateException;
import kudos.model.Email;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    private static final String DEFAULT_EMAIL_SUBJECT = "confirmationMail";

    private final Logger LOG = Logger.getLogger(EmailService.class);

    @Value("${email.sender}")
    String sender;
    @Value("${email.smtpHost}")
    String serverHost;
    @Value("${email.smtpPort}")
    String serverPort;

    @Autowired
    TemplatingService templatingService;

    public void send(Email email) throws MessagingException, IOException, TemplateException {

        Properties tMailServerProperties = setupProperties();
        Session tSession = Session.getDefaultInstance(tMailServerProperties, null);
        Message tMsg = createMessage(tSession, sender, email.getRecipientAddress(), /*email.getSubject()*/email.getSubject());
        tMsg.setContent(/*DEFAULT_EMAIL_MESSAGE + email.getMessage()*/templatingService.getHtml("Welcome to kudos App",email.getSubject(),
                 email.getMessage()), "text/html");
        //TODO fix gmail auth Transport.send(tMsg);

        LOG.info(templatingService.getHtml("Welcome to kudos App",email.getSubject(),
                email.getMessage()));

    }

    private Properties setupProperties() {
        Properties props = System.getProperties();
        props.put("mail.smtp.auth", "false");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", serverHost);
        props.put("mail.smtp.port", serverPort);
        return props;
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
