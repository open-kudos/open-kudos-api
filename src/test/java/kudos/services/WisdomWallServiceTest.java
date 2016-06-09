package kudos.services;

import kudos.model.Idea;
import kudos.repositories.WisdomWallRepository;
import kudos.web.exceptions.UserException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Created by Robertas on 6/9/2016.
 */
public class WisdomWallServiceTest {

    private WisdomWallService wisdomWallService;
    private Idea testIdea;
    private WisdomWallRepository wisdomWallRepository;
    private static List<Idea> ideas = new ArrayList<>();
    static {
        ideas.add(new Idea("author1", "postedBy1@test.lt", "idea1"));
        ideas.add(new Idea("author2", "postedBy2@test.lt", "idea2"));
        ideas.add(new Idea("author3", "postedBy3@test.lt", "idea3"));
    }

    @Before
    public void before() throws UserException {
        wisdomWallService = mock(WisdomWallService.class);
        wisdomWallRepository = mock(WisdomWallRepository.class);
    }

    @Test
    public void testIfServiceAddsIdeaToWisdomWall() throws UserException {
        testIdea = new Idea("author", "test@test.com", "idea");
        when(wisdomWallService.addIdeaToWisdomWall("author", "idea")).thenReturn(testIdea);
        assertEquals(wisdomWallService.addIdeaToWisdomWall("author", "idea"), testIdea);

    }

    @Test
    public void testIfServiceGetsIdeasByPersonWhoPostedIt() {
        when(wisdomWallRepository.findIdeasByPostedByEmail(any(String.class))).thenReturn(ideas);
        assertEquals("postedBy1@test.lt", wisdomWallRepository.findIdeasByPostedByEmail("").get(0).getPostedBy());
    }
}
