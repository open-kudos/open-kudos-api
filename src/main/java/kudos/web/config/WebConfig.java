package kudos.web.config;

import kudos.KudosBusinessStrategy;
import kudos.web.servlet.JsonViewResolver;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactory;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Bean
    public ViewResolver viewResolver() {
        return new JsonViewResolver();
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:/webapp/WEB_INF/messages");
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
        return KudosBusinessStrategy.createWeeklyStrategy(50, 1);
    }

    @Bean(name = "DBTimeFormatter")
    public DateTimeFormatter DBTimeFormatter() {
        return DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss,SSS");
    }

}
