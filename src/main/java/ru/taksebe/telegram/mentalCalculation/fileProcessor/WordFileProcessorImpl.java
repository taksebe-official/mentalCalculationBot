package ru.taksebe.telegram.mentalCalculation.fileProcessor;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Создание стрима файла с заданиями для последующей отправки в Telegram
 */
public class WordFileProcessorImpl {

    /**
     * Создание стрима
     * @param taskList список заданий
     */
    public FileInputStream createWordFile(List<String> taskList) throws IOException {
        //Формирование документа на основе шаблона - файла .docx из папки resources
        XWPFDocument doc = new XWPFDocument(getClass().getClassLoader().getResourceAsStream("Template.docx"));
        setTaskListToXWPFDocument(doc, taskList);
        return createTempFile(doc);
    }

    /**
     * Вставка списка заданий в документ
     * @param doc объект документа Word
     * @param taskList список заданий
     */
    private void setTaskListToXWPFDocument(XWPFDocument doc, List<String> taskList) {
        //запись первой строки списка заданий в первый абзац документа (создаётся по умолчанию при создании документа)
        XWPFParagraph paragraph = doc.getLastParagraph();
        XWPFRun run = paragraph.createRun();
        setRunParameters(run, taskList.get(0));

        //запись оставшихся строк списка заданий путём создания для каждой нового абзаца
        for (int i = 1; i < taskList.size(); i++) {
            paragraph = doc.createParagraph();
            run = paragraph.createRun();
            setRunParameters(run, taskList.get(i));
        }
    }

    /**
     * Запись параметров шрифта и текста для 1 абзаца
     * @param run объект записи для абзаца Word-документа
     * @param task содержмое 1 абзаца (одно задание)
     */
    private void setRunParameters(XWPFRun run, String task) {
        run.setFontSize(20);//размер шрифта
        run.setFontFamily("Calibri");//тип шрифта
        run.setText(task);
    }

    /**
     * Формирование стрима из объекта документа Word
     * @param doc объект Word-документа
     */
    private FileInputStream createTempFile(XWPFDocument doc) throws IOException {
        File result = File.createTempFile("Print_Me", ".docx");
        try (FileOutputStream out = new FileOutputStream(result)) {
            doc.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new FileInputStream(result);
    }
}