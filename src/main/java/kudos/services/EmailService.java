package kudos.services;

import com.google.common.base.Strings;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.apache.log4j.Logger;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.jasypt.util.text.StrongTextEncryptor;

/**
 * Created by chc on 15.8.4.
 */
public class EmailService {

    public static Logger LOG = Logger.getLogger(EmailService.class.getName());

    public static void sendConfirmationEmail(String userEmail) throws EmailException {

        String message = new StrongPasswordEncryptor().encryptPassword(userEmail);

        String username = System.getProperty("username");
        String password = System.getProperty("password");
        LOG.error(username + " " + password);

        if (!Strings.isNullOrEmpty(username) && !Strings.isNullOrEmpty(password)) {

            Email email = new SimpleEmail();
            email.setDebug(true);
            email.setHostName("10.190.80.50");
            email.setSmtpPort(465);
            email.setFrom("mantas.damijonaitis@swedbank.lt");
            email.setSubject("Kudos app confirmation link");
            email.setMsg("Hi. Paste this link to your browser to complete you registration to kudos app: localhost:8080/confirm-email?" + message);
            email.addTo(userEmail);
            email.send();

        }

    }

}
