package kudos.web.controllers;

import kudos.web.beans.response.HistoryResponse;
import kudos.exceptions.UserException;
import org.jsondoc.core.annotation.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Api(name = "History Controller", description = "Controller for user history")
@Controller
@RequestMapping("/history")
public class HistoryController extends BaseController {

//    @RequestMapping(value = "/all", method = RequestMethod.GET)
//    public @ResponseBody List<HistoryResponse> historyByUserEmail(String userEmail, int startingIndex, int endingIndex) throws UserException {
//        return historyService.getPageableUserHistoryByEmail(userEmail, startingIndex, endingIndex);
//    }
//
//    @RequestMapping(value = "/received", method = RequestMethod.GET)
//    public @ResponseBody List<HistoryResponse> receivedHistoryByUserEmail(String userEmail, int startingIndex, int endingIndex) throws UserException {
//        return historyService.getPageableUserReceivedHistoryByEmail(userEmail, startingIndex, endingIndex);
//    }
//
//    @RequestMapping(value = "/gave", method = RequestMethod.GET)
//    public @ResponseBody List<HistoryResponse> gaveHistoryByUserEmail(String userEmail, int startingIndex, int endingIndex) throws UserException {
//        return historyService.getPageableUserGivenHistoryByEmail(userEmail, startingIndex, endingIndex);
//    }
//
//    @RequestMapping(value = "/challenges", method = RequestMethod.GET)
//    public @ResponseBody List<HistoryResponse> challengesHistoryByUserEmail(String userEmail, int startingIndex, int endingIndex) throws UserException {
//        return historyService.getPageableUserAllChallengesHistoryByEmail(userEmail, startingIndex, endingIndex);
//    }

}
