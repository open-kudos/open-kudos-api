package kudos.web.controller;

import kudos.dao.DAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;

/**
 * Created by chc on 15.7.23.
 */
public abstract class BaseController {

    @Autowired
    protected DAO userDAO;

    @Autowired
    protected AuthenticationManager authenticationManager;


}
