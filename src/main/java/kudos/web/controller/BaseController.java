package kudos.web.controller;

import kudos.dao.repositories.TransactionRepository;
import kudos.dao.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;

/**
 * Created by chc on 15.7.23.
 */
public abstract class BaseController {

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected AuthenticationManager authenticationManager;

    @Autowired
    TransactionRepository transactionRepository;

}
