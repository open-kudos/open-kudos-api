package kudos.services;

import kudos.exceptions.BusinessException;
import kudos.model.Challenge;
import kudos.repositories.ChallengeRepository;
import kudos.web.exceptions.UserException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ChallengeServiceTest {
    private ChallengeService challengeService;
    private ChallengeRepository challengeRepository;
    private Challenge mockedChallenge;


    @Before
    public void before() {
        challengeService =  mock(ChallengeService.class);
        challengeRepository = mock(ChallengeRepository.class);
        mockedChallenge = mock(Challenge.class);

        when(challengeRepository.save(mockedChallenge)).thenReturn(mockedChallenge);
        when(challengeRepository.findChallengeById(any(String.class))).thenReturn(mockedChallenge);
    }

    @Test
    public void testIfServiceSavesChallenge() throws BusinessException, UserException {
        Assert.assertEquals(challengeRepository.save(mockedChallenge), mockedChallenge);
    }

    @Test
    public void testIfServiceFindsChallengeById() {
        Assert.assertEquals(challengeRepository.findChallengeById("1abc"), mockedChallenge);
    }
}
