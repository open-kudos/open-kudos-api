package kudos.web.controllers;

/**
 * Created by vytautassugintas on 21/06/16.
 */
import kudos.model.history.History;
import kudos.web.exceptions.UserException;
import org.jsondoc.core.annotation.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Api(name = "Transactions Controller", description = "Controller for kudos transactions")
@Controller
@RequestMapping("/history")
public class HistoryController extends BaseController {

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public @ResponseBody List<History> historyByUserEmail(String userEmail, int startingIndex, int endingIndex) throws UserException {
        return historyService.getPageableUserHistoryByEmail(userEmail, startingIndex, endingIndex);
    }

    @RequestMapping(value = "/received", method = RequestMethod.GET)
    public @ResponseBody List<History> receivedHistoryByUserEmail(String userEmail, int startingIndex, int endingIndex) throws UserException {
        return historyService.getPageableUserReceivedHistoryByEmail(userEmail, startingIndex, endingIndex);
    }

    @RequestMapping(value = "/gave", method = RequestMethod.GET)
    public @ResponseBody List<History> gaveHistoryByUserEmail(String userEmail, int startingIndex, int endingIndex) throws UserException {
        return historyService.getPageableUserGivenHistoryByEmail(userEmail, startingIndex, endingIndex);
    }

    @RequestMapping(value = "/challenges", method = RequestMethod.GET)
    public @ResponseBody List<History> challengesHistoryByUserEmail(String userEmail, int startingIndex, int endingIndex) throws UserException {
        return historyService.getPageableUserAllChallengesHistoryByEmail(userEmail, startingIndex, endingIndex);
    }

}
