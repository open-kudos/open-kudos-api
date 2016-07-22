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

@Component
public class EmailService {

    public void sendEmail(String recipient, String message, String subject) throws MessagingException {
        Properties mailServerProperties = System.getProperties();
        mailServerProperties.put("mail.smtp.port", "587");
        mailServerProperties.put("mail.smtp.auth", "true");
        mailServerProperties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getDefaultInstance(mailServerProperties, null);
        MimeMessage email = generateEmail(recipient, message, subject, session);
        Transport transport = session.getTransport("smtp");

        transport.connect("smtp.gmail.com", "vilniuscckudos@gmail.com", "kudosapp");
        transport.sendMessage(email, email.getAllRecipients());
        transport.close();
    }

    public MimeMessage generateEmail(String recipient, String message, String subject, Session session) throws MessagingException {
        MimeMessage email = new MimeMessage(session);
        email.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
        email.setSubject(subject);
        email.setContent(message, "text/html");
        return email;
    }

    public void sendEmailForNewChallenge(User creator, User participant, Challenge challenge) throws MessagingException{
        String message = "<h2>You got new challenge</h2>"
                + "<p>You got challenge from " + creator.getFirstName() + " " + creator.getLastName() + "</p>"
                + "<p>Challenge description: " + checkChallengeDescription(challenge) + ", for "
                + challenge.getTransaction().getAmount() + " acorns" + "<p>Let " + creator.getFirstName() + " "
                + creator.getLastName() + " know if you accept the challenge in your OPEN KUDOS account!</p>";


        Runnable runnable = () -> {
            try {
                sendEmail(participant.getEmail(), message, "You have been challenged!");
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }
//
//    public void generateEmailForOngoingChallengeSelection(Challenge challenge) throws MessagingException{
//        String text;
//
//        if (challenge.getParticipantStatus() == null) {
//            if (challenge.getParticipant().isSubscribing()) {
//                if (challenge.getCreatorStatus()) text = "WON";
//                else text = "LOST";
//
//                String message = "<h2>Challenge updated!</h2>"
//                        + "<p>" + challenge.getCreator().getFirstName() + " " + challenge.getCreator().getLastName() + " selected that HE " + text + " the challenge in " + challenge.getName() + "</p>"
//                        + "<p>Challenge description: " + checkChallengeDescription(challenge) + ", for " + challenge.getAmount() + " acorns"
//                        + "<p>It is the time to you decide who won this challenge in your Openkudos account!</p>"
//                        + "<br><p>If you don't want to receive notifications about new challenges, go to your openkudos account settings and stop your subscription<p>";
//
//
//                Runnable runnable = () -> {
//                    try {
//                        generateAndSendEmail(challenge.getParticipant().getEmail(), message, "Challenge updated!");
//                    } catch (MessagingException e) {
//                        e.printStackTrace();
//                    }
//                };
//
//                Thread thread = new Thread(runnable);
//                thread.start();
//            }
//        } else if (challenge.getCreatorStatus() == null) {
//            if (challenge.getCreator().isSubscribing()){
//                if (challenge.getParticipantStatus()) text = "WON";
//                else text = "LOST";
//
//                String message = "<h2>Challenge updated!</h2>"
//                        + "<p>" + challenge.getParticipant().getFirstName() + " " + challenge.getParticipant().getLastName() + " selected that HE " + text + " the challenge in " + challenge.getName() + "</p>"
//                        + "<p>Challenge description: " + checkChallengeDescription(challenge) + ", for " + challenge.getAmount() + " acorns"
//                        + "<p>It is the time to you decide who won this challenge in your Openkudos account!</p>"
//                        + "<br><p>If you don't want to receive notifications about new challenges, go to your openkudos account settings and stop your subscription<p>";
//
//
//                Runnable runnable = () -> {
//                    try {
//                        generateAndSendEmail(challenge.getCreator().getEmail(), message, "Challenge updated!");
//                    } catch (MessagingException e) {
//                        e.printStackTrace();
//                    }
//                };
//
//                Thread thread = new Thread(runnable);
//                thread.start();
//            }
//        }
//    }
//
    public String checkChallengeDescription(Challenge challenge){
        if (challenge.getDescription() == null){
            return challenge.getName();
        } else {
            return challenge.getDescription();
        }
    }
}


