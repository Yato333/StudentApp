package com.app.util.file;

import com.app.App;

import java.io.File;

/**
 * This file manager reads data from a selected file and loads it into the application.
 * @see Extensions Supported extensions
 */

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
