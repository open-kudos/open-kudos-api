package kudos.web.controllers;

import kudos.KudosBusinessStrategy;
import kudos.services.*;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;

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
    protected WisdomWallService wisdomWallService;

    @Autowired
    protected EmailService emailService;

    @Autowired
    @Qualifier(value = "DBTimeFormatter")
    protected DateTimeFormatter formatter;

    @Autowired
    protected ShopService shopService;

    @Autowired
    protected AuthenticationService authenticationService;

    @Autowired
    protected LeaderBoardService leaderBoardService;

}
