package kudos.web.controllers;

import com.google.common.base.Strings;
import kudos.exceptions.BusinessException;
import kudos.exceptions.ChallengeException;
import kudos.exceptions.IdNotSpecifiedException;
import kudos.exceptions.WrongChallengeEditorException;
import kudos.model.Challenge;
import kudos.model.TeamChallenge;
import kudos.web.beans.form.TeamChallengeTransferForm;
import kudos.web.exceptions.FormValidationException;
import kudos.web.exceptions.UserException;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiResponseObject;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;

@Api(name = "Team Challenge Controller", description = "Controller for managing team challenges")
@RequestMapping("/teamchallenges")
@Controller
public class TeamChallengeController extends BaseController{

    @ApiMethod(description = "Service to create team challenges")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public @ApiResponseObject
    @ResponseBody
    TeamChallenge createTeamChallenge(TeamChallengeTransferForm form, Errors errors) throws UserException, FormValidationException, BusinessException {

        new TeamChallengeTransferForm.TeamChallengeTransferFormValidator().validate(form, errors);

        if (errors.hasErrors())
            throw new FormValidationException(errors);

        return teamChallengeService.createTeamChallenge(
                form.getName(),
                form.getFirstTeam(),
                form.getSecondTeam(),
                form.getDescription(),
                form.getFinishDate(),
                Integer.parseInt(form.getAmount())
        );
    }

    @RequestMapping(value = "/accept", method = RequestMethod.POST)
    public @ApiResponseObject @ResponseBody TeamChallenge accept(String id)
            throws BusinessException, IdNotSpecifiedException, ChallengeException, UserException {

        if(Strings.isNullOrEmpty(id))
            throw new IdNotSpecifiedException("id.not.specified");

        Optional<TeamChallenge> maybeChallenge = teamChallengeService.getChallenge(id);
        if(!maybeChallenge.isPresent()){
            throw new ChallengeException("challenge_not_found");
        }

        TeamChallenge teamChallenge = maybeChallenge.get();
        if (!teamChallenge.getSecondTeam().containsKey(usersService.getLoggedUser().get().getEmail())) {
            throw new WrongChallengeEditorException("not_a_participant");
        }

        return teamChallengeService.accept(teamChallenge);
    }


}
