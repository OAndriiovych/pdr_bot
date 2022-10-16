package org.pdr.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class MyProperties {

    private static final MyProperties inst = new MyProperties();
    private static final String DEFAULT_PATH = "src/main/resources/bot.properties";
    private static Properties properties = null;

    private MyProperties() {
    }

    public static MyProperties getInstance() {
        if (properties == null) {
            try {
                reloadPropertiesFile(DEFAULT_PATH);
            } catch (IOException ignore) {
            }
        }
        return inst;
    }

    public static void reloadPropertiesFile(String path) throws IOException {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(path)) {
            properties.load(fis);
        }
        MyProperties.properties = properties;
    }

    public String getTelegramBotToken() {
        return properties.getProperty("bot.token");
    }

    public String getTelegramBotName() {
        return properties.getProperty("bot.name");
    }

    public String getLiqPayPublicKey() {
        return properties.getProperty("liqpay.publicKey");
    }

    public String getLiqPayPrivateKey() {
        return properties.getProperty("liqpay.privateKey");
    }

    public String getServerUrl() {
        return properties.getProperty("server.url");
    }

    public static String getDBUrl(){return properties.getProperty("db.url");}

    public static String getDBPass(){return properties.getProperty("db.pass");}

    public static String getDBUserName(){return properties.getProperty("db.username");}


}
