package com.kavka.apiservices.service;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.util.Iterator;

@Service
@RequiredArgsConstructor
public class FileService {

    private final JdbcTemplate jdbcTemplate;

    public void loadFromExcel(String path, String query) {
        try {
            FileInputStream file = new FileInputStream(path);
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.rowIterator();
            rowIterator.next();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                StringBuilder insertQuery = new StringBuilder(query);


                for (int i=0; i<row.getLastCellNum(); i++) {
                    Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    switch (cell.getCellType()) {
                        case NUMERIC:
                            insertQuery.append(cell.getNumericCellValue());
                            break;
                        case BOOLEAN:
                            insertQuery.append(cell.getBooleanCellValue());
                            break;
                        case STRING:
                            String cellValue = cell.getStringCellValue().replace("'", "''");
                            insertQuery.append(String.format("'%s'", cellValue));
                            break;
                        case BLANK:
                        default:
                            insertQuery.append("''");
                    }
                    if (i != row.getLastCellNum()-1) insertQuery.append(",");
                }

                insertQuery.append(")");
                jdbcTemplate.update(insertQuery.toString());
            }
            workbook.close();
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
