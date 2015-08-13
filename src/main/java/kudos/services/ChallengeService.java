package kudos.services;

import com.mongodb.MongoException;
import kudos.exceptions.BusinessException;
import kudos.exceptions.InvalidChallengeStatusException;
import kudos.exceptions.KudosExceededException;
import kudos.model.Challenge;
import kudos.model.User;
import kudos.repositories.ChallengeRepository;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
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
    @Qualifier(value = "DBTimeFormatter")
    DateTimeFormatter dateTimeFormatter;

    @Autowired
    public ChallengeService(ChallengeRepository challengeRepository, KudosService kudosService, UsersService usersService){
        this.challengeRepository = challengeRepository;
        this.kudosService = kudosService;
        this.usersService = usersService;
    }

    public Challenge create(User participant, User referee, String name, LocalDateTime finishDate, int amount)
             throws BusinessException{

        String userEmail = usersService.getLoggedUser().get().getEmail();

        LocalDateTime now = LocalDateTime.now();
        Challenge challenge = new Challenge(userEmail, participant.getEmail(), referee.getEmail(), name, now.toString(dateTimeFormatter), finishDate.toString(dateTimeFormatter), amount, Challenge.Status.CREATED);
        kudosService.reduceFreeKudos(usersService.getLoggedUser().get(), amount, name);
        challenge = challengeRepository.save(challenge);
        return challenge;
    }

    public Challenge getChallenge(String id){
        return challengeRepository.findChallengeById(id);
    }

    public Challenge accept(Challenge challenge) throws InvalidChallengeStatusException {
        if(challenge.getStatus().equals(Challenge.Status.ACCOMPLISHED)){
            throw new InvalidChallengeStatusException("challenge.already.accomplished");
        }
        if(challenge.getStatus().equals(Challenge.Status.DECLINED)){
            throw new InvalidChallengeStatusException("challenge.already.declined");
        }
        if(challenge.getStatus().equals(Challenge.Status.FAILED)){
            throw new InvalidChallengeStatusException("challenge.already.failed");
        }
        if(challenge.getStatus().equals(Challenge.Status.ACCEPTED)){
            throw new InvalidChallengeStatusException("challenge.already.accepted");
        }
        Challenge databaseChallenge = getChallenge(challenge.getId());
        databaseChallenge.setStatus(Challenge.Status.ACCEPTED);
        return challengeRepository.save(databaseChallenge);
    }

    public Challenge decline(Challenge challenge) throws BusinessException {
        if(challenge.getStatus().equals(Challenge.Status.ACCOMPLISHED)){
            throw new InvalidChallengeStatusException("challenge.already.accomplished");
        }
        if(challenge.getStatus().equals(Challenge.Status.FAILED)){
            throw new InvalidChallengeStatusException("challenge.already.failed");
        }
        if(challenge.getStatus().equals(Challenge.Status.ACCEPTED)){
            throw new InvalidChallengeStatusException("challenge.already.accepted");
        }
        if(challenge.getStatus().equals(Challenge.Status.DECLINED)){
            throw new InvalidChallengeStatusException("challenge.already.declined");
        }
        Challenge databaseChallenge = getChallenge(challenge.getId());
        databaseChallenge.setStatus(Challenge.Status.DECLINED);

        kudosService.retrieveSystemKudos(usersService.findByEmail(challenge.getCreator()).get()
                , challenge.getAmount(), challenge.getName());

        return challengeRepository.save(databaseChallenge);
    }

    public Challenge accomplish(Challenge challenge) throws BusinessException {
        if(challenge.getStatus().equals(Challenge.Status.FAILED)){
            throw new InvalidChallengeStatusException("challenge.already.failed");
        }
        if(challenge.getStatus().equals(Challenge.Status.DECLINED)){
            throw new InvalidChallengeStatusException("challenge.already.declined");
        }
        if(challenge.getStatus().equals(Challenge.Status.ACCOMPLISHED)){
            throw new InvalidChallengeStatusException("challenge.already.accomplished");
        }

        Challenge databaseChallenge = getChallenge(challenge.getId());
        databaseChallenge.setStatus(Challenge.Status.ACCOMPLISHED);

        kudosService.takeSystemKudos(usersService.findByEmail(challenge.getParticipant()).get()
                ,challenge.getAmount(),challenge.getName());

        return challengeRepository.save(databaseChallenge);
    }

    public Challenge fail(Challenge challenge) throws BusinessException {
        if(challenge.getStatus().equals(Challenge.Status.ACCOMPLISHED)){
            throw new InvalidChallengeStatusException("challenge.already.accomplished");
        }
        if(challenge.getStatus().equals(Challenge.Status.DECLINED)){
            throw new InvalidChallengeStatusException("challenge.already.declined");
        }
        if(challenge.getStatus().equals(Challenge.Status.FAILED)){
            throw new InvalidChallengeStatusException("challenge.already.failed");
        }

        Challenge databaseChallenge = getChallenge(challenge.getId());
        databaseChallenge.setStatus(Challenge.Status.FAILED);

        kudosService.retrieveSystemKudos(usersService.findByEmail(challenge.getCreator()).get()
                ,challenge.getAmount(),challenge.getName());

        return challengeRepository.save(databaseChallenge);
    }

    public List<Challenge> getAllUserCreatedChallenges() {
        return challengeRepository.findChallengesByCreator(usersService.getLoggedUser().get().getEmail());
    }

    public List<Challenge> getAllUserParticipatedChallenges() {
        return challengeRepository.findChallengesByParticipant(usersService.getLoggedUser().get().getEmail());
    }

    public List<Challenge> getAllUserRefferedChallenges() {
        return challengeRepository.findAllChallengesByReferee(usersService.getLoggedUser().get().getEmail());
    }

    public List<Challenge> getAllAcceptedChallenges() {
        return challengeRepository.findAllChallengesByStatus(Challenge.Status.ACCEPTED);
    }

    public List<Challenge> getAllCreatedChallenges() {
        return challengeRepository.findAllChallengesByStatus(Challenge.Status.CREATED);
    }

    public Challenge saveChallenge(Challenge challenge){
        return challengeRepository.save(challenge);
    }

}
