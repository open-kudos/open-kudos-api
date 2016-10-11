package kudos.services;


import org.springframework.stereotype.Component;

@Component
public class EmailService {

//    public void sendEmail(String recipient, String message, String subject) throws MessagingException {
//        Properties mailServerProperties = System.getProperties();
//        mailServerProperties.put("mail.smtp.port", "587");
//        mailServerProperties.put("mail.smtp.auth", "true");
//        mailServerProperties.put("mail.smtp.starttls.enable", "true");
//
//        Session session = Session.getDefaultInstance(mailServerProperties, null);
//        MimeMessage email = generateEmail(recipient, message, subject, session);
//        Transport transport = session.getTransport("smtp");
//
//        transport.connect("smtp.gmail.com", "vilniuscckudos@gmail.com", "kudosapp");
//        transport.sendMessage(email, email.getAllRecipients());
//        transport.close();
//    }
//
//    public MimeMessage generateEmail(String recipient, String message, String subject, Session session) throws MessagingException {
//        MimeMessage email = new MimeMessage(session);
//        email.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
//        email.setSubject(subject);
//        email.setContent(message, "text/html");
//        return email;
//    }
//
//    public void sendEmailForNewChallenge(User creator, User participant, Challenge challenge) throws MessagingException{
//        String message = "<h2>You got new challenge</h2>"
//                + "<p>You got challenge from " + creator.getFirstName() + " " + creator.getLastName() + "</p>"
//                + "<p>Challenge description: " + checkChallengeDescription(challenge) + ", for "
//                + challenge.getTransaction().getAmount() + " acorns" + "<p>Let " + creator.getFirstName() + " "
//                + creator.getLastName() + " know if you accept the challenge in your OPEN KUDOS account!</p>";
//
//        Runnable runnable = () -> {
//            try {
//                sendEmail(participant.getEmail(), message, "You have been challenged!");
//            } catch (MessagingException e) {
//                e.printStackTrace();
//            }
//        };
//
//        Thread thread = new Thread(runnable);
//        thread.start();
//    }
//
//    public void sendEmailOnNewThread(String recipient, String message, String subject){
//        Runnable runnable = () -> {
//            try {
//                sendEmail(recipient, message, subject);
//            } catch (MessagingException e) {
//                e.printStackTrace();
//            }
//        };
//
//        Thread thread = new Thread(runnable);
//        thread.start();
//    }
//
//    public String checkChallengeDescription(Challenge challenge){
//        if (challenge.getDescription() == null){
//            return challenge.getName();
//        } else {
//            return challenge.getDescription();
//        }
//    }
}


