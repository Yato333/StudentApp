package com.app.util.file;

import com.app.App;
import com.app.controller.RootController;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

/**
 * This file manager saves data stored in this application in a file selected by the user.
 * @see Extensions Supported extensions
 */

public final class Saver extends FileManager {
    private Saver() {}

    private static void saveSCV(@NotNull File file) {
        try {
            StringBuilder result = new StringBuilder();
            var group = Objects.requireNonNull(RootController.getInstance().getCurrentGroup());
            result.append(group.getName()).append(LS);

            result.append("First Name" + ';' + "Last Name").append(LS);
            for(var student : group.getStudents()) {
                result.append(student.getFirstName()).append(';').append(student.getLastName()).append(LS);
            }

            var writer = new FileWriter(file);
            writer.write(result.toString());
            writer.close();
        } catch (IOException e) {
            showErrorAlert(e);
        }
    }

    private static void savePDF(@NotNull File file) {
        // TODO: finish this
        /*
        try {
            var group = Objects.requireNonNull(RootController.getInstance().getCurrentGroup());

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();
            PdfPTable table = new PdfPTable(3);

            var groupNameCell = new PdfPCell();
            groupNameCell.setPhrase(new Phrase(group.getName()));
            table.addCell(groupNameCell);

            document.add(table);
            document.close();
        } catch (FileNotFoundException | DocumentException e) {
            showErrorAlert(e);
        }
         */
    }

    private static void saveExcel(@NotNull File file) {
        try {
            var groups = RootController.getInstance().getGroupChoiceBox().getItems();
            var workBook = new XSSFWorkbook();

            var month = Objects.requireNonNull(RootController.getInstance().getCurrentMonth());
            int monthLength = month.lengthOfMonth();

            for(var group : groups) {
                var sheet = workBook.createSheet(group.getName());
                var students = group.getStudents();

                int rowIndex = 0, columnIndex = 0;

                var headerRow0 = sheet.createRow(rowIndex++);
                headerRow0.createCell(columnIndex++).setCellValue("Student Name");
                headerRow0.createCell(columnIndex).setCellValue("Day of " + month.toString());

                columnIndex = 1;
                var headerRow = sheet.createRow(rowIndex++);

                for(int day = 1; day <= monthLength; ++day) {
                    var cell = headerRow.createCell(columnIndex++);
                    cell.setCellValue(day);
                }

                for(var student : students) {
                    columnIndex = 0;
                    var row = sheet.createRow(rowIndex++);
                    row.createCell(columnIndex++).setCellValue(student.toString());
                    for(int day = 1; day <= month.lengthOfMonth(); ++day) {
                        var mark = student.MARKS.get(month.atDay(day));
                        var cell = row.createCell(columnIndex++);
                        if(mark != null)
                            cell.setCellValue(mark.toString());
                    }
                }

                // Merging month day description
                sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, monthLength));
                // Merging Student Name description
                sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));

                for(int i = 0; i <= monthLength; ++i)
                    sheet.autoSizeColumn(i);
            }

            workBook.write(new FileOutputStream(file));
        } catch (IOException e) {
            showErrorAlert(e);
        }
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
