package kudos.web.model.specificResponse;

import kudos.web.model.mainResponse.Response;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.joda.time.Period;

/**
 * Created by chc on 15.8.7.
 */
public class StatusResponse extends Response {

    private String nextRefill;
    private String amount;


    public StatusResponse(String amount, String nextRefill) {
        this.amount = amount;
        this.nextRefill = nextRefill;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getNextRefill() {
        return nextRefill;
    }

    public void setNextRefill(String nextRefill) {
        this.nextRefill = nextRefill;
    }

    public static Response showKudosStatus(String amount){
        LocalDate date = new LocalDate(System.currentTimeMillis());
        Period period = Period.fieldDifference(date, date.withDayOfWeek(DateTimeConstants.MONDAY));
        int days = period.getDays();
        if (days < 1) {
            days = days + 7;
        }
        return new StatusResponse("Currently you have "+amount+" kudos.","Next refill in: "+date.plusDays(days));
    }

}
