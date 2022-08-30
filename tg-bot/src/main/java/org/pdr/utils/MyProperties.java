package org.pdr.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class MyProperties {
    private static final String PATH = "tg-bot/src/main/resources/bot.properties";
    private static final Properties PROPERTIES = getProperty();

    private MyProperties() {
    }

    public static String getTelegramBotToken() {
        return PROPERTIES.getProperty("bot.token");
    }

    public static String getTelegramBotName() {
        return PROPERTIES.getProperty("bot.name");
    }

    public static String getLiqPayPublicKey() {
        return PROPERTIES.getProperty("liqpay.publicKey");
    }

    public static String getLiqPayPrivateKey() {
        return PROPERTIES.getProperty("liqpay.privateKey");
    }

    public static String getServerUrl() {
        return PROPERTIES.getProperty("server.url");
    }

    private static Properties getProperty() {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(PATH)) {
            properties.load(fis);
        } catch (IOException e) {
            //#TODO add logger
        }
        return properties;
    }
}
