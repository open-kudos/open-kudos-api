package kudos.services.transfers;

import com.google.common.base.Optional;
import kudos.dao.UserDAO;
import kudos.model.Kudos;
import kudos.model.User;
import kudos.web.model.Response;
import kudos.web.model.TransferResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Created by chc on 15.8.5.
 */
@Service
public class KudosTransferService {

    @Autowired
    private UserDAO userDAO;

    private Logger LOG = Logger.getLogger(KudosTransferService.class.getName());

    public ResponseEntity<Response> transferKudos(String senderEmail, String receiverEmail, Kudos kudos){

        Optional<User> receiverUserOptional = userDAO.get(receiverEmail);
        Optional<User> senderUserOptional = userDAO.get(senderEmail);
        User senderUser = senderUserOptional.get();
        if(receiverUserOptional.isPresent() && senderUser.getRemainingKudos() > kudos.getKudosType().amount){
            LOG.warn("Kudos amount: "+kudos.getKudosType().amount);
            LOG.warn("User kudos amount: "+senderUser.getRemainingKudos());
            senderUser.reduceKudos(kudos.getKudosType().amount);
            senderUser.addKudosOperation(kudos);

            User receiverUser = receiverUserOptional.get();
            receiverUser.increaseKudos(kudos.getKudosType().amount);
            receiverUser.addKudosOperation(kudos);

            userDAO.update(receiverUser);
            userDAO.update(senderUser);

            return new ResponseEntity<>(TransferResponse.success(),HttpStatus.OK);

        } else if(senderUser.getRemainingKudos() < kudos.getKudosType().amount){
            return new ResponseEntity<>(TransferResponse.fail("Insufficient kudos amount"), HttpStatus.METHOD_NOT_ALLOWED);
        } else {
            return new ResponseEntity<>(TransferResponse.fail("User that you want to pass the kudos does not exist"),HttpStatus.BAD_REQUEST);
        }
    }

}
