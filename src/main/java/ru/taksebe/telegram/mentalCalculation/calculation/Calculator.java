package ru.taksebe.telegram.mentalCalculation.calculation;

import ru.taksebe.telegram.mentalCalculation.enums.OperationEnum;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Формирование перечня уникальных заданий
 */
public class Calculator {
    private final Random random = new Random();

    /**
     * Формирования перечня уникальных заданий для 1 страницы итогового документа
     * @param operation операция (например, сложение)
     * @param min       минимальное значение, которое должно использоваться в заданиях
     * @param max       максимально значение, которое должно использоваться в заданиях
     * @param count     количество заданий
     */
    Set<String> getTaskSet(OperationEnum operation, int min, int max, int count) {
        Set<String> tasks = new HashSet<>();
        while (tasks.size() < count) {
            addTaskToSet(tasks, operation, min, max);
        }
        return tasks;
    }

    /**
     * Формирование задания, его проверка на попадание в заданный интервал и добавление в перечень
     * @param tasks     список заданий
     * @param operation операция (например, сложение)
     * @param min       минимальное значение, которое должно использоваться в заданиях
     * @param max       максимально значение, которое должно использоваться в заданиях
     */
    private void addTaskToSet(Set<String> tasks, OperationEnum operation, int min, int max) {
        int first;
        int second;

        switch (operation) {
            case MULTIPLICATION:
                first = getRandomIntBetweenRange(min, max);
                second = getRandomIntBetweenRange(2, (max <= 10) ? 10 : max);
                tasks.add(getMultiplicationTask(first, second));
                break;
            case DIVISION:
                first = getRandomIntBetweenRange(min, max);
                second = getRandomIntBetweenRange(2, (max <= 10) ? 10 : max);
                int multiplicationResult = first * second;
                tasks.add(String.format("%s : %s =", multiplicationResult, first));
                break;
            case SUBTRACTION:
                first = getRandomIntBetweenRange(min, max);
                second = getRandomIntBetweenRange(min, max);
                int subtractionResult = first - second;
                if (subtractionResult >= min && subtractionResult <= max) {
                    tasks.add(String.format("%s - %s =", first, second));
                }
                break;
            case ADDITION:
            default:
                first = getRandomIntBetweenRange(min, max);
                second = getRandomIntBetweenRange(min, max);
                int additionResult = first + second;
                if (additionResult >= min && additionResult <= max) {
                    tasks.add(String.format("%s + %s =", first, second));
                }
                break;
        }
    }

    /**
     * Получение случайного числа, попадающего в интервал
     */
    private int getRandomIntBetweenRange(int min, int max) {
        return (int) (Math.random() * ((max - min) + 1)) + min;
    }

    /**
     * Формирование текста задания на умножения из 2 чисел
     */
    private String getMultiplicationTask(int first, int second) {
        return random.nextBoolean() ?
                String.format("%s * %s =", first, second) :
                String.format("%s * %s =", second, first);
    }
}