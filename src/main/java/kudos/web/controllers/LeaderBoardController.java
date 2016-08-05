package kudos.web.controllers;

import kudos.model.LeaderBoardItem;
import kudos.web.beans.response.LeaderBoardItemResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/leaderboard")
public class LeaderBoardController extends BaseController {

    @RequestMapping(value = "/receivers", method = RequestMethod.GET)
    public List<LeaderBoardItemResponse> getTopReceivers(@RequestParam(value="periodInDays", required = false)
                                                             Integer periodInDays) {
        if(periodInDays != null) {
            return convert(leaderBoardService.getTopReceivers(periodInDays));
        } else {
            return convert(leaderBoardService.getTopReceiversFromAllTime());
        }
    }

    @RequestMapping(value = "/senders", method = RequestMethod.GET)
    public List<LeaderBoardItemResponse> getTopSenders(@RequestParam(value="periodInDays", required = false)
                                                           Integer periodInDays) {
        if(periodInDays != null) {
            return convert(leaderBoardService.getTopSenders(periodInDays));
        } else {
            return convert(leaderBoardService.getTopSendersFromAllTime());
        }
    }

    public List<LeaderBoardItemResponse> convert(List<LeaderBoardItem> items) {
        return items.stream().map(LeaderBoardItemResponse::new).collect(Collectors.toList());
    }

}
