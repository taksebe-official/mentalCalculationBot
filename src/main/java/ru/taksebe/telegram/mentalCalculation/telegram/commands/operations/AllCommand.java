package ru.taksebe.telegram.mentalCalculation.telegram.commands.operations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.taksebe.telegram.mentalCalculation.Utils;
import ru.taksebe.telegram.mentalCalculation.enums.OperationEnum;

public class AllCommand extends OperationCommand {
    private Logger logger = LoggerFactory.getLogger(PlusCommand.class);

    public AllCommand(String identifier, String description) {
        super(identifier, description);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String userName = Utils.getUserName(user);

        logger.debug(String.format("Пользователь %s. Начато выполнение команды %s", userName,
                this.getCommandIdentifier()));
        sendAnswer(absSender, chat.getId(), OperationEnum.getAllOperations(), this.getDescription(),
                this.getCommandIdentifier(), userName);
        logger.debug(String.format("Пользователь %s. Завершено выполнение команды %s", userName,
                this.getCommandIdentifier()));
    }
}
