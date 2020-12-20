package com.cex.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@ComponentScan(basePackages = {"com.cex.bot", "com.cex.common"})
@PropertySource("classpath:application.properties")
@Import({DiscordConfig.class, DbConfig.class, FishingBotAsyncConfigurer.class, EhcacheConfig.class})
@EnableCaching
public class FishingBotRootContext {
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {

        return new PropertySourcesPlaceholderConfigurer();
    }
}
