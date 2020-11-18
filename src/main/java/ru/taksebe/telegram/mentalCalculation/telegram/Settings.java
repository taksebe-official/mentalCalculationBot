package ru.taksebe.telegram.mentalCalculation.telegram;

import lombok.Data;
import ru.taksebe.telegram.mentalCalculation.exceptions.IllegalSettingsException;

import java.util.Map;

/**
 * Пользовательские настройки
 */
@Data
public class Settings {

    /**
     * Минимальное число, используемое в заданиях
     */
    private int min;

    /**
     * Максимальное число, используемое в заданиях
     */
    private int max;

    /**
     * Количество страниц выгружаемого файла
     */
    private int listCount;

    /**
     * Количество уникальных задач, которыне можно сформировать с использованием интервала чисел от min до max
     */
    private int uniqueTaskCount;

    public Settings(int min, int max, int listCount) {
        validateSettings(min, max, listCount);
        setMinMax(min, max);
        setListCount(listCount);
        this.uniqueTaskCount = calculateUniqueTaskCount();
    }

    /**
     * Получение настроек по id чата. Если ранее для этого чата в ходе сеанса работы бота настройки не были установлены,
     * используются настройки по умолчанию
     */
    public static Settings getSettings(Long chatId) {
        Map<Long, Settings> userSettings = Bot.getUserSettings();
        Settings settings = userSettings.get(chatId);
        if (settings == null) {
            return getDefaultSettings();
        }
        return settings;
    }

    /**
     * Создание настроек из полученного пользователем сообщения
     * @param text текст сообщения
     * @throws IllegalArgumentException пробрасывается, если сообщение пользователя не соответствует формату
     */
    static Settings createSettings(String text) throws IllegalArgumentException {
        if (text == null) {
            throw new IllegalArgumentException("Сообщение не является текстом");
        }
        text = text.replaceAll("-", "")//избавляемся от отрицательных чисел (умники найдутся)
                .replaceAll(", ", ",")//меняем ошибочный разделитель "запятая+пробел" на запятую
                .replaceAll(" ", ",");//меняем разделитель-пробел на запятую
        String[] parameters = text.split(",");
        if (parameters.length != 3) {
            throw new IllegalArgumentException(String.format("Не удалось разбить сообщение \"%s\" на 3 составляющих",
                    text));
        }
        return new Settings(Integer.parseInt(parameters[0]), Integer.parseInt(parameters[1]),
                Integer.parseInt(parameters[2]));
    }

    /**
     * Получение настроек по умолчанию
     */
    private static Settings getDefaultSettings() {
        return new Settings(1, 15, 1);
    }

    /**
     * Валидация настроек
     */
    private void validateSettings(int min, int max, int listCount) {
        if (min == 0 || max == 0 || listCount == 0) {
            throw new IllegalSettingsException("\uD83D\uDCA9 Ни один из параметров не может равняться 0");
        }
    }

    /**
     * Присвоение значений минимальному и максимальному числам, используемым в заданиях. Если пользователь перепутал
     * максимальное и минимальное число, они меняются местами
     */
    private void setMinMax(int min, int max) {
        if (max < min) {
            this.max = min;
            this.min = max;
        } else {
            this.min = min;
            this.max = max;
        }
    }

    /**
     * Присвоение значения числу страниц выгружаемого файла. Если пользователь установил значение больше 10,
     * присваивается значение 10, чтобы не перегружать сервис и не затягивать с ответом
     */
    private void setListCount(int listCount) {
        if (listCount > 10) {
            this.listCount = 10;
        } else {
            this.listCount = listCount;
        }
    }

    /**
     * Расчёт числа уникальных задач, которыне можно сформировать с использованием интервала чисел от min до max
     */
    private int calculateUniqueTaskCount() {
        int uniqueTaskCount = 0;
        if (this.max - 2 * this.min + 1 >= 0) {
            uniqueTaskCount = ((this.max - 2 * this.min + 2) * (this.max - 2 * this.min + 1 ))/2;
        }
        if (uniqueTaskCount == 0) {
            throw new IllegalSettingsException(String.format("\uD83D\uDCA9 Для пары чисел %s - %s не существует" +
                    " сложений и вычитаний, результат которых попадает в интервал между ними",
                    this.min, this.max));
        }
        return uniqueTaskCount;
    }
}