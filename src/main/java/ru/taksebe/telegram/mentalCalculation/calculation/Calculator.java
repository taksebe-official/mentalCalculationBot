package ru.taksebe.telegram.mentalCalculation.calculation;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.taksebe.telegram.mentalCalculation.enums.OperationEnum;

import java.util.HashSet;
import java.util.Set;

/**
 * Формирование перечня уникальных заданий
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class Calculator {

    /**
     * Формирования перечня уникальных заданий для 1 страницы итогового документа
     * @param operation операция (например, сложение)
     * @param min минимальное значение, которое должно использоваться в заданиях
     * @param max максимально значение, которое должно использоваться в заданиях
     * @param count количество заданий
     */
    Set<String> getTaskSet(OperationEnum operation, int min, int max, int count) {
        Set<String> tasks = new HashSet<>();
        while (tasks.size() < count) {
            addTaskToSet(tasks, operation, min, max);
        }
        return tasks;
    }

    /**
     * Формирование задания, его проверка на попадание в заданные интервал и добавление в перечень
     * @param tasks список заданий
     * @param operation операция (например, сложение)
     * @param min минимальное значение, которое должно использоваться в заданиях
     * @param max максимально значение, которое должно использоваться в заданиях
     */
    private void addTaskToSet(Set<String> tasks, OperationEnum operation, int min, int max) {
        int first = getRandomIntBetweenRange(min, max);
        int second = getRandomIntBetweenRange(min, max);

        int result = calculate(operation, first, second);
        if (result >= min && result <= max) {
            tasks.add(generateTask(operation, first, second));
        }
    }

    /**
     * Получение случайного числа, попадающего в интервал
     */
    private int getRandomIntBetweenRange(int min, int max) {
        return (int) (Math.random() * ((max - min) + 1)) + min;
    }

    /**
     * Расчёт результата
     * @param operation операция (например, сложение)
     * @param first первый аргумент
     * @param second второй аргумент
     */
    private int calculate(OperationEnum operation, int first, int second) {
        switch (operation) {
            case SUBTRACTION:
                return first - second;
            case ADDITION:
            default:
                return first + second;
        }
    }

    /**
     * Формирование строки задания
     */
    private String generateTask(OperationEnum operation, int first, int second) {
        switch (operation) {
            case SUBTRACTION:
                return String.format("%s - %s =", first, second);
            case ADDITION:
            default:
                return String.format("%s + %s =", first, second);
        }
    }
}