package kudos.web.controllers;

import kudos.exceptions.FormValidationException;
import kudos.exceptions.UserException;
import kudos.model.ActionType;
import kudos.model.Idea;
import kudos.model.User;
import kudos.web.beans.request.AddIdeaForm;
import kudos.web.beans.request.validator.AddIdeaFormValidator;
import kudos.web.beans.response.IdeaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wisdomwall")
public class WisdomWallController extends BaseController {

    @Autowired
    AddIdeaFormValidator addIdeaFormValidator;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public void addIdea(@RequestBody AddIdeaForm form, BindingResult errors) throws UserException, FormValidationException {
        addIdeaFormValidator.validate(form, errors);
        if(errors.hasErrors())
            throw new FormValidationException(errors);

        User creator = authenticationService.getLoggedInUser();
        actionsService.save(creator, wisdomWallService.addIdea(creator, form.getAuthor(), form.getPhrase()), ActionType.ADDED_NEW_IDEA);
    }

    @RequestMapping(value = "/randomIdea", method = RequestMethod.GET)
    public IdeaResponse getRandomIdea() throws UserException {
        User creator = authenticationService.getLoggedInUser();
        Idea idea = wisdomWallService.getRandomIdea(creator);
        return new IdeaResponse(idea.getCreator().getId(), idea.getAuthor(), idea.getPhrase());
    }

}
