package kudos.services;

import kudos.model.TeamChallenge;
import kudos.model.TeamMember;
import kudos.repositories.TeamChallengeRepository;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Robertas on 6/9/2016.
 */
public class TeamChallengeServiceTest {
    private TeamChallengeService teamChallengeService;
    private TeamChallengeRepository teamChallengeRepository;

    @Before
    public void before() {
        teamChallengeService =  mock(TeamChallengeService.class);
        teamChallengeRepository = mock(TeamChallengeRepository.class);
    }

    @Test
    public void testIfServiceCreatesTeamChallenge() {
       /* TeamChallenge teamChallenge = new TeamChallenge("name", [any(TeamMember.class)], [any(TeamMember.class)], "description", 1);
        when()*/
    }
}
