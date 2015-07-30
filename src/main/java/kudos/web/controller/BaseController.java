package kudos.web.controller;

import kudos.dao.UserDAO;
import kudos.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chc on 15.7.23.
 */
public abstract class BaseController {

    @Autowired
    protected UserDAO userDAO;

    @Autowired
    protected AuthenticationManager authenticationManager;


}