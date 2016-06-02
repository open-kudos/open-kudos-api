package kudos.services;

import kudos.exceptions.BusinessException;
import kudos.exceptions.InvalidChallengeStatusException;
import kudos.model.TeamChallenge;
import kudos.model.TeamMember;
import kudos.model.Transaction;
import kudos.repositories.TeamChallengeRepository;
import kudos.web.exceptions.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TeamChallengeService {

    @Autowired
    private TeamChallengeRepository repository;
    @Autowired
    private UsersService usersService;
    @Autowired
    private KudosService kudosService;

    public TeamChallenge createTeamChallenge(String name, List<TeamMember> firstTeam, List<TeamMember> secondTeam, String description, int amount) throws UserException, BusinessException {

        TeamChallenge challenge = new TeamChallenge(name, firstTeam, secondTeam, description, amount, TeamChallenge.Status.CREATED);
        return repository.insert(challenge);
    }

    public TeamChallenge accept(TeamChallenge teamChallenge) throws BusinessException, UserException {
        checkNotAccomplishedDeclinedFailedCanceledOrAccepted(teamChallenge);

        for (TeamMember participant : teamChallenge.getFirstTeam()) {
            if (participant.getMemberEmail().equals(usersService.getLoggedUser().get().getEmail()) && !participant.getAccepted()) {
                participant.setAccepted(true);
            }
        }

        for (TeamMember participant : teamChallenge.getSecondTeam()) {
            if (participant.getMemberEmail().equals(usersService.getLoggedUser().get().getEmail()) && !participant.getAccepted()) {
                participant.setAccepted(true);
            }
        }

        kudosService.reduceFreeKudos(usersService.getLoggedUser().get(), teamChallenge.getAmount(), "");

        if (areAllTrue(teamChallenge.getFirstTeam()) && areAllTrue(teamChallenge.getSecondTeam())) {
            return setStatusAndTeamsAndSave(teamChallenge, TeamChallenge.Status.ACCEPTED);
        }
        return setStatusAndTeamsAndSave(teamChallenge, TeamChallenge.Status.CREATED);
    }

    public TeamChallenge decline(TeamChallenge teamChallenge) throws BusinessException, UserException {
        checkNotAccomplishedDeclinedFailedCanceledOrAccepted(teamChallenge);

        for (TeamMember participant : teamChallenge.getFirstTeam()) {
            if (participant.getAccepted())
                kudosService.retrieveSystemKudos(usersService.findByEmail(participant.getMemberEmail()).get(), teamChallenge.getAmount(), teamChallenge.getName(), Transaction.Status.DECLINED_CHALLENGE);
        }

        return setStatusAndTeamsAndSave(teamChallenge, TeamChallenge.Status.DECLINED);
    }

    public TeamChallenge cancel(TeamChallenge teamChallenge) throws BusinessException, UserException {
        checkNotAccomplishedDeclinedFailedCanceledOrAccepted(teamChallenge);

        for (TeamMember participant : teamChallenge.getFirstTeam()) {
            if (participant.getAccepted())
                kudosService.retrieveSystemKudos(usersService.findByEmail(participant.getMemberEmail()).get(), teamChallenge.getAmount(), teamChallenge.getName(), Transaction.Status.CANCELED_CHALLENGE);
        }

        return setStatusAndTeamsAndSave(teamChallenge, TeamChallenge.Status.CANCELED);
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

    private TeamChallenge setStatusAndTeamsAndSave(TeamChallenge challenge, TeamChallenge.Status status) {
        TeamChallenge databaseChallenge = repository.findChallengeById(challenge.getId());
        databaseChallenge.setStatus(status);
        databaseChallenge.setFirstTeam(challenge.getFirstTeam());
        databaseChallenge.setSecondTeam(challenge.getSecondTeam());
        return repository.save(databaseChallenge);
    }

    public boolean areAllTrue(List<TeamMember> participants) {
        for (TeamMember participant : participants) {
            if (!participant.getAccepted())
                return false;
        }
        return true;
    }

    public Optional<TeamChallenge> getChallenge(String id) {
        return Optional.ofNullable(repository.findChallengeById(id));
    }

}
