package ru.taksebe.telegram.mentalCalculation.telegram.nonCommand;

import ru.taksebe.telegram.mentalCalculation.exceptions.IllegalSettingsException;

/**
 * Валидации и расчёты настроек
 */
class SettingsAssistant {

    /**
     * Расчёт максимального значения из двух чисел, заданных пользователем. Если пользователь перепутал максимальное и
     * минимальное число, они меняются местами
     */
    static int calculateMax(int min, int max) {
        if (max > min) {
            return max;
        }
        return min;
    }

    /**
     * Расчёт минимального значения из двух чисел, заданных пользователем
     */
    static int calculateMin(int min, int max) {
        if (min < max) {
            return min;
        }
        return max;
    }

    /**
     * Расчёт значения числу страниц выгружаемого файла. Если пользователь установил значение больше 10,
     * присваивается значение 10, чтобы не перегружать сервис и не затягивать с ответом
     */
    static int calculateListCount(int listCount) {
        if (listCount > 10) {
            return  10;
        } else {
            return listCount;
        }
    }

    /**
     * Расчёт числа уникальных задач, которыне можно сформировать с использованием интервала чисел от min до max
     */
    static int calculateUniqueTaskCount(int min, int max) {
        int uniqueTaskCount = 0;
        if (max - 2 * min + 1 >= 0) {
            uniqueTaskCount = ((max - 2 * min + 2) * (max - 2 * min + 1 ))/2;
        }
        if (uniqueTaskCount < 0) {
            throw new IllegalArgumentException(String.format("Заданные значения %s - %s слишком велики. " +
                    "Число уникальных задач не влезает в Integer", min, max));
        }
        if (uniqueTaskCount == 0) {
            throw new IllegalSettingsException(String.format("\uD83D\uDCA9 Для пары чисел %s - %s не существует" +
                            " сложений и вычитаний, результат которых попадает в интервал между ними", min, max));
        }
        return uniqueTaskCount;
    }
}