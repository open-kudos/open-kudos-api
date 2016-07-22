package kudos.web.controllers;

import kudos.exceptions.UserException;
import kudos.model.LeaderBoardItem;
import kudos.web.beans.response.LeaderBoardItemResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/leaderboard")
public class LeaderBoardController extends BaseController {

    @RequestMapping(value = "/receivers", method = RequestMethod.GET)
    public List<LeaderBoardItemResponse> getTopReceivers(@RequestParam(value="periodInDays") int periodInDays) {
        return convert(leaderBoardService.getTopReceivers(periodInDays));
    }

    @RequestMapping(value = "/senders", method = RequestMethod.GET)
    public List<LeaderBoardItemResponse> getTopSenders(@RequestParam(value="periodInDays") int periodInDays) {
        return convert(leaderBoardService.getTopSenders(periodInDays));
    }

    public List<LeaderBoardItemResponse> convert(List<LeaderBoardItem> items) {
        List<LeaderBoardItemResponse> response = new ArrayList<>();
        for(LeaderBoardItem item : items) {
            response.add(new LeaderBoardItemResponse(item.getFullName(), item.getUserId(), item.getKudosAmount()));
        }
        return response;
    }



}
