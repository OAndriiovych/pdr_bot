package org.pdr;

import org.pdr.utils.CliArgs;
import org.pdr.utils.MyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        processConsoleArgs(args);
        SpringApplication.run(Main.class, args);
    }

    private static void processConsoleArgs(String[] args) {
        CliArgs cliArgs = new CliArgs(args);
        System.setProperty(MyProperties.PATH_LIABLE, cliArgs.switchValue("-botProperties", "tg-bot/src/main/resources/bot2.properties"));
    }
}
