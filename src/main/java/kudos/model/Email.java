package kudos.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by chc on 15.8.4.
 */
@Document
public class Email {
    @Id
    private String id;

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
}
