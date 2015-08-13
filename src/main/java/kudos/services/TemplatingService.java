package kudos.services;

import freemarker.cache.FileTemplateLoader;
import freemarker.core.ParseException;
import freemarker.template.*;
import org.springframework.stereotype.Component;

import java.io.File;
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

    public TemplatingService() throws IOException {
        configuration = new Configuration(new Version(2, 3, 23));
        configuration.setClassForTemplateLoading(TemplatingService.class, "/");

        FileTemplateLoader templateLoader = new FileTemplateLoader(new File("src/main/resources/mail-templates"));
        configuration.setTemplateLoader(templateLoader);
        mainTemplate = configuration.getTemplate("MainTemplate.ftl");
    }

    public String getEmailHtml(String title, String message, String hashedLink) throws IOException, TemplateException {

        data.put("title", title);

        Template emailTemplate = configuration.getTemplate("RegistrationTemplate.ftl");

        Map<String, Object> emailTemplateData = new HashMap<>();

        emailTemplateData.put("message",message);
        emailTemplateData.put("hashedLink", hashedLink);

        Writer out = new StringWriter();
        emailTemplate.process(emailTemplateData, out);

        data.put("customTemplate", out.toString());
        out = new StringWriter();

        mainTemplate.process(data,out);
        return out.toString();

    }



}
