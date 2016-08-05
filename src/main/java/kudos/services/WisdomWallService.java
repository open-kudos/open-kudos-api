package kudos.services;

import kudos.model.Idea;
import kudos.model.User;
import kudos.repositories.WisdomWallRepository;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class WisdomWallService {

    @Autowired
    private WisdomWallRepository wisdomwallRepository;

    public void addIdea(User creator, String author, String phrase) {
        wisdomwallRepository.save(new Idea(creator, author, phrase, LocalDateTime.now().toString()));
    }

    public Idea getRandomIdea(User creator) {
        List<Idea> ideas = wisdomwallRepository.findAll();
        Random randomGenerator = new Random();
        if (ideas.size() > 0) {
            int index = randomGenerator.nextInt(ideas.size());
            return ideas.get(index);
        }
        return new Idea(creator, "Open Kudos", "There is no ideas, maybe it is time to add new one?",
                LocalDateTime.now().toString());
    }

}
