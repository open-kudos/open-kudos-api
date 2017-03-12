package kudos.web.config;

import kudos.KudosBusinessStrategy;
import kudos.web.servlet.JsonViewResolver;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactory;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
@EnableSpringDataWebSupport
public class WebConfig extends WebMvcConfigurerAdapter {

    @Value("${kudos.weeklyAmount}")
    private int kudosWeeklyAmount;

    @Bean
    public ViewResolver viewResolver() {
        return new JsonViewResolver();
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("messages");
        return messageSource;
    }

    @Bean
    FreeMarkerConfigurationFactory freeMarkerConfigurationFactory(){
        FreeMarkerConfigurationFactory freeMarkerConfigurationFactory = new FreeMarkerConfigurationFactory();
        freeMarkerConfigurationFactory.setTemplateLoaderPath("src/main/resources/mail-templates");
        return freeMarkerConfigurationFactory;
    }

    @Bean
    public KudosBusinessStrategy kudosBusinessStrategy() {
        return KudosBusinessStrategy.createWeeklyStrategy(kudosWeeklyAmount);
    }

    @Bean(name = "DBTimeFormatter")
    public DateTimeFormatter DBTimeFormatter() {
        return DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss,SSS");
    }

}
