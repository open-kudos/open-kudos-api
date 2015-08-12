package kudos.services;

import com.mongodb.MongoException;
import kudos.exceptions.BusinessException;
import kudos.exceptions.KudosExceededException;
import kudos.model.Challenge;
import kudos.model.User;
import kudos.repositories.ChallengeRepository;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Challenge create(User participant, User referee, String name, LocalDate due, int amount)
             throws BusinessException{

        String userEmail = usersService.getLoggedUser().get().getEmail();

        Challenge challenge = new Challenge(userEmail, participant.getEmail(), referee.getEmail(), name, due, null, amount, Challenge.Status.CREATED);

        challenge = challengeRepository.save(challenge);
        kudosService.giveSystemKudos(participant,amount, name);
        return challenge;
    }

    public void accept(Challenge challenge) {
        Challenge correctedChallenge = challengeRepository.findOne(challenge.getId());
        correctedChallenge.setStatus(Challenge.Status.ACCEPTED);
    }

    public void decline(Challenge challenge) {
        Challenge correctedChallenge = challengeRepository.findOne(challenge.getId());
        correctedChallenge.setStatus(Challenge.Status.DECLINED);
    }

    public void accomplish(Challenge challenge) {
        Challenge correctedChallenge = challengeRepository.findOne(challenge.getId());
        correctedChallenge.setStatus(Challenge.Status.ACCOMPLISHED);
    }

    public void fail(Challenge challenge) {
        Challenge correctedChallenge = challengeRepository.findOne(challenge.getId());
        correctedChallenge.setStatus(Challenge.Status.FAILED);
    }

    public List<Challenge>getAllCreatedChallenges(){
        return challengeRepository.findChallengesByCreator(usersService.getLoggedUser().get().getEmail());
    }

    public List<Challenge>getAllParticipatedChallenges(){
        return challengeRepository.findChallengesByParticipant(usersService.getLoggedUser().get().getEmail());
    }

    public List<Challenge>getAllRefferedChallenges(){
        return challengeRepository.findAllChallengesByReferee(usersService.getLoggedUser().get().getEmail());
    }

}
