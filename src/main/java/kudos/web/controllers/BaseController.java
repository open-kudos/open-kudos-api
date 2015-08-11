package kudos.web.controllers;

import kudos.KudosBusinessStrategy;
import kudos.repositories.TransactionRepository;
import kudos.repositories.UserRepository;
import kudos.services.KudosService;
import kudos.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by chc on 15.7.23.
 */
public abstract class BaseController {

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected AuthenticationManager authenticationManager;

    @Autowired
    protected TransactionRepository transactionRepository;

    @Autowired
    protected UsersService usersService;

    @Autowired
    protected KudosService kudosService;

    @Autowired
    protected KudosBusinessStrategy kudosBusinessStrategy;

    protected DateFormat getDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
    }

}
