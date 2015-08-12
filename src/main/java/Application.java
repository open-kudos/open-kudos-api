import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * Created by chc on 15.8.3.
 */
@EnableScheduling
public class Application {

    public static void main(String[] args) throws Exception{
        Server httpServer = new Server(8080);
        WebAppContext webapp = new WebAppContext("src/main/webapp","/");
        webapp.setDefaultsDescriptor("src/main/webapp/WEB-INF/web.xml");
        httpServer.setHandler(webapp);
        try {
            httpServer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

