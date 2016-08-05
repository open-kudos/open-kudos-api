package kudos;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDateTime;

public abstract class KudosBusinessStrategy {

    private final int weeklyAmount;

    public KudosBusinessStrategy(int weeklyAmount) {
        this.weeklyAmount = weeklyAmount;
    }

    public int getWeeklyAmount() {
        return weeklyAmount;
    }

    public abstract LocalDateTime getStartTime();

    public static KudosBusinessStrategy createWeeklyStrategy(int weeklyAmount) {
        return new KudosBusinessStrategy(weeklyAmount) {

            @Override
            public LocalDateTime getStartTime() {
                return new LocalDateTime().withDayOfWeek(DateTimeConstants.MONDAY).withHourOfDay(0).withMinuteOfHour(0);
            }
        };
    }

}
