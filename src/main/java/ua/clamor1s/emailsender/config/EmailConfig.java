package ua.clamor1s.emailsender.config;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Map;
import java.util.Properties;

@Configuration
@EnableScheduling
public class EmailConfig {

    @Bean
    public Dotenv getEnv() {
        return Dotenv.load();
    }

    @Bean
    public Session getJavaMailSender() {
        Dotenv env = getEnv();

        String username = env.get("EMAIL_ADDRESS");
        String password = env.get("EMAIL_API_PASSWORD");

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                }
        });

        return session;
    }


}
