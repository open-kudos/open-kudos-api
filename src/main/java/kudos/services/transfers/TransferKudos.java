package kudos.services.transfers;

import com.google.common.base.Optional;
import kudos.dao.DAO;
import kudos.model.Kudos;
import kudos.model.User;
import kudos.web.model.Response;
import kudos.web.model.TransferResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Created by chc on 15.8.5.
 */
public class TransferKudos {

    @Autowired
    DAO dao;

    public ResponseEntity<Response> transferKudos(String senderEmail, String receiverEmail, Kudos kudos){
        Optional<User> receiverUserOptional = (Optional<User>)dao.get(receiverEmail);
        Optional<User> senderUserOptional = (Optional<User>)(dao.get(senderEmail));
        User senderUser = senderUserOptional.get();
        if(receiverUserOptional.isPresent() && senderUser.getRemainingKudos() > kudos.getAmount()){

            senderUser.reduceKudos(kudos.getAmount());
            senderUser.addKudosOperation(kudos);

            User receiverUser = receiverUserOptional.get();
            receiverUser.increaseKudos(kudos.getAmount());
            receiverUser.addKudosOperation(kudos);

            return new ResponseEntity<>(TransferResponse.success(),HttpStatus.OK);

        } else if(senderUser.getRemainingKudos() < kudos.getAmount()){
            return new ResponseEntity<>(TransferResponse.fail("Insufficient kudos amount"), HttpStatus.METHOD_NOT_ALLOWED);
        } else {
            return new ResponseEntity<>(TransferResponse.fail("User that you want to pass the kudos does not exist"),HttpStatus.BAD_REQUEST);
        }
    }

}
