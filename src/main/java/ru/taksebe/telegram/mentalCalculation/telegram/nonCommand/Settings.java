package ru.taksebe.telegram.mentalCalculation.telegram.nonCommand;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Пользовательские настройки
 */
@Getter
@EqualsAndHashCode
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
     * Количество уникальных задач на сложение/вычитание, которыне можно сформировать с использованием интервала чисел
     * от min до max
     */
    @EqualsAndHashCode.Exclude
    private int plusMinusUniqueTaskCount;

    /**
     * Количество уникальных задач на умножение, которыне можно сформировать с использованием интервала чисел
     * от min до max
     */
    @EqualsAndHashCode.Exclude
    private int multiplicationUniqueTaskCount;

    /**
     * Количество уникальных задач на деление, которыне можно сформировать с использованием интервала чисел
     * от min до max
     */
    @EqualsAndHashCode.Exclude
    private int divisionUniqueTaskCount;

    public Settings(int min, int max, int listCount) {
        this.min = SettingsAssistant.calculateMin(min, max);
        this.max = SettingsAssistant.calculateMax(min, max);
        this.listCount = SettingsAssistant.calculateListCount(listCount);
        this.plusMinusUniqueTaskCount = SettingsAssistant.calculatePlusMinusUniqueTaskCount(this.min, this.max);
        this.multiplicationUniqueTaskCount = SettingsAssistant.calculateMultiplicationUniqueTaskCount(this.min, this.max);
        this.divisionUniqueTaskCount = SettingsAssistant.calculateDivisionUniqueTaskCount(this.min, this.max);
    }
}