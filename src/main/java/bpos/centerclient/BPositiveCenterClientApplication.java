package bpos.centerclient;

import javafx.application.Application;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BPositiveCenterClientApplication implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(BPositiveCenterClientApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Application.launch(StartClient.class, args.getSourceArgs());
    }
}
