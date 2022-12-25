package org.pdr.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Getter
@Setter
@Configuration
@PropertySource("file:${" + MyProperties.PATH_LIABLE + "}")
public class MyProperties {
    public static final String PATH_LIABLE = "bot.properties";
    @Value("${bot.token}")
    String telegramBotToken;
    @Value("${bot.name}")
    String TelegramBotName;
    @Value("${liqpay.publicKey}")
    String LiqPayPublicKey;
    @Value("${liqpay.publicKey}")
    String LiqPayPrivateKey;
    @Value("${server.url}")
    String ServerUrl;
    @Value("${db.url}")
    String DBUrl;
    @Value("${db.pass}")
    String DBPass;
    @Value("${db.username}")
    String DBUserName;

}
