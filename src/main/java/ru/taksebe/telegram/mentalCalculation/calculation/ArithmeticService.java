package ru.taksebe.telegram.mentalCalculation.calculation;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.taksebe.telegram.mentalCalculation.enums.OperationEnum;
import ru.taksebe.telegram.mentalCalculation.fileProcessor.WordFileProcessorImpl;
import ru.taksebe.telegram.mentalCalculation.telegram.nonCommand.Settings;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Сервис генерации заданий на сложение и/или вычитание
 */
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ArithmeticService {
    WordFileProcessorImpl fileProcessor;
    Calculator calculator;

    /**
     * Формирование файла с заданиями
     * @param operations список типов операций
     * @param settings   настройки выгружаемого файла
     */
    public FileInputStream getFile(List<OperationEnum> operations, Settings settings)
            throws IOException {
        List<String> taskList = new ArrayList<>();
        for (int i = 1; i <= settings.getListCount(); i++) {
            taskList.addAll(getTaskList(operations, settings));
        }

        if (taskList.isEmpty()) {
            throw new IllegalArgumentException(String.format("По непонятным причинам по заданным настройкам " +
                    "(min = %s, max = %s, listCount = %s) не удалось создать ни одной строки " +
                    "с задачами", settings.getMin(), settings.getMax(), settings.getListCount()));
        }
        return fileProcessor.createWordFile(taskList);
    }

    /**
     * Формирование списка задач
     * @param operations типы операций
     * @param settings   настройки выгружаемого файла
     */
    private List<String> getTaskList(List<OperationEnum> operations, Settings settings) {
        int taskCount = getOperationTaskCount(operations.size());
        List<String> taskList = new ArrayList<>();
        for (OperationEnum operation : operations) {
            settings = getFixedSettings(operation, settings);
            fillTaskList(taskList, operation, taskCount, settings.getMin(), settings.getMax(),
                    getActualUniqueTaskCount(operation, settings));
        }
        Collections.shuffle(taskList);
        return taskList;
    }

    /**
     * Расчёт количества задач для каждой операции. Если запрашивается больше 4 операций - ошибка
     * @param operationsCount количество операций
     */
    private int getOperationTaskCount(int operationsCount) {
        //количество строк, вмещающихся на одну страницу А4 в горизонтальной ориентации при выбранных размере и
        //типе шрифта (см. метод setRunParameters класса WordFileProcessorImpl)
        int linesCount = 52;
        switch (operationsCount) {
            case 1:
                return linesCount;
            case 2:
                return linesCount / 2;
            case 3:
                return linesCount / 3;
            case 4:
                return linesCount / 4;
            default:
                throw new IllegalArgumentException(String.format("Недопустимое количество операций для формирования " +
                        "файла с заданиями - %s", operationsCount));
        }
    }

    /**
     * Корректировка настроек для случая, когда заданное пользователем минимальное значение не позволяет создать ни
     * одного задания на сложение/вычитание
     */
    private Settings getFixedSettings(OperationEnum operation, Settings settings) {
        if (OperationEnum.getPlusMinus().contains(operation) && settings.getPlusMinusUniqueTaskCount() == 0) {
            return new Settings(1, settings.getMax(), settings.getListCount());
        }
        return settings;
    }

    /**
     * Получение ограничения на максимальное количество уникальных задач в зависимости от вида арифметического действия
     * @param operation тип операции
     * @param settings  настройки выгружаемого файла
     */
    private int getActualUniqueTaskCount(OperationEnum operation, Settings settings) {
        switch (operation) {
            case MULTIPLICATION:
                return settings.getMultiplicationUniqueTaskCount();
            case DIVISION:
                return settings.getDivisionUniqueTaskCount();
            case ADDITION:
            case SUBTRACTION:
            default:
                return settings.getPlusMinusUniqueTaskCount();
        }
    }

    /**
     * Наполнение списка задач. В случае, если уникальных задач хватает для наполнения листа, используются только
     * уникальные задачи. В противном случае задания повторяются
     * @param taskList        список задач
     * @param operation       тип операции
     * @param taskCount       требуемое количество задач в списке
     * @param min             минимальное значение
     * @param max             максимальное значение
     * @param uniqueTaskCount число уникальных значений
     */
    private void fillTaskList(List<String> taskList, OperationEnum operation, int taskCount, int min, int max,
                              int uniqueTaskCount) {
        if (taskCount <= uniqueTaskCount) {
            taskList.addAll(calculator.getTaskSet(operation, min, max, taskCount));
        } else {
            taskList.addAll(calculator.getTaskSet(operation, min, max, uniqueTaskCount));
            int remainingNumbers = taskCount - uniqueTaskCount;
            fillTaskList(taskList, operation, remainingNumbers, min, max, uniqueTaskCount);
        }
    }
}