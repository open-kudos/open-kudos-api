package kudos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by p998dls on 2015.07.28.
 */
@Controller
public class IndexController {

    @RequestMapping(value="/", method = RequestMethod.GET)
    public String index() {
        return "redirect:/index";
    }

    @RequestMapping(value="/index", method = RequestMethod.GET)
    public String showIndex() {
        return "index";
    }
}
