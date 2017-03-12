package kudos.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document
public class Achievement {

    @Id
    private String id;
    private String title;
    private String description;


}
