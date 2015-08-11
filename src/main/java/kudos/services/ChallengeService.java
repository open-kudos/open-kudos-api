package kudos.services;

import kudos.exceptions.KudosExceededException;
import kudos.model.Challenge;
import kudos.model.Transaction;
import kudos.model.User;
import kudos.repositories.ChallengeRepository;
import kudos.repositories.UserRepository;
import kudos.web.beans.response.Response;
import kudos.web.beans.response.ChallengeResponse;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 *
 * Created by chc on 15.8.7.
 */
@Service
public class ChallengeService {

    private ChallengeRepository challengeRepository;
    private KudosService kudosService;
    private UsersService usersService;

    @Autowired
    public ChallengeService(ChallengeRepository challengeRepository, KudosService kudosService, UsersService usersService){
        this.challengeRepository = challengeRepository;
        this.kudosService = kudosService;
        this.usersService = usersService;
    }

    public Challenge challenge(User participant, User referee, String name, LocalDate due, int amount)
            throws KudosExceededException {

        String userEmail = usersService.getLoggedUser().get().getEmail();
        if(!kudosService.canUserCanSpendKudos(userEmail)){
            throw new KudosExceededException();
        }

        return challengeRepository.save(new Challenge(userEmail, participant.getEmail(),referee.getEmail(),name,due,amount));

    }

}
