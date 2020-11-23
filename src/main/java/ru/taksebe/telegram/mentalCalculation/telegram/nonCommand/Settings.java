package ru.taksebe.telegram.mentalCalculation.telegram.nonCommand;

import lombok.Getter;

/**
 * Пользовательские настройки
 */
@Getter
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
        this.min = SettingsAssistant.calculateMin(min, max);
        this.max = SettingsAssistant.calculateMax(min, max);
        this.listCount = SettingsAssistant.calculateListCount(listCount);
        this.uniqueTaskCount = SettingsAssistant.calculateUniqueTaskCount(this.min, this.max);
    }

    /**
     * Получение настроек по умолчанию
     */
    public static Settings getDefaultSettings() {
        return new Settings(1, 15, 1);
    }
}