package org.pdr;

import org.pdr.utils.MyProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileNotFoundException;
import java.io.IOException;

@SpringBootApplication
public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            loadConf(args);
        } catch (IOException e) {
            logger.error("some problem with file properties", e);
        }
        SpringApplication.run(Main.class, args);
    }


    private static void loadConf(String[] args) throws IOException {
        logger.info("loading properties");
        if (args.length == 0) {
            throw new FileNotFoundException("absent file path properties");
        }
        MyProperties.reloadPropertiesFile(args[0]);
        logger.info("properties loaded ");
    }
}
