package tests;

import helper.ExcelHandler;

public class ExcelHandlerTest {
    public static void main(String[] args) {
        String filePath = "data.xlsx";
        ExcelHandler excelHandler = new ExcelHandler(filePath);

        // Create Excel file if it doesn't exist
        if (!excelHandler.fileExists()) {
            excelHandler.createExcel();
        }

        // Read data
        System.out.println("Reading Excel File:");
        excelHandler.readExcel();

        // Update a cell
        System.out.println("\nUpdating Cell (Row 1, Column 1)...");
        excelHandler.updateExcel(1, 1, "Updated Vikas");
        excelHandler.readExcel();

        // Append a new row
        System.out.println("\nAppending a new row...");
        excelHandler.appendRow(2, "New User", 25);
        excelHandler.readExcel();

        // Delete a row
        System.out.println("\nDeleting Row 1...");
        excelHandler.deleteRow(1);
        excelHandler.readExcel();
    }
}
