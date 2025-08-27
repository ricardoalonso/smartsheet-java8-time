package com.smartsheet.samples;

import com.smartsheet.api.Smartsheet;
import com.smartsheet.api.SmartsheetFactory;
import com.smartsheet.api.models.Cell;
import com.smartsheet.api.models.Column;
import com.smartsheet.api.models.Row;
import com.smartsheet.api.models.Sheet;

import java.text.SimpleDateFormat;
import java.util.*;


public class JavaDateTest {
    static {
        // These lines enable logging to the console
        //System.setProperty("Smartsheet.trace.parts", "RequestBodySummary,ResponseBodySummary");
        //System.setProperty("Smartsheet.trace.pretty", "true");
    }

    private static final Long SHEET_ID = 8423189587644292L; // Example sheet Id, replace with your own

    // The API identifies columns by Id, but it's more convenient to refer to column names
    private static HashMap<String, Long> columnMap = new HashMap<String, Long>();   // Map from friendly column name to column Id

    public static void main(final String[] args) {

        try {
            // Initialize client. Gets API access token from SMARTSHEET_ACCESS_TOKEN variable
            Smartsheet smartsheet = SmartsheetFactory.createDefaultClient();

            Sheet sheet = smartsheet.sheetResources().getSheet(SHEET_ID);

            // Load the entire sheet
            sheet = smartsheet.sheetResources().getSheet(sheet.getId());
            System.out.println("Loaded " + sheet.getRows().size() + " rows from sheet: " + sheet.getName());

            // Build the column map for later reference
            for (Column column : sheet.getColumns()){
                System.out.println("Column name: " + column.getTitle() + ", Type: " + column.getType());
                columnMap.put(column.getTitle(), column.getId());
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date toStore = dateFormat.parse("26/08/2025");
            System.out.println("Current date: " + (new Date()));
            System.out.println("Date to store: " + toStore);
            
            Cell name = new Cell();
            name.setColumnId(columnMap.get("Name"));
            name.setValue("John Doe");
            Cell date = new Cell();
            date.setColumnId(columnMap.get("Date"));
            date.setValue(toStore);
            
            Row row = new Row();
            //row.setId(UUID.randomUUID().getMostSignificantBits()); // Set to null to create a new row
            row.setCells(Arrays.asList(name, date));

            List<Row> rows = new ArrayList<Row>();
            rows.add(row);

            System.out.println("Writing " + rows.size() + " rows back to sheet id " + sheet.getId());
            smartsheet.sheetResources().rowResources().addRows(sheet.getId(), rows);

            System.out.println("Reading back the data to verify");
            Sheet sheetNew = smartsheet.sheetResources().getSheet(sheet.getId());
            System.out.println("Loaded " + sheet.getRows().size() + " rows from sheet: " + sheet.getName());
            List<Row> rowsNew = sheetNew.getRows();
            for (Row r : rowsNew) {
                System.out.print("Row #" + r.getRowNumber() + " (" + r.getId() + "): ");
                System.out.print(" Name=\"" + r.getCells().get(0).getValue() + "\", ");
                System.out.print(" Date=\"" + r.getCells().get(1).getValue() + "\" ");
                System.out.print("(" + r.getCells().get(1).getValue().getClass() + ", " +
                   columnMap.get("Date") + " )");
                System.out.println();
            }

            System.out.println("Done");

        } catch (Exception ex) {
            System.out.println("Exception : " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // Helper function to find cell in a row
    static Cell getCellByColumnName(Row row, String columnName) {
        Long colId = columnMap.get(columnName);

        return row.getCells().stream()
                .filter(cell -> colId.equals((Long) cell.getColumnId()))
                .findFirst()
                .orElse(null);

    }

}
