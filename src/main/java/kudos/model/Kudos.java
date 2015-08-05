package kudos.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by chc on 15.8.5.
 */
@Document
public class Kudos {

    public enum KudosType{
        MINIMUM(5, "Thank you!"),
        NORMAL(10, "Great work!"),
        MAXIMUM(15, "You are my lifesaver!");

        public final int amount;
        public final String kudosMessage;
        KudosType(int i, String s) {
            amount = i;
            kudosMessage = s;
        }
    }

    @Id
    private String id;

    private String sender;
    private String receiver;
    private String message;
    private KudosType kudosType;
    private String timestamp;

    // WHERE timestamp > NOW-2days AND kudosType === 'KIND' GROUP BY receiver ORDER BY sum(kudosAmount) DESC LIMIT 0,1

    public Kudos(String receiver, KudosType kudosType, String message){
        this.kudosType = kudosType;
        this.message = message;
        this.timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS").format(new Date());
    }

    public KudosType getKudosType() {
        return kudosType;
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }

}
