package pl.gulci.airlysched;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AirlyschedApplication {

    public static void main(String[] args) {
        SpringApplication.run(AirlyschedApplication.class, args);
    }
}
