package dev.dmitrij.kuzmiciov.app.util.file;

import dev.dmitrij.kuzmiciov.app.App;

import java.io.File;

public final class Loader extends FileManager {
    private Loader() {}

    private static void loadSCV(File file) {

    }

    private static void loadExcel(File file) {

    }

    private static void loadPDF(File file) {

    }

    public static void load() {
        File file = fileChooser.showOpenDialog(App.getPrimaryStage());
        if(file == null)
            return;
        if(fileChooser.getSelectedExtensionFilter() == Extensions.CSV.filter)
            loadSCV(file);
        else if(fileChooser.getSelectedExtensionFilter() == Extensions.XLSX.filter)
            loadExcel(file);
        else
            loadPDF(file);
    }
}
