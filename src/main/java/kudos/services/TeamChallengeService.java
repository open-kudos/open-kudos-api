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

import javax.jws.soap.SOAPBinding;
import java.util.*;

@Service
public class TeamChallengeService {

    private TeamChallengeRepository teamChallengeRepository;
    private UsersService usersService;
    private KudosService kudosService;

    @Autowired
    public TeamChallengeService(TeamChallengeRepository teamChallengeRepository, UsersService usersService, KudosService kudosService) {
        this.teamChallengeRepository = teamChallengeRepository;
        this.usersService = usersService;
        this.kudosService = kudosService;
    }

    public TeamChallenge createTeamChallenge(String name, List<TeamMember> firstTeam, List<TeamMember> secondTeam, String description, int amount) throws UserException, BusinessException {

        TeamChallenge challenge = new TeamChallenge(name, firstTeam, secondTeam, description, amount, TeamChallenge.Status.CREATED);
        return teamChallengeRepository.insert(challenge);
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

    public TeamChallenge accomplish(TeamChallenge teamChallenge) throws BusinessException, UserException {
        checkNotAccomplishedDeclinedFailedOrCanceled(teamChallenge);

        if (teamChallenge.getFirstTeamStatus() == null) {
            return setSecondTeamStatusAndSave(teamChallenge, teamChallenge.getSecondTeamStatus());
        } else if (teamChallenge.getSecondTeamStatus() == null) {
            return setFirstTeamStatusAndSave(teamChallenge, teamChallenge.getFirstTeamStatus());
        } else if (teamChallenge.getFirstTeamStatus() == teamChallenge.getSecondTeamStatus()) {
            teamChallenge.setFirstTeamStatus(null);
            teamChallenge.setSecondTeamStatus(null);
            return setBothTeamsStatusAndSave(teamChallenge, teamChallenge.getFirstTeamStatus(), teamChallenge.getSecondTeamStatus());
        }

        for (TeamMember participant : checkWhoIsWinner(teamChallenge)) {
            kudosService.takeSystemKudos(usersService.findByEmail(participant.getMemberEmail()).get(), 2 * teamChallenge.getAmount(), teamChallenge.getName(), Transaction.Status.COMPLETED_CHALLENGE);
        }

        return setStatusAndTeamsAndSave(teamChallenge, TeamChallenge.Status.ACCOMPLISHED);
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
        TeamChallenge databaseChallenge = teamChallengeRepository.findChallengeById(challenge.getId());
        databaseChallenge.setStatus(status);
        databaseChallenge.setFirstTeam(challenge.getFirstTeam());
        databaseChallenge.setSecondTeam(challenge.getSecondTeam());
        return teamChallengeRepository.save(databaseChallenge);
    }

    private TeamChallenge setFirstTeamStatusAndSave(TeamChallenge challenge, Boolean status) {
        TeamChallenge databaseChallenge = teamChallengeRepository.findChallengeById(challenge.getId());
        databaseChallenge.setFirstTeamStatus(status);
        return teamChallengeRepository.save(databaseChallenge);
    }

    private TeamChallenge setSecondTeamStatusAndSave(TeamChallenge challenge, Boolean status) {
        TeamChallenge databaseChallenge = teamChallengeRepository.findChallengeById(challenge.getId());
        databaseChallenge.setSecondTeamStatus(status);
        return teamChallengeRepository.save(databaseChallenge);
    }

    private TeamChallenge setBothTeamsStatusAndSave (TeamChallenge challenge, Boolean firstTeamStatus, Boolean secondTeamStatus) {
        TeamChallenge databaseChallenge = teamChallengeRepository.findChallengeById(challenge.getId());
        databaseChallenge.setFirstTeamStatus(firstTeamStatus);
        databaseChallenge.setSecondTeamStatus(secondTeamStatus);
        return teamChallengeRepository.save(databaseChallenge);
    }

    private List<TeamMember> checkWhoIsWinner(TeamChallenge challenge) {
        if (challenge.getFirstTeamStatus() && !challenge.getSecondTeamStatus()) {
            return challenge.getFirstTeam();
        }
        return challenge.getSecondTeam();
    }

    public boolean areAllTrue(List<TeamMember> participants) {
        for (TeamMember participant : participants) {
            if (!participant.getAccepted())
                return false;
        }
        return true;
    }

    public Optional<TeamChallenge> getChallenge(String id) {
        return Optional.ofNullable(teamChallengeRepository.findChallengeById(id));
    }

    public List<TeamChallenge> getUserChallengesByStatus(TeamChallenge.Status status) throws UserException {
        List<TeamChallenge> allChallenges = teamChallengeRepository.findAllChallengesByFirstTeamMemberEmailAndStatus(usersService.getLoggedUser().get().getEmail(), status);
        allChallenges.addAll(teamChallengeRepository.findAllChallengesBySecondTeamMemberEmailAndStatus(usersService.getLoggedUser().get().getEmail(), status));
        return allChallenges;

    }

}
