package kudos.services;

import kudos.exceptions.KudosExceededException;
import kudos.model.Challenge;
import kudos.model.User;
import kudos.repositories.ChallengeRepository;
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

    @Autowired
    private ChallengeRepository challengeRepository;

    /*public ChallengeService(ChallengeRepository challengeRepository, )*/

    ResponseEntity<Response> sendChallenge(Challenge challenge){
        challengeRepository.insert(challenge);
        return new ResponseEntity<Response>(ChallengeResponse.success(challenge),HttpStatus.OK);
    }

    public Challenge challenge(User participant, User referee, String name, LocalDate due, int amount)
            throws KudosExceededException {

        return null;
    }

}
