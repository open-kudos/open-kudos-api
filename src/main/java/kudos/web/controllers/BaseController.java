package kudos.web.controllers;

import kudos.KudosBusinessStrategy;
import kudos.services.ChallengeService;
import kudos.services.KudosService;
import kudos.services.RelationService;
import kudos.services.TransactionService;
import kudos.services.UsersService;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;


/**
 * Created by chc on 15.7.23.
 */
public abstract class BaseController {

    @Autowired
    protected AuthenticationManager authenticationManager;

    @Autowired
    protected UsersService usersService;

    @Autowired
    protected KudosService kudosService;

    @Autowired
    protected TransactionService transactionService;

    @Autowired
    protected KudosBusinessStrategy kudosBusinessStrategy;

    @Autowired
    protected ChallengeService challengeService;

    @Autowired
    protected RelationService relationService;

    @Autowired
    @Qualifier(value = "DBTimeFormatter")
    protected DateTimeFormatter formatter;

}
