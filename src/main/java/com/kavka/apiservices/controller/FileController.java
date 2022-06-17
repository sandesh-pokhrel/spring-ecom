package com.kavka.apiservices.controller;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.util.Iterator;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

    private final JdbcTemplate jdbcTemplate;

    @GetMapping("/load/products")
    public void loadProducts(@RequestParam String path) {

        try {
            FileInputStream file = new FileInputStream(path);
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.rowIterator();
            rowIterator.next();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                StringBuilder productInsertQuery = new StringBuilder("INSERT INTO product (artist,code,color_wayfair,country_of_manufacture,description,feature_wayfair1,feature_wayfair2,feature_wayfair3,feature_wayfair4,feature_wayfair5,feature_wayfair6,holiday_wayfair,kavka_collection,lead_time_hours_wayfair,replacement_time_hours_wayfair,ship_type_wayfair,shopify_tags,name,product_category_id) VALUES (");


                for (int i=0; i<row.getLastCellNum(); i++) {
                    Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    switch (cell.getCellType()) {
                        case NUMERIC:
                            productInsertQuery.append(cell.getNumericCellValue());
                            break;
                        case BOOLEAN:
                            productInsertQuery.append(cell.getBooleanCellValue());
                            break;
                        case STRING:
                            String cellValue = cell.getStringCellValue().replace("'", "''");
                            productInsertQuery.append(String.format("'%s'", cellValue));
                            break;
                        case BLANK:
                        default:
                            productInsertQuery.append("''");
                    }
                    if (i != row.getLastCellNum()-1) productInsertQuery.append(",");
                }

                productInsertQuery.append(")");
                jdbcTemplate.update(productInsertQuery.toString());
            }
            workbook.close();
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
