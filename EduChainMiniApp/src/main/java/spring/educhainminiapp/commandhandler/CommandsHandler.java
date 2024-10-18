package spring.educhainminiapp.commandhandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * Простой диспетчер команд, который просто возвращает полученный текст.
 */
@Component
@Slf4j
public class CommandsHandler {

    public void handleUpdate(Update update, AbsSender sender) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String chatId = update.getMessage().getChatId().toString();
            String text = update.getMessage().getText();

            SendMessage message = SendMessage.builder()
                    .chatId(chatId)
                    .text("Вы написали: " + text)
                    .build();
            try {
                sender.execute(message);
            } catch (Exception e) {
                log.error("Ошибка при отправке сообщения: {}", e.getMessage(), e);
            }
        }
    }
}