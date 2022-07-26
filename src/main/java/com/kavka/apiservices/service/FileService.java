package com.kavka.apiservices.service;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.*;

public abstract class FileService<T> {

    public List<Map<T, Object>> createMapFromWorkBook(XSSFWorkbook workbook, T[] columnHeaders) {
        List<Map<T, Object>> listProductRowMap = new ArrayList<>();
        try {

            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.rowIterator();
            rowIterator.next();
            while (rowIterator.hasNext()) {
                Map<T, Object> productRowMap = new HashMap<>();
                Row row = rowIterator.next();
                for (int i = 0; i < row.getLastCellNum(); i++) {
                    Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    switch (cell.getCellType()) {
                        case NUMERIC:
                            productRowMap.put(columnHeaders[i], cell.getNumericCellValue());
                            break;
                        case BOOLEAN:
                            productRowMap.put(columnHeaders[i], cell.getBooleanCellValue());
                            break;
                        case STRING:
                            productRowMap.put(columnHeaders[i], cell.getStringCellValue());
                            break;
                        case BLANK:
                        default:
                            productRowMap.put(columnHeaders[i], null);
                    }
                }
                listProductRowMap.add(productRowMap);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listProductRowMap;
    }
}
