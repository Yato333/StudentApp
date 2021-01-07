package com.app.util.file;

import com.app.App;

import java.io.File;

/**
 * This file manager saves data stored in this application in a file selected by the user.
 * @see Extensions Supported extensions
 */

public final class Saver extends FileManager {
    private Saver() {}

    private static void saveSCV(File file) {

    }

    private static void savePDF(File file) {

    }

    private static void saveExcel(File file) {

    }

    /**
     * Saves all data in a selected file.
     */
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
