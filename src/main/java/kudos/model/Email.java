package kudos.model;

public class Email {

    private final String recipientAddress;
    private final String timestamp;
    private final String subject;
    private final String message;

    public Email(String recipientAddress, String timestamp, String subject, String message) {
        this.recipientAddress = recipientAddress;
        this.timestamp = timestamp;
        this.subject = subject;
        this.message = message;
    }

    public String getSubject() {
        return subject;
    }

    public String getRecipientAddress() {
        return recipientAddress;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

}
