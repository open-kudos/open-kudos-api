package kudos.services;

import kudos.exceptions.BusinessException;
import kudos.exceptions.InvalidChallengeStatusException;
import kudos.model.Challenge;
import kudos.repositories.ChallengeRepository;
import kudos.web.exceptions.UserException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * ChallengeService tests suite
 * Created by chc on 15.8.12.
 */
public class ChallengeServiceTest {

    private ChallengeService challengeService;
    private Challenge mockedChallenge;

    @Before
    public void before() throws InvalidChallengeStatusException, UserException {
        ChallengeRepository challengeRepository = mock(ChallengeRepository.class);
        KudosService kudosService = mock(KudosService.class);
        UsersService usersService = mock(UsersService.class);
        challengeService = new ChallengeService(challengeRepository, kudosService, usersService);
        mockedChallenge = mock(Challenge.class);
        when(challengeRepository.save(mockedChallenge)).thenReturn(mockedChallenge);

    }

    @Test(expected = InvalidChallengeStatusException.class)
    public void testIfAcceptMethodDoesNotLetDeclineAlreadyAcceptedChallenge() throws InvalidChallengeStatusException {
        Challenge challenge = new Challenge("", "", "", "", "", "", 0, Challenge.Status.DECLINED);
        challengeService.accept(challenge);
    }

    @Test(expected = InvalidChallengeStatusException.class)
    public void testIfAcceptMethodDoesNotLetAcceptAlreadyFailedChallenge() throws InvalidChallengeStatusException {
        Challenge challenge = new Challenge("", "", "", "", "", "", 0, Challenge.Status.FAILED);
        challengeService.accept(challenge);
    }

    @Test(expected = InvalidChallengeStatusException.class)
    public void testIfAcceptMethodDoesNotLetAcceptAlreadyAccomplishedChallenge() throws InvalidChallengeStatusException {
        Challenge challenge = new Challenge("", "", "", "", "", "", 0, Challenge.Status.ACCOMPLISHED);
        challengeService.accept(challenge);
    }

    @Test(expected = InvalidChallengeStatusException.class)
    public void testIfAcceptMethodDoesNotLetAcceptAlreadyAcceptedChallenge() throws InvalidChallengeStatusException {
        Challenge challenge = new Challenge("", "", "", "", "", "", 0, Challenge.Status.ACCEPTED);
        challengeService.accept(challenge);
    }

    @Test(expected = NullPointerException.class)
    public void testIfAcceptMethodLetsAcceptAlreadyCreatedChallenge() throws InvalidChallengeStatusException {
        Challenge challenge = new Challenge("", "", "", "", "", "", 0, Challenge.Status.CREATED);
        challengeService.accept(challenge);
    }

    @Test(expected = InvalidChallengeStatusException.class)
    public void testIfDeclineMethodDoesNotLetDeclineAlreadyAccomplishedChallenge() throws BusinessException, UserException {
        Challenge challenge = new Challenge("", "", "", "", "", "", 0, Challenge.Status.ACCOMPLISHED);
        challengeService.decline(challenge);
    }

    @Test(expected = InvalidChallengeStatusException.class)
    public void testIfDeclineMethodDoesNotLetDeclineAlreadyFailedChallenge() throws BusinessException, UserException {
        Challenge challenge = new Challenge("", "", "", "", "", "", 0, Challenge.Status.FAILED);
        challengeService.decline(challenge);
    }

    @Test(expected = InvalidChallengeStatusException.class)
    public void testIfDeclineMethodDoesNotLetDeclineAlreadyAcceptedChallenge() throws BusinessException, UserException {
        Challenge challenge = new Challenge("", "", "", "", "", "", 0, Challenge.Status.ACCEPTED);
        challengeService.decline(challenge);
    }

    @Test(expected = InvalidChallengeStatusException.class)
    public void testIfDeclineMethodDoesNotLetDeclineAlreadyDeclinedChallenge() throws BusinessException, UserException {
        Challenge challenge = new Challenge("", "", "", "", "", "", 0, Challenge.Status.DECLINED);
        challengeService.decline(challenge);
    }

    @Test(expected = NullPointerException.class)
    public void testIfDeclineMethodLetsDeclineAlreadyCreatedChallenge() throws BusinessException, UserException {
        Challenge challenge = new Challenge("", "", "", "", "", "", 0, Challenge.Status.CREATED);
        challengeService.decline(challenge);
    }

    @Test(expected = InvalidChallengeStatusException.class)
    public void testIfAccomplishMethodDoesNotLetAccomplishAlreadyFailedChallenge() throws BusinessException, UserException {
        Challenge challenge = new Challenge("", "", "", "", "", "", 0, Challenge.Status.FAILED);
        challengeService.accomplish(challenge);
    }

    @Test(expected = InvalidChallengeStatusException.class)
    public void testIfAccomplishMethodDoesNotLetAccomplishAlreadyDeclinedChallenge() throws BusinessException, UserException {
        Challenge challenge = new Challenge("", "", "", "", "", "", 0, Challenge.Status.DECLINED);
        challengeService.accomplish(challenge);
    }

    @Test(expected = InvalidChallengeStatusException.class)
    public void testIfAccomplishMethodDoesNotLetAccomplishAlreadyAccomplishedChallenge() throws BusinessException, UserException {
        Challenge challenge = new Challenge("", "", "", "", "", "", 0, Challenge.Status.ACCOMPLISHED);
        challengeService.accomplish(challenge);
    }

    @Test(expected = NullPointerException.class)
    public void testIfAccomplishMethodLetsAccomplishAlreadyAcceptedChallenge() throws BusinessException, UserException {
        Challenge challenge = new Challenge("", "", "", "", "", "", 0, Challenge.Status.ACCEPTED);
        challengeService.accomplish(challenge);
    }

    @Test(expected = NullPointerException.class)
    public void testIfAccomplishMethodLetsAccomplishAlreadyCreatedChallenge() throws BusinessException, UserException {
        Challenge challenge = new Challenge("", "", "", "", "", "", 0, Challenge.Status.CREATED);
        challengeService.accomplish(challenge);
    }

    @Test(expected = InvalidChallengeStatusException.class)
    public void testIfFailMethodDoesNotLetFailAlreadyAccomplishedChallenge() throws BusinessException, UserException {
        Challenge challenge = new Challenge("", "", "", "", "", "", 0, Challenge.Status.ACCOMPLISHED);
        challengeService.fail(challenge);
    }

    @Test(expected = InvalidChallengeStatusException.class)
    public void testIfFailMethodDoesNotLetFailAlreadyDeclinedChallenge() throws BusinessException, UserException {
        Challenge challenge = new Challenge("", "", "", "", "", "", 0, Challenge.Status.DECLINED);
        challengeService.fail(challenge);
    }

    @Test(expected = InvalidChallengeStatusException.class)
    public void testIfFailMethodDoesNotLetFailAlreadyFailedChallenge() throws BusinessException, UserException {
        Challenge challenge = new Challenge("", "", "", "", "", "", 0, Challenge.Status.FAILED);
        challengeService.fail(challenge);
    }

    @Test(expected = NullPointerException.class)
    public void testIfFailMethodLetsFailCreatedChallenge() throws BusinessException, UserException {
        Challenge challenge = new Challenge("", "", "", "", "", "", 0, Challenge.Status.CREATED);
        challengeService.fail(challenge);
    }

    @Test(expected = NullPointerException.class)
    public void testIfFailMethodLetsFailAcceptedChallenge() throws BusinessException, UserException {
        Challenge challenge = new Challenge("", "", "", "", "", "", 0, Challenge.Status.ACCEPTED);
        challengeService.fail(challenge);
    }

    @Test
    public void testIfSaveMethodSavesChallenge() {
        assertEquals(mockedChallenge, challengeService.save(mockedChallenge));
    }


}


