package kudos.services;

import kudos.exceptions.BusinessException;
import kudos.model.Challenge;
import kudos.model.User;
import kudos.repositories.ChallengeRepository;
import kudos.web.exceptions.UserException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ChallengeServiceTest {

    private ChallengeService challengeService;

    private ChallengeRepository challengeRepository;

    private Challenge mockedChallenge;


    @Before
    public void before() throws UserException, BusinessException {
        challengeService =  mock(ChallengeService.class);
        challengeRepository = mock(ChallengeRepository.class);
        mockedChallenge = mock(Challenge.class);

    }

    @Test
    public void testIfServiceSavesChallenge() throws BusinessException, UserException {
        when(challengeService.save(any(Challenge.class))).thenReturn(mockedChallenge);
        assertEquals(challengeService.save(new Challenge("", "", "", "", "", "", 1, Challenge.Status.CREATED)), mockedChallenge);
    }

    @Test
    public void testIfServiceFindsChallengeById() {
        when(challengeRepository.findChallengeById(any(String.class))).thenReturn(mockedChallenge);
        assertEquals(challengeRepository.findChallengeById("1abc"), mockedChallenge);
    }

    @Test
    public void testIfCreateChallengeMethodReturnsChallenge() throws UserException, BusinessException {
        when(challengeService.create(any(User.class), any(String.class), any(String.class), any(String.class), any(Integer.class)))
                .thenReturn(mockedChallenge);
        assertEquals(challengeService.create(new User("test", "test", "test", "test"), "test", "test", "test", 1), mockedChallenge);
    }

    @Test
    public void testIfAcceptChallengeMethodReturnsChallenge() throws UserException, BusinessException {
        when(challengeService.accept(any(Challenge.class))).thenReturn(mockedChallenge);
        assertEquals(challengeService.accept(new Challenge("", "", "", "", "", "", 1, Challenge.Status.ACCEPTED)), mockedChallenge);
    }

    @Test
    public void testIfDeclineChallengeMethodReturnsChallenge() throws UserException, BusinessException {
        when(challengeService.decline(any(Challenge.class))).thenReturn(mockedChallenge);
        assertEquals(challengeService.decline(new Challenge("", "", "", "", "", "", 1, Challenge.Status.DECLINED)), mockedChallenge);
    }

    @Test
    public void testIfCancelChallengeMethodReturnsChallenge() throws UserException, BusinessException {
        when(challengeService.cancel(any(Challenge.class))).thenReturn(mockedChallenge);
        assertEquals(challengeService.cancel(new Challenge("", "", "", "", "", "", 1, Challenge.Status.CANCELED)), mockedChallenge);
    }

}
