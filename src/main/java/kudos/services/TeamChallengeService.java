package kudos.services;

import kudos.exceptions.BusinessException;
import kudos.exceptions.InvalidChallengeStatusException;
import kudos.model.TeamChallenge;
import kudos.model.User;
import kudos.repositories.TeamChallengeRepository;
import kudos.repositories.UserRepository;
import kudos.web.exceptions.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class TeamChallengeService {

    @Autowired
    private TeamChallengeRepository repository;
    @Autowired
    private UsersService usersService;
    @Autowired
    private KudosService kudosService;

    public TeamChallenge createTeamChallenge(String name, Map<String, Boolean> firstTeam, Map<String, Boolean> secondTeam, String description, String finishDate, int amount) throws UserException, BusinessException {

        if (firstTeam.containsKey(usersService.getLoggedUser().get().getEmail())) {
            firstTeam.put(usersService.getLoggedUser().get().getEmail(), true);
        }
        kudosService.reduceFreeKudos(usersService.getLoggedUser().get(), amount, name);
        TeamChallenge challenge = new TeamChallenge(name, firstTeam, secondTeam, description, finishDate, amount, TeamChallenge.Status.CREATED);
        return repository.insert(challenge);
    }

    public TeamChallenge accept(TeamChallenge teamChallenge) throws BusinessException, UserException {
        checkNotAccomplishedDeclinedFailedCanceledOrAccepted(teamChallenge);
        if (teamChallenge.getSecondTeam().containsKey(usersService.getLoggedUser().get().getEmail())) {
            teamChallenge.getSecondTeam().put(usersService.getLoggedUser().get().getEmail(), true);
        }

        kudosService.reduceFreeKudos(usersService.getLoggedUser().get(), teamChallenge.getAmount(), "");
        if (areAllTrue(teamChallenge.getSecondTeam())) {
            return setStatusAndSave(teamChallenge, TeamChallenge.Status.ACCEPTED);
        }
        return setStatusAndSave(teamChallenge, TeamChallenge.Status.CREATED);
    }

    private void checkNotAccomplishedDeclinedFailedCanceledOrAccepted(TeamChallenge teamChallenge) throws InvalidChallengeStatusException {
        switch (teamChallenge.getStatus()) {
            case ACCEPTED:
                throw new InvalidChallengeStatusException("challenge_already_accepted");
        }
        checkNotAccomplishedDeclinedFailedOrCanceled(teamChallenge);
    }

    private void checkNotAccomplishedDeclinedFailedOrCanceled(TeamChallenge teamChallenge) throws InvalidChallengeStatusException {
        switch (teamChallenge.getStatus()) {
            case ACCOMPLISHED:
                throw new InvalidChallengeStatusException("challenge_already_accomplished");
            case DECLINED:
                throw new InvalidChallengeStatusException("challenge_already_declined");
            case FAILED:
                throw new InvalidChallengeStatusException("challenge_already_failed");
            case CANCELED:
                throw new InvalidChallengeStatusException("challenge_already_canceled");
        }
    }

    private TeamChallenge setStatusAndSave(TeamChallenge challenge, TeamChallenge.Status status) {
        TeamChallenge databaseChallenge = repository.findChallengeById(challenge.getId());
        databaseChallenge.setStatus(status);
        return repository.save(databaseChallenge);
    }

    public boolean areAllTrue(Map<String, Boolean> participants) {
        for (Map.Entry<String, Boolean> participant : participants.entrySet()) {
            if (!participant.getValue())
                return false;
        }
        return true;
    }

    public Optional<TeamChallenge> getChallenge(String id) {
        return Optional.ofNullable(repository.findChallengeById(id));
    }
}
