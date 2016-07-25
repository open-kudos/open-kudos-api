package kudos.web.controllers;

import kudos.exceptions.UserException;
import kudos.model.Idea;
import kudos.model.User;
import kudos.web.beans.request.AddIdeaForm;
import kudos.web.beans.response.IdeaResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wisdomwall")
public class WisdomWallController extends BaseController {

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public void addIdea(AddIdeaForm addIdeaForm) throws UserException {
        User creator = authenticationService.getLoggedInUser();
        wisdomWallService.addIdea(creator, addIdeaForm.getAuthor(), addIdeaForm.getPhrase());
    }

    @RequestMapping(value = "/randomIdea", method = RequestMethod.GET)
    public IdeaResponse getRandomIdea() throws UserException {
        User creator = authenticationService.getLoggedInUser();
        Idea idea = wisdomWallService.getRandomIdea(creator);
        return new IdeaResponse(idea.getCreator().getId(), idea.getAuthor(), idea.getPhrase());
    }

}
