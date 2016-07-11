package kudos.services;


import kudos.model.Challenge;
import kudos.model.User;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by chc on 15.8.4.
 */
@Component
public class EmailService {

    static Properties mailServerProperties;
    static Session getMailSession;
    static MimeMessage generateMailMessage;

    public void generateAndSendEmail(String email, String message, String subject) throws MessagingException {

        mailServerProperties = System.getProperties();
        mailServerProperties.put("mail.smtp.port", "587");
        mailServerProperties.put("mail.smtp.auth", "true");
        mailServerProperties.put("mail.smtp.starttls.enable", "true");

        getMailSession = Session.getDefaultInstance(mailServerProperties, null);
        generateMailMessage = new MimeMessage(getMailSession);
        generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
        generateMailMessage.setSubject(subject);
        generateMailMessage.setContent(message, "text/html");
        Transport transport = getMailSession.getTransport("smtp");

        transport.connect("smtp.gmail.com", "vilniuscckudos@gmail.com", "kudosapp");
        transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
        transport.close();
    }

    public void generateEmailForNewChallenge(User creator, User participant, Challenge challenge) throws MessagingException{
        if (participant.isSubscribing()) {
            String message = "<h2>You got new challenge</h2>"
                    + "<p>You got challenge from " + creator.getFirstName() + " " + creator.getLastName() + "</p>"
                    + "<p>Challenge description: " + checkChallengeDescription(challenge) + ", for " + challenge.getAmount() + " acorns"
                    + "<p>Let " + creator.getFirstName() + " " + creator.getLastName() + " know if you accept the challenge in your Openkudos account!</p>"
                    + "<br><p>If you don't want to receive notifications about new challenges, go to your openkudos account settings and stop your subscription<p>";


            Runnable runnable = () -> {
                try {
                    generateAndSendEmail(participant.getEmail(), message, "You have been challenged!");
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            };

            Thread thread = new Thread(runnable);
            thread.start();
        }
    }

    public String checkChallengeDescription(Challenge challenge){
        if (challenge.getDescription() == null){
            return challenge.getName();
        } else {
            return challenge.getDescription();
        }
    }
}


