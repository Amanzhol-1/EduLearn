package spring.educhainminiapp.bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import spring.educhainminiapp.commandhandler.CommandsHandler;
import spring.educhainminiapp.config.BotConfig;

/**
 * Класс бота, отвечающий за получение и обработку сообщений от пользователей.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class EduLearnBot extends TelegramLongPollingBot {

    private final BotConfig botConfig;
    private final CommandsHandler commandsHandler;

    @Override
    public String getBotUsername() {
        return botConfig.getUsername();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        commandsHandler.handleUpdate(update, this);
    }
}


