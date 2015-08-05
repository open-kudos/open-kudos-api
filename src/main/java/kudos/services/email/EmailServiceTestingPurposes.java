package kudos.services.email;


import kudos.model.Email;
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
public class EmailServiceTestingPurposes {

    public static void send(Email email) throws MessagingException {

        Properties tMailServerProperties = setupProperties();
        Session tSession = Session.getDefaultInstance(tMailServerProperties, null);
        Message tMsg = createMessage(tSession, email.getRecipientAddress(), email.getSubject());
        tMsg.setContent(email.getMessage(),"text/html");
        Transport.send(tMsg);

    }

    private static Properties setupProperties() {
        String mailServerPropertiesKey = System.getProperty("mailServerKey");
        String mailServerPropertiesValue = System.getProperty("mailServerValue");
        Properties tMailServerProperties = System.getProperties();
        tMailServerProperties.put(mailServerPropertiesKey,mailServerPropertiesValue);
        return tMailServerProperties;
    }

    private static MimeMessage createMessage(Session aSession, String aToAddress, String aSubject) throws AddressException, MessagingException {
        MimeMessage tMessage = new MimeMessage(aSession);
        tMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(aToAddress));
        tMessage.setSubject(aSubject);
        tMessage.setSentDate(new Date());

        return tMessage;

    }

}
