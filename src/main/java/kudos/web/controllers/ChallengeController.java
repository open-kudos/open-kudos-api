package kudos.web.controllers;

import com.google.common.base.Optional;
import kudos.exceptions.BusinessException;
import kudos.web.beans.form.ChallengeTransferForm;
import kudos.model.Challenge;
import kudos.model.User;
import kudos.services.ChallengeService;
import kudos.web.beans.response.ChallengeHistoryResponse;
import kudos.web.beans.response.ChallengeResponse;
import kudos.web.beans.response.SingleErrorResponse;
import kudos.web.exceptions.FormValidationException;
import kudos.web.beans.response.Response;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.text.ParseException;
import java.util.List;

/**
 * Created by chc on 15.8.11.
 */
@RequestMapping("/challenges")
@Controller
public class ChallengeController extends BaseController {


    @Autowired
    private ChallengeService service;


    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<Response> challenge(@ModelAttribute("form") ChallengeTransferForm form, Errors errors)
            throws FormValidationException, ParseException, BusinessException {

        new ChallengeTransferForm.ChallengeTransferFormValidator().validate(form, errors);

        if (errors.hasErrors()) {
            throw new FormValidationException(errors);
        }

        Optional<User> participant = usersService.findByEmail(form.getParticipant());
        Optional<User> referee = usersService.findByEmail(form.getReferee());

        if(!participant.isPresent()) {
            return new ResponseEntity<>(new SingleErrorResponse("participant.not.exist"),HttpStatus.BAD_REQUEST);
        }

        if(!referee.isPresent()){
            return new ResponseEntity<>(new SingleErrorResponse("referee.not.exist"),HttpStatus.BAD_REQUEST);
        }

        LocalDate due = LocalDate.fromDateFields(getDateFormat().parse(form.getFinishDate()));
        int amount = Integer.parseInt(form.getAmount());

        Challenge challenge = service.create(participant.get(), referee.get(), form.getName(), due, amount);

        return new ResponseEntity<>(new ChallengeResponse(challenge),HttpStatus.OK);
    }

    @RequestMapping("/show-created")
    public ResponseEntity<Response> showCreatedChallenges(){
        return new ResponseEntity<>(new ChallengeHistoryResponse(service.getAllCreatedChallenges()),HttpStatus.OK);
    }

    @RequestMapping("/show-reffered")
    public ResponseEntity<Response>showRefferedChallenges(){
        return new ResponseEntity<>(new ChallengeHistoryResponse(service.getAllRefferedChallenges()),HttpStatus.OK);
    }

    @RequestMapping("show-participated")
    public ResponseEntity<Response>showParticipatedChallenges(){
        return new ResponseEntity<>(new ChallengeHistoryResponse(service.getAllParticipatedChallenges()),HttpStatus.OK);
    }

}
