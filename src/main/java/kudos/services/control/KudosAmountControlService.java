package kudos.services.control;

import kudos.dao.repositories.TransactionRepository;
import kudos.model.Transaction;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Created by chc on 15.8.7.
 */
@Service
public class KudosAmountControlService {

    private static int WEEKLY_KUDOS_AMOUNT = 50;

    @Autowired
    TransactionRepository transactionRepository;

    public int howManyKudosUserCanSpend(String senderMail){
        final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss,SSS");
        final LocalDateTime startTime = new LocalDateTime().withDayOfWeek(DateTimeConstants.MONDAY);

        List<Transaction> transactionList = transactionRepository.findTransactionBySenderEmailOrderByTimestampDesc(senderMail);
        int amount = 0;
        if(transactionList.size() > 0) {
            for (int i = 0; i < transactionList.size(); i++) {

                LocalDateTime transactionTime = dateTimeFormatter.parseLocalDateTime(transactionList.get(i).getTimestamp());

                int transactionAmount = transactionList.get(i).getKudosType().amount;

                if (startTime.isBefore(transactionTime) && transactionTime.isAfter(startTime) && amount < WEEKLY_KUDOS_AMOUNT) {
                    amount += transactionAmount;
                } else {
                    return 0;
                }

            }
            return amount;

        } else {
            return WEEKLY_KUDOS_AMOUNT;
        }
    }

    public boolean canUserCanSpendKudos(String senderEmail) {
        if(howManyKudosUserCanSpend(senderEmail) >= Transaction.KudosType.MINIMUM.amount){
            return true;
        } else{
            return false;
        }
    }

}
