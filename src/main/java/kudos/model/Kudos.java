package kudos.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by chc on 15.8.5.
 */
public class Kudos {

    public enum KudosType{
        MINIMUM,
        NORMAL,
        MAXIMUM
    }

    private enum TransformType{
        SEND,
        RECEIVED;
    }

    private static final String MINIMUM_KUDOS_MESSAGE = "Thank you!";
    private static final String NORMAL_KUDOS_MESSAGE = "Great work!";
    private static final String MAXIMUM_KUDOS_MESSAGE = "You are my lifesaver!";
    private static final int MINIMUM_KUDOS_AMOUNT = 5;
    private static final int NORMAL_KUDOS_AMOUNT = 10;
    private static final int MAXIMUM_KUDOS_AMOUNT = 15;

    private String collegue;
    private String message;
    private String kudosTypeMessage;
    private int amount;
    private String timestamp;

    public Kudos(String collegue, KudosType kudosType, String message){
        this.collegue = collegue;
        this.message = message;
        this.timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS").format(new Date());

        switch(kudosType){
            case MINIMUM:
                this.kudosTypeMessage = MINIMUM_KUDOS_MESSAGE;
                this.amount = MINIMUM_KUDOS_AMOUNT;
            break;

            case NORMAL:
                this.kudosTypeMessage = NORMAL_KUDOS_MESSAGE;
                this.amount = NORMAL_KUDOS_AMOUNT;
            break;

            case MAXIMUM:
                this.kudosTypeMessage = MAXIMUM_KUDOS_MESSAGE;
                this.amount = MAXIMUM_KUDOS_AMOUNT;
            break;
        }

    }

    public String getCollegue() {
        return collegue;
    }

    public String getMessage() {
        return message;
    }

    public String getKudosTypeMessage() {
        return kudosTypeMessage;
    }

    public int getAmount() {
        return amount;
    }

    public String getTimestamp() {
        return timestamp;
    }

}
