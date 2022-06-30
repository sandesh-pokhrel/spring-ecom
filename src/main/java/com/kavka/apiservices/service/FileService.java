package com.kavka.apiservices.service;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FileService {

    private final JdbcTemplate jdbcTemplate;
    private final ProductService productService;

    private Integer getProductIdByCode(String code) {
        return productService.getByCode(code).getId();
    }


    public void loadFromExcel(XSSFWorkbook workbook, String query) {
        try {

            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.rowIterator();
            rowIterator.next();
            while (rowIterator.hasNext()) {
                Integer productId = null;
                Row row = rowIterator.next();
                StringBuilder insertQuery = new StringBuilder(query);


                for (int i = 0; i < row.getLastCellNum(); i++) {
                    Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    if (query.contains("product_detail") && i == 1 && (Objects.nonNull(cell) && !(cell.getCellType() == CellType.BLANK))) {
                        productId = getProductIdByCode(cell.getStringCellValue().substring(cell.getStringCellValue().lastIndexOf("-")+1));
                    }
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
                    if (i != row.getLastCellNum() - 1) insertQuery.append(",");
                }
                if (Objects.nonNull(productId))
                    insertQuery.append(",").append(productId);
                insertQuery.append(")");
                jdbcTemplate.update(insertQuery.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
