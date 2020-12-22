package dev.dmitrij.kuzmiciov.app.util.file;

import javafx.stage.FileChooser;

public abstract class FileManager {
    protected final static FileChooser fileChooser = new FileChooser();

    static {
        for (var ext : Extensions.values())
            fileChooser.getExtensionFilters().add(ext.filter);
        fileChooser.setSelectedExtensionFilter(fileChooser.getExtensionFilters().get(0));
    }

    protected enum Extensions {
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
