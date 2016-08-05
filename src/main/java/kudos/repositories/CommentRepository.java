package kudos.repositories;

import kudos.model.Challenge;
import kudos.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentRepository extends MongoRepository<Comment, String> {

      Page<Comment> findCommentsByChallengeOrderByCreationDateDesc(Challenge challenge, Pageable pageable);

}
