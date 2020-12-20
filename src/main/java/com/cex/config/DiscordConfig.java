package com.cex.config;

import com.cex.common.security.dataCryptor.DataCryptor;
import com.cex.common.security.dataCryptor.DiscordDataCryptorImpl;
import com.cex.common.listener.DiscordBotListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.security.auth.login.LoginException;

@Configuration
public class DiscordConfig {
    @Value("${discord.bot.token}")
    private String token;

    @Bean
    public DataCryptor discordDataCryptor() {
        return new DiscordDataCryptorImpl();
    }

    @Bean
    public ListenerAdapter listenerAdapter() {
        return new DiscordBotListener();
    }

    @Bean
    public JDABuilder discordJdaBuilder() {
        return JDABuilder.createDefault(discordDataCryptor().decryptData(token))
                .addEventListeners(listenerAdapter())
                .setStatus(OnlineStatus.ONLINE)
                .setAutoReconnect(true);
    }

    @Bean
    public JDA discordJDA() throws LoginException {
        return discordJdaBuilder().build();
    }
}
