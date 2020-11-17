package ru.taksebe.telegram.mentalCalculation.fileProcessor;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * Создание стрима файла с заданиями для последующей отправки в Telegram
 */
public interface WordFileProcessor {

    /**
     * Создание стрима
     * @param taskList список заданий
     */
    FileInputStream createWordFile(List<String> taskList) throws IOException;
}