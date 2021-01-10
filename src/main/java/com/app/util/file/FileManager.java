package com.app.util.file;

import com.app.util.Utility;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;

/**
 * This class is a base for {@link Loader} and {@link Saver} classes for this application.
 * It contains the enum, describing the file extensions that are supported in this application
 * and an instance of {@link FileChooser} with those extensions.
 * @see Extensions
 */

public abstract class FileManager implements Utility {
    protected final static FileChooser fileChooser = new FileChooser();
    protected final static String LS = System.lineSeparator();
    static {
        for (var ext : Extensions.values())
            fileChooser.getExtensionFilters().add(ext.filter);
        fileChooser.setSelectedExtensionFilter(fileChooser.getExtensionFilters().get(0));
    }

    protected static void showErrorAlert(Exception e) {
        var alert = new Alert(Alert.AlertType.ERROR);
        StringBuilder contentTextBuilder = new StringBuilder();
        for(var el : e.getStackTrace())
            contentTextBuilder.append(el.toString()).append(LS);
        alert.setContentText(contentTextBuilder.toString());
        alert.showAndWait();
    }

    /**
     * These file extensions are supported in this application.
     * @see javafx.stage.FileChooser.ExtensionFilter
     */
    public enum Extensions {
        CSV ("Comma separated values", "*.csv"),
        XLSX ("Excel file", "*.xlsx"),
        PDF ("PDF file", "*.pdf");

        public final String description, extension;
        public final FileChooser.ExtensionFilter filter;

        Extensions(String description, String extension) {
            this.description = description;
            this.extension = extension;
            filter = new FileChooser.ExtensionFilter(description, extension);
        }
    }
}
