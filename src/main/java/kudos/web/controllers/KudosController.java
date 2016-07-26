package kudos.web.controllers;

import kudos.exceptions.InvalidKudosAmountException;
import kudos.exceptions.UserException;
import kudos.model.Transaction;
import kudos.model.User;
import kudos.web.beans.request.GiveKudosForm;
import kudos.web.beans.response.KudosTransactionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/kudos")
public class KudosController extends BaseController {

    @RequestMapping(value = "/give", method = RequestMethod.POST)
    public KudosTransactionResponse giveKudos(@RequestBody GiveKudosForm form) throws UserException,
            InvalidKudosAmountException, MessagingException {

        User sender = authenticationService.getLoggedInUser();
        Optional<User> receiver = usersService.findByEmail(form.getReceiverEmail().toLowerCase());

        if(receiver.isPresent()) {
            return new KudosTransactionResponse(kudosService.giveKudos(sender, receiver.get(), form.getAmount(),
                    form.getMessage()));
        } else {
            String email = sender.getFirstName() + " " + sender.getLastName() + "wanted to give you KUDOS," +
                    " but you are not registered. Maybe it is time to do it? Go to www.openkudos.com and try it!";
            emailService.sendEmail(form.getReceiverEmail().toLowerCase(), email, "Open Kudos");
            throw new UserException("receiver_does_not_exist");
        }
    }

    @RequestMapping(value = "/history/given", method = RequestMethod.GET)
    public Page<KudosTransactionResponse> getGivenKudosHistory(Pageable pageable) throws UserException {
        User user = authenticationService.getLoggedInUser();
        return convert(transactionService.getGivenKudosHistory(user, pageable), "GIVEN");
    }

    @RequestMapping(value = "/history/received", method = RequestMethod.GET)
    public Page<KudosTransactionResponse> getReceivedKudosHistory(Pageable pageable) throws UserException {
        User user = authenticationService.getLoggedInUser();
        return convert(transactionService.getReceivedKudosHistory(user, pageable), "RECEIVED");
    }

//    @RequestMapping(value = "/history", method = RequestMethod.GET)
//    public Page<KudosTransactionResponse> getReceivedKudosHistory(Pageable pageable) throws UserException {
//        User user = authenticationService.getLoggedInUser();
//        return convert(transactionService.getKudosHistory(user, pageable), "RECEIVED");
//    }

    private Page<KudosTransactionResponse> convert(Page<Transaction> input, String type) {
        List<KudosTransactionResponse> transactions = new ArrayList<>();
        for(Transaction transaction : input.getContent()) {
            KudosTransactionResponse trans = new KudosTransactionResponse(transaction);
            trans.setType(type);
            transactions.add(trans);
        }
        return new PageImpl<>(transactions, new PageRequest(input.getNumber(), input.getSize()), input.getTotalElements());
    }

    //    public List<HistoryResponse> sortListByTimestamp(List<HistoryResponse> historyList, int startingIndex, int endingIndex){
//        try {
//            return historyList.stream().sorted((h1, h2) -> h2.getTimestamp().compareTo(h1.getTimestamp())).collect(Collectors.toList()).subList(startingIndex, endingIndex);
//        } catch (IndexOutOfBoundsException e){
//            return historyList.stream().sorted((h1, h2) -> h2.getTimestamp().compareTo(h1.getTimestamp())).collect(Collectors.toList());
//        }
//    }

}
