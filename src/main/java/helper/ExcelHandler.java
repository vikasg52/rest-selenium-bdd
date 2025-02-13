package helper;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

public class ExcelHandler {
    private String filePath;

    // Constructor to set file path
    public ExcelHandler(String filePath) {
        this.filePath = filePath;
    }

    /** ✅ Create a new Excel file */
    public void createExcel() {
        try (Workbook workbook = new XSSFWorkbook(); FileOutputStream fos = new FileOutputStream(filePath)) {
            Sheet sheet = workbook.createSheet("Sheet1");

            Row row = sheet.createRow(0);
            row.createCell(0).setCellValue("ID");
            row.createCell(1).setCellValue("Name");
            row.createCell(2).setCellValue("Age");

            Row row1 = sheet.createRow(1);
            row1.createCell(0).setCellValue(1);
            row1.createCell(1).setCellValue("Vikas");
            row1.createCell(2).setCellValue(30);

            workbook.write(fos);
            System.out.println("Excel file created successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** ✅ Read data from Excel */
    public void readExcel() {
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                for (Cell cell : row) {
                    switch (cell.getCellType()) {
                        case STRING:
                            System.out.print(cell.getStringCellValue() + "\t");
                            break;
                        case NUMERIC:
                            System.out.print(cell.getNumericCellValue() + "\t");
                            break;
                        default:
                            System.out.print(" \t");
                    }
                }
                System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** ✅ Update a specific cell in Excel */
    public void updateExcel(int rowNumber, int columnNumber, String newValue) {
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            Row row = sheet.getRow(rowNumber);
            if (row != null) {
                Cell cell = row.getCell(columnNumber);
                if (cell != null) {
                    cell.setCellValue(newValue);
                }
            }

            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }
            System.out.println("Excel file updated successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** ✅ Delete a row from Excel */
    public void deleteRow(int rowIndexToDelete) {
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            Row row = sheet.getRow(rowIndexToDelete);
            if (row != null) {
                sheet.removeRow(row);
                System.out.println("Row deleted successfully.");
            }

            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** ✅ Append a new row to Excel */
    public void appendRow(int id, String name, int age) {
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            int lastRowNum = sheet.getLastRowNum();
            Row newRow = sheet.createRow(lastRowNum + 1);

            newRow.createCell(0).setCellValue(id);
            newRow.createCell(1).setCellValue(name);
            newRow.createCell(2).setCellValue(age);

            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }
            System.out.println("Data appended successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** ✅ Check if file exists */
    public boolean fileExists() {
        File file = new File(filePath);
        return file.exists();
    }
}
