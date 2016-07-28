package kudos.services.util;

import kudos.model.Challenge;
import org.joda.time.LocalDateTime;

import java.util.Comparator;

public class DateComparatorDescendingByCreated implements Comparator<Challenge> {

    @Override
    public int compare(Challenge challenge1, Challenge challenge2) {
        LocalDateTime date1 = LocalDateTime.parse(challenge1.getCreatedDate());
        LocalDateTime date2 = LocalDateTime.parse(challenge2.getCreatedDate());
        return date2.compareTo(date1);
    }
}
