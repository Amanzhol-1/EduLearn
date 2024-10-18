package spring.educhainminiapp.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import spring.educhainminiapp.bot.EduLearnBot;
import spring.educhainminiapp.commandhandler.CommandsHandler;

/**
 * Класс конфигурации, загружающий свойства бота из файла настроек и регистрирующий бота.
 */
@Configuration
@ConfigurationProperties(prefix = "bot")
@Data
public class BotConfig {
    private String name;
    private String token;

    public String getUsername() {
        return name;
    }

    @Bean
    public CommandsHandler commandsHandler() {
        return new CommandsHandler();
    }

    @Bean
    public EduLearnBot eduLearnBot(CommandsHandler commandsHandler) {
        return new EduLearnBot(this, commandsHandler);
    }

    @Bean
    public TelegramBotsApi telegramBotsApi(EduLearnBot bot) throws Exception {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(bot);
        return botsApi;
    }
}