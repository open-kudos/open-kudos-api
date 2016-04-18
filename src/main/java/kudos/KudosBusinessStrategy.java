package kudos;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDateTime;

/**
 * Created by chc on 15.8.11.
 */
public abstract class KudosBusinessStrategy {

    private final int deposit;
    private final int minDeposit;

    public KudosBusinessStrategy(int deposit, int minDeposit) {
        this.deposit = deposit;
        this.minDeposit = minDeposit;
    }

    public int getDeposit() {
        return deposit;
    }

    public int getMinDeposit() {
        return minDeposit;
    }

    public abstract LocalDateTime getStartTime();

    public static KudosBusinessStrategy createWeeklyStrategy(int deposit, int minDeposit) {
        return new KudosBusinessStrategy(deposit, minDeposit) {

            @Override
            public LocalDateTime getStartTime() {
                return new LocalDateTime().withDayOfWeek(DateTimeConstants.MONDAY).withHourOfDay(0).withMinuteOfHour(0);
            }
        };
    }

    public static KudosBusinessStrategy createMonthlyStrategy(int deposit, int minDeposit) {
        return new KudosBusinessStrategy(deposit, minDeposit) {

            @Override
            public LocalDateTime getStartTime() {
                return new LocalDateTime().withDayOfMonth(1);
            }
        };
    }

}
