package dev.dmitrij.kuzmiciov.app.util.file;

import dev.dmitrij.kuzmiciov.app.util.Utility;
import javafx.stage.FileChooser;

/**
 * This class is a base for {@link Loader} and {@link Saver} classes for this application.
 * It contains the enum, describing the file extensions that are supported in this application
 * and an instance of {@link FileChooser} with those extensions.
 * @see Extensions
 */

public abstract class FileManager implements Utility {
    protected final static FileChooser fileChooser = new FileChooser();

    static {
        for (var ext : Extensions.values())
            fileChooser.getExtensionFilters().add(ext.filter);
        fileChooser.setSelectedExtensionFilter(fileChooser.getExtensionFilters().get(0));
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
