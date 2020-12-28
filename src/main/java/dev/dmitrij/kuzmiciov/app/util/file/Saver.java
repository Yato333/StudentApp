package dev.dmitrij.kuzmiciov.app.util.file;

import dev.dmitrij.kuzmiciov.app.App;

import java.io.File;

public final class Saver extends FileManager {
    private Saver() {}

    private static void saveSCV(File file) {

    }

    private static void savePDF(File file) {

    }

    private static void saveExcel(File file) {

    }

    public static void save() {
        File file = fileChooser.showSaveDialog(App.getPrimaryStage());
        if(file != null) {
            if (fileChooser.getSelectedExtensionFilter() == Extensions.CSV.filter)
                saveSCV(file);
            else if (fileChooser.getSelectedExtensionFilter() == Extensions.XLSX.filter)
                saveExcel(file);
            else
                savePDF(file);
        }
    }
}
