package ru.taksebe.telegram.mentalCalculation.telegram.commands.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * Команда "Помощь"
 */
public class HelpCommand extends ServiceCommand {
    private Logger logger = LoggerFactory.getLogger(HelpCommand.class);

    public HelpCommand(String identifier, String description) {
        super(identifier, description);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String userName = (user.getUserName() != null) ? user.getUserName() :
                String.format("%s %s", user.getLastName(), user.getFirstName());

        logger.info(String.format("Пользователь %s. Начато выполнение команды %s", userName,
                this.getCommandIdentifier()));
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName,
                "Я бот, который поможет Вашим детям быстро научиться считать в уме\n\n" +
                        "Я сгенерирую word-файл с заданиями, чтобы Вам не пришлось искать или придумывать их. " +
                        "Напечатайте его и попросите Вашего ребёнка решать по одной страничке в день\n\n" +
                        "❗*Список команд*\n/settings - просмотреть текущие настройки\n/plus - получить " +
                        "задания на сложение\n/minus - получить задания на вычитание\n/plusminus - получить задания " +
                        "на сложение и вычитание вперемешку\n/help - помощь\n\n" +
                        "По умолчанию я сформирую *1 страницу* заданий с использованием чисел *от 1 до 15*. Если Вы " +
                        "хотите изменить эти параметры, введите через пробел или запятую 3 числа - минимальное число " +
                        "для использования в заданиях, максимальное число и количество страниц в файле (не более 10)\n\n" +
                        "Желаю удачи\uD83D\uDE42");
        logger.info(String.format("Пользователь %s. Завершено выполнение команды %s", userName,
                this.getCommandIdentifier()));
    }
}