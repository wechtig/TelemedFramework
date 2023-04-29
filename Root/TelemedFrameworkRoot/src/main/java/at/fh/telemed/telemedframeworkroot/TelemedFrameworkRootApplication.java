package at.fh.telemed.telemedframeworkroot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;

@SpringBootApplication
@EnableJpaRepositories
@EnableJdbcHttpSession
public class TelemedFrameworkRootApplication {

    public static void main(String[] args) {
        SpringApplication.run(TelemedFrameworkRootApplication.class, args);
    }

}
