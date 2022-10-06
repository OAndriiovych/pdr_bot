package org.pdr.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class MyProperties {
    private static Properties properties = null;

    private MyProperties() {
    }

    public static void reloadPropertiesFile(String path) throws IOException {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(path)) {
            properties.load(fis);
        }
        MyProperties.properties = properties;
    }

    public static String getTelegramBotToken() {
        return properties.getProperty("bot.token");
    }

    public static String getTelegramBotName() {
        return properties.getProperty("bot.name");
    }

    public static String getLiqPayPublicKey() {
        return properties.getProperty("liqpay.publicKey");
    }

    public static String getLiqPayPrivateKey() {
        return properties.getProperty("liqpay.privateKey");
    }

    public static String getServerUrl() {
        return properties.getProperty("server.url");
    }

    public static String getDBUrl(){return properties.getProperty("db.url");}

    public static String getDBPass(){return properties.getProperty("db.pass");}

    public static String getDBUserName(){return properties.getProperty("db.username");}


}
