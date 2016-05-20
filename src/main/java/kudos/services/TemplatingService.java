package kudos.services;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chc on 15.8.13.
 */
@Component
public class TemplatingService {

    private final Template mainTemplate;
    private final Configuration configuration;
    private final Map<String, Object> data = new HashMap<>();

    private static final String DEFAULT_TITLE = "Welcome to KUDOS app!";

    public TemplatingService() throws IOException {
        configuration = new Configuration(new Version(2, 3, 23));
        configuration.setClassForTemplateLoading(TemplatingService.class, "/");
        mainTemplate = configuration.getTemplate("mail-templates/MainTemplate.ftl");

        data.put("title", DEFAULT_TITLE);
    }

    public String getHtml(String message, String hashedLink, String subject) throws IOException, TemplateException {

        Template emailTemplate = configuration.getTemplate("RegistrationTemplate.ftl");

        Map<String, Object> emailTemplateData = new HashMap<>();

        emailTemplateData.put("message",message);
        emailTemplateData.put("hashedLink", hashedLink);
        emailTemplateData.put("subject",subject);

        Writer out = new StringWriter();
        emailTemplate.process(emailTemplateData, out);

        data.put("customTemplate", out.toString());
        out = new StringWriter();

        mainTemplate.process(data,out);
        return out.toString();
    }

}
