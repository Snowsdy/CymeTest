package fr.snowsdy.cymetest;

import com.github.instagram4j.instagram4j.IGClient;
import com.github.instagram4j.instagram4j.models.media.timeline.TimelineImageMedia;
import com.github.instagram4j.instagram4j.requests.feed.FeedUserRequest;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * The entry point of the Spring Boot application.
 * <p>
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 */
@SpringBootApplication
@Theme(value = "cymetest", variant = Lumo.DARK)
@PWA(name = "CymeTest", shortName = "CymeTest")
@NpmPackage(value = "line-awesome", version = "1.3.0")
@NpmPackage(value = "@vaadin-component-factory/vcf-nav", version = "1.0.6")
public class Application extends SpringBootServletInitializer implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

        try {
            IGClient client = IGClient.builder()
                    .username("username")
                    .password("password")
                    .login();

            // 1st Test : Print the Full Name
            // System.out.println(client.getSelfProfile().getFull_name()); // Successful

            /*
            This function permits to get all single photo from the IGClient
            */
            client.sendRequest(new FeedUserRequest(client.getSelfProfile().getPk()))
                    .thenAccept(response -> response.getItems()
                            .stream()
                            .filter(TimelineImageMedia.class::isInstance)
                            .map(TimelineImageMedia.class::cast)
                            .forEach(item -> {
                                System.out.println(item.getLike_count()); // Like Count
                                System.out.println(item.getCode()); // URI's post
                                System.out.println("URL : " + item.getImage_versions2().getCandidates().get(0).getUrl()); // URL's photo

                                item.getImage_versions2().getCandidates().forEach(img -> System.out.println("IMG : " + img.getUrl()));


                            }));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
