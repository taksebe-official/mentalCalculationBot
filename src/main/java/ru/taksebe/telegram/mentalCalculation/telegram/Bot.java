package ru.taksebe.telegram.mentalCalculation.telegram;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.taksebe.telegram.mentalCalculation.exceptions.IllegalSettingsException;
import ru.taksebe.telegram.mentalCalculation.telegram.commands.operations.MinusCommand;
import ru.taksebe.telegram.mentalCalculation.telegram.commands.operations.PlusCommand;
import ru.taksebe.telegram.mentalCalculation.telegram.commands.operations.PlusMinusCommand;
import ru.taksebe.telegram.mentalCalculation.telegram.commands.service.HelpCommand;
import ru.taksebe.telegram.mentalCalculation.telegram.commands.service.SettingsCommand;
import ru.taksebe.telegram.mentalCalculation.telegram.commands.service.StartCommand;

import java.util.HashMap;
import java.util.Map;

/**
 * Собственно, бот
 */
public final class Bot extends TelegramLongPollingCommandBot {
    private Logger logger = LoggerFactory.getLogger(Bot.class);

    private final String BOT_NAME;
    private final String BOT_TOKEN;

    /**
     * Настройки файла для разных пользователей. Ключ - уникальный id чата
     */
    @Getter
    private static Map<Long, Settings> userSettings;

    public Bot(String botName, String botToken) {
        super();
        logger.info("Конструктор суперкласса отработал");
        this.BOT_NAME = botName;
        this.BOT_TOKEN = botToken;
        logger.info("Имя и токен присвоены");
        register(new StartCommand("start", "Старт"));
        logger.info("Команда start создана");
        register(new PlusCommand("plus", "Сложение"));
        logger.info("Команда plus создана");
        register(new MinusCommand("minus", "Вычитание"));
        logger.info("Команда minus создана");
        register(new PlusMinusCommand("plusminus", "Сложение и вычитание"));
        logger.info("Команда plusminus создана");
        register(new HelpCommand("help","Помощь"));
        logger.info("Команда help создана");
        register(new SettingsCommand("settings", "Мои настройки"));
        logger.info("Команда settings создана");
        userSettings = new HashMap<>();
        logger.info("Бот создан!");
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    /**
     * Ответ на запрос, не являющийся командой
     */
    @Override
    public void processNonCommandUpdate(Update update) {
        Message msg = update.getMessage();
        Long chatId = msg.getChatId();
        User user = msg.getFrom();
        String userName = user.getUserName();
        String userLogName = (userName != null) ? userName :
                String.format("%s %s", user.getLastName(), user.getFirstName());
        String text = msg.getText();
        logger.info(String.format("Пользователь %s. Начата обработка сообщения \"%s\", не являющегося командой",
                userLogName, text));

        Settings settings;
        try {
            logger.info(String.format("Пользователь %s. Пробуем создать объект настроек из сообщения \"%s\"",
                    userLogName, text));
            settings = Settings.createSettings(text);
            saveUserSettings(chatId, settings);
            logger.info(String.format("Пользователь %s. Объект настроек из сообщения \"%s\" создан и сохранён",
                    userLogName, text));
            setAnswer(chatId, "Настройки обновлены. Вы всегда можете их посмотреть с помощью /settings");
        } catch (IllegalSettingsException e) {
            logger.error(String.format("Пользователь %s. Не удалось создать объект настроек из сообщения \"%s\". " +
                            "%s", userLogName, text, e.getMessage()));
            setAnswer(chatId, e.getMessage() +
                    "\n\n❗ Настройки не были изменены. Вы всегда можете их посмотреть с помощью /settings");
        } catch (IllegalArgumentException e) {
            logger.error(String.format("Пользователь %s. Не удалось создать объект настроек из сообщения \"%s\". " +
                    "%s. %s", userLogName, text, e.getClass().getSimpleName(), e.getMessage()));
            setAnswer(chatId, "Простите, я не понимаю Вас. Возможно, Вам поможет /help");
        } finally {
            logger.info(String.format("Пользователь %s. Завершена обработка сообщения \"%s\", не являющегося командой",
                    userLogName, text));
        }
    }

    /**
     * Добавление настроек пользователя в мапу, чтобы потом их использовать для этого пользователя при генерации файла
     * Если настройки совпадают с дефолтными, они не сохраняются, чтобы впустую не раздувать мапу
     * @param chatId id чата
     * @param settings настройки
     */
    private void saveUserSettings(Long chatId, Settings settings) {
        if (!settings.equals(Settings.getDefaultSettings())) {
            userSettings.put(chatId, settings);
        }
    }

    /**
     * Отправка ответа
     * @param chatId id чата
     * @param text текст ответа
     */
    private void setAnswer(Long chatId, String text) {
        SendMessage answer = new SendMessage();
        answer.setText(text);
        answer.setChatId(chatId.toString());
        replyToUser(answer);
    }

    /**
     * Отправка ответа пользователю
     */
    private void replyToUser(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}