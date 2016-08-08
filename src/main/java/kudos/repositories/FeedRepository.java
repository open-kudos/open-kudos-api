package kudos.repositories;

import kudos.model.Feed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FeedRepository extends MongoRepository<Feed, String> {

    Page<Feed> findAll(Pageable pageable);

}
