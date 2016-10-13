package kudos.services;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.multipart.FormDataMultiPart;
import kudos.model.Challenge;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.MediaType;

@Component
public class EmailService {

    public final static HTTPBasicAuthFilter httpBasicAuthFilter = new HTTPBasicAuthFilter("api", "key-cba3da24e695e51592f396fe07a00092");
    public final static String resourceDomain = "https://api.mailgun.net/v3/" + "mg.openkudos.com" + "/messages";


    public ClientResponse sendEmail(String emailTo, String message, String subject) {
        Client client = Client.create();

        client.addFilter(httpBasicAuthFilter);
        WebResource webResource = client.resource(resourceDomain);

        FormDataMultiPart formData = new FormDataMultiPart();
        formData.field("from", "Kudos team <info@openkudos.com>");
        formData.field("to", emailTo);
        formData.field("subject", subject);
        formData.field("html", message);

        return webResource.type(MediaType.MULTIPART_FORM_DATA).
                post(ClientResponse.class, formData);
    }

    public String checkChallengeDescription(Challenge challenge) {
        if (challenge.getDescription() == null) {
            return challenge.getName();
        } else {
            return challenge.getDescription();
        }
    }
}


