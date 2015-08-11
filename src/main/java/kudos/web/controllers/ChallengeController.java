package kudos.web.controllers;

import kudos.exceptions.KudosExceededException;
import kudos.web.beans.form.ChallengeTransferForm;
import kudos.model.Challenge;
import kudos.model.User;
import kudos.services.ChallengeService;
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

import java.security.Principal;
import java.text.ParseException;

/**
 * Created by chc on 15.8.11.
 */
@RequestMapping("/challenges")
@Controller
public class ChallengeController extends BaseController {


    @Autowired
    private ChallengeService service;


    @RequestMapping(value = "/challenge", method = RequestMethod.POST)
    public ResponseEntity<Response> challenge(@ModelAttribute("form") ChallengeTransferForm form, Errors errors)
            throws FormValidationException, ParseException, KudosExceededException {

        new ChallengeTransferForm.ChallengeTransferFormValidator().validate(form, errors);

        if (errors.hasErrors()) {
            throw new FormValidationException(errors);
        }

        User participant = usersService.findByEmail(form.getReceiverEmail()).get();
        User referee = usersService.findByEmail(form.getJudgeEmail()).get();

        LocalDate due = LocalDate.fromDateFields(getDateFormat().parse(form.getEstimatedDate()));
        int amount = Integer.parseInt(form.getAmount());

        Challenge challenge = service.challenge(participant, referee, form.getChallengeName(), due, amount);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
