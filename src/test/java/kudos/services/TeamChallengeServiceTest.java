package kudos.services;

import kudos.exceptions.BusinessException;
import kudos.model.TeamChallenge;
import kudos.model.TeamMember;
import kudos.repositories.TeamChallengeRepository;
import kudos.web.exceptions.UserException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Robertas on 6/9/2016.
 */
public class TeamChallengeServiceTest {
    private TeamChallengeService teamChallengeService;
    private TeamChallengeRepository teamChallengeRepository;
    private TeamChallenge mockedChallenge;
    private static List<TeamMember> team1 = new ArrayList<>();
    static {
        team1.add(new TeamMember("test1@test.lt", false));
    }
    private static List<TeamMember> team2 = new ArrayList<>();
    static {
        team2.add(new TeamMember("test2@test.lt", false));
    }
    @Before
    public void before() {
        teamChallengeService =  mock(TeamChallengeService.class);
        teamChallengeRepository = mock(TeamChallengeRepository.class);
        mockedChallenge = mock(TeamChallenge.class);
    }

    @Test
    public void testIfServiceCreatesTeamChallenge() throws BusinessException, UserException {

    }
}
