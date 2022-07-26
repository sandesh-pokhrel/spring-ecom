package com.kavka.apiservices.service;

import com.kavka.apiservices.exception.InvalidOperationException;
import com.kavka.apiservices.exception.NotFoundException;
import com.kavka.apiservices.model.Product;
import com.kavka.apiservices.model.ProductCategory;
import com.kavka.apiservices.model.ProductColumnHeader;
import com.kavka.apiservices.model.ProductDetail;
import com.kavka.apiservices.util.GeneralUtil;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductFileService extends FileService<ProductColumnHeader>{

    private final JdbcTemplate jdbcTemplate;
    private final ProductService productService;
    private final GeneralUtil generalUtil;
    private final ProductCategoryService productCategoryService;
    private final ProductDetailService productDetailService;

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

    public void loadFromExcelWhole(XSSFWorkbook workbook) {
        List<Map<ProductColumnHeader, Object>> listObjectMap = createMapFromWorkBook(workbook, ProductColumnHeader.values());
        Product savedProduct = null;
        Map<String, ProductCategory> productCategoryMap = new HashMap<>();
        for (Map<ProductColumnHeader, Object> objectMap : listObjectMap) {
            String sku = generalUtil.getMatchingTypeValue(objectMap.get(ProductColumnHeader.SKU), String.class);
            String productCategoryCode = sku.substring(0, sku.indexOf("-"));
            Integer productId = null;
            try {
                productId = getProductIdByCode(generalUtil.getMatchingTypeValue(objectMap.get(ProductColumnHeader.ARTIST_PRINT_SERIAL), String.class));
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
            if (Objects.isNull(productId)) {
                if (!productCategoryMap.containsKey(productCategoryCode)) {
                    ProductCategory productCategory = productCategoryService.getByCode(productCategoryCode);
                    if (Objects.isNull(productCategory)) throw new InvalidOperationException("Bad excel file!");
                    productCategoryMap.put(productCategoryCode, productCategory);
                }
                Product product = Product.builder()
                        .artist(generalUtil.getMatchingTypeValue(objectMap.get(ProductColumnHeader.ARTIST), String.class))
                        .code(generalUtil.getMatchingTypeValue(objectMap.get(ProductColumnHeader.ARTIST_PRINT_SERIAL), String.class))
                        .colorWayfair(generalUtil.getMatchingTypeValue(objectMap.get(ProductColumnHeader.COLOR_WAYFAIR), String.class))
                        .countryOfManufacture(generalUtil.getMatchingTypeValue(objectMap.get(ProductColumnHeader.COUNTRY_OF_MANUFACTURE), String.class))
                        .description(generalUtil.getMatchingTypeValue(objectMap.get(ProductColumnHeader.DESCRIPTION_WAYFAIR), String.class))
                        .featureWayfairOne(generalUtil.getMatchingTypeValue(objectMap.get(ProductColumnHeader.FEATURE1_WAYFAIR), String.class))
                        .featureWayfairTwo(generalUtil.getMatchingTypeValue(objectMap.get(ProductColumnHeader.FEATURE2_WAYFAIR), String.class))
                        .featureWayfairThree(generalUtil.getMatchingTypeValue(objectMap.get(ProductColumnHeader.FEATURE3_WAYFAIR), String.class))
                        .featureWayfairFour(generalUtil.getMatchingTypeValue(objectMap.get(ProductColumnHeader.FEATURE4_WAYFAIR), String.class))
                        .featureWayfairFive(generalUtil.getMatchingTypeValue(objectMap.get(ProductColumnHeader.FEATURE5_WAYFAIR), String.class))
                        .featureWayfairSix(generalUtil.getMatchingTypeValue(objectMap.get(ProductColumnHeader.FEATURE6_WAYFAIR), String.class))
                        .holidayWayfair(generalUtil.getMatchingTypeValue(objectMap.get(ProductColumnHeader.HOLIDAY_WAYFAIR), String.class))
                        .colorWayfair(generalUtil.getMatchingTypeValue(objectMap.get(ProductColumnHeader.COLOR_WAYFAIR), String.class))
                        .kavkaCollection(generalUtil.getMatchingTypeValue(objectMap.get(ProductColumnHeader.KAVKA_COLLECTION), String.class))
                        .leadTimeHoursWayfair(generalUtil.getMatchingTypeValue(objectMap.get(ProductColumnHeader.LEAD_TIME_HOURS_WAYFAIR), Double.class))
                        .replacementTimeHoursWayfair(generalUtil.getMatchingTypeValue(objectMap.get(ProductColumnHeader.REPLACEMENT_TIME_HOURS_WAYFAIR), Double.class))
                        .shipTypeWayfair(generalUtil.getMatchingTypeValue(objectMap.get(ProductColumnHeader.SHIP_TYPE_WAYFAIR), String.class))
                        .shopifyTags(generalUtil.getMatchingTypeValue(objectMap.get(ProductColumnHeader.SHOPIFY_TAGS), String.class))
                        .name(generalUtil.getMatchingTypeValue(objectMap.get(ProductColumnHeader.TITLE), String.class))
                        .imageUrl(generalUtil.getMatchingTypeValue(objectMap.get(ProductColumnHeader.MAIN_IMAGE_URL), String.class))
                        .productCategory(productCategoryMap.get(productCategoryCode))
                        .build();
                savedProduct = productService.save(product);
            }
            if (Objects.nonNull(savedProduct)) {
                // TODO: excel does not contain tier prices verify this
                ProductDetail productDetail = ProductDetail.builder()
                        .code(sku.substring(sku.indexOf("-")+1, sku.lastIndexOf("-")))
                        .sku(sku)
                        .ctSku(String.valueOf(objectMap.get(ProductColumnHeader.CT_SKU)))
                        .ctPrintTemplate(generalUtil.getMatchingTypeValue(objectMap.get(ProductColumnHeader.CT_PRINT_TEMPLATE), String.class))
                        .cartonDepth(generalUtil.getMatchingTypeValue(objectMap.get(ProductColumnHeader.CARTON_DEPTH), Double.class))
                        .cartonHeight(generalUtil.getMatchingTypeValue(objectMap.get(ProductColumnHeader.CARTON_HEIGHT), Double.class))
                        .cartonWidth(generalUtil.getMatchingTypeValue(objectMap.get(ProductColumnHeader.CARTON_WIDTH), Double.class))
                        .otherImageOne(generalUtil.getMatchingTypeValue(objectMap.get(ProductColumnHeader.OTHER_IMAGE1), String.class))
                        .otherImageTwo(generalUtil.getMatchingTypeValue(objectMap.get(ProductColumnHeader.OTHER_IMAGE2), String.class))
                        .otherImageThree(generalUtil.getMatchingTypeValue(objectMap.get(ProductColumnHeader.OTHER_IMAGE3), String.class))
                        .otherImageFour(generalUtil.getMatchingTypeValue(objectMap.get(ProductColumnHeader.OTHER_IMAGE4), String.class))
                        .otherImageFive(generalUtil.getMatchingTypeValue(objectMap.get(ProductColumnHeader.OTHER_IMAGE5), String.class))
                        .otherImageSix(generalUtil.getMatchingTypeValue(objectMap.get(ProductColumnHeader.OTHER_IMAGE6), String.class))
                        .otherImageSeven(generalUtil.getMatchingTypeValue(objectMap.get(ProductColumnHeader.OTHER_IMAGE7), String.class))
                        .otherImageEight(generalUtil.getMatchingTypeValue(objectMap.get(ProductColumnHeader.OTHER_IMAGE8), String.class))
                        .mainImageUrl(generalUtil.getMatchingTypeValue(objectMap.get(ProductColumnHeader.MAIN_IMAGE_URL), String.class))
                        .printSize(generalUtil.getMatchingTypeValue(objectMap.get(ProductColumnHeader.PRINT_SIZE), String.class))
                        .productDimensions(generalUtil.getMatchingTypeValue(objectMap.get(ProductColumnHeader.PRODUCT_DIMENSTIONS), String.class))
                        .productMaxDepth(generalUtil.getMatchingTypeValue(objectMap.get(ProductColumnHeader.PRODUCT_MAX_DEPTH), Double.class))
                        .productMaxHeight(generalUtil.getMatchingTypeValue(objectMap.get(ProductColumnHeader.PRODUCT_MAX_HEIGHT), Double.class))
                        .productMaxWidth(generalUtil.getMatchingTypeValue(objectMap.get(ProductColumnHeader.PRODUCT_MAX_WIDTH), Double.class))
                        .productWeight(generalUtil.getMatchingTypeValue(objectMap.get(ProductColumnHeader.PRODUCT_WEIGHT), Double.class))
                        .productTypeWayfair(generalUtil.getMatchingTypeValue(objectMap.get(ProductColumnHeader.PRODUCT_TYPE_WAYFAIR), String.class))
                        .shippingWeight(generalUtil.getMatchingTypeValue(objectMap.get(ProductColumnHeader.SHIPPING_WEIGHT_WAYFAIR), Double.class))
                        .price(generalUtil.getMatchingTypeValue(objectMap.get(ProductColumnHeader.WPRICE_WAYFAIR), Double.class))
                        .product(savedProduct)
                        .build();
                this.productDetailService.save(productDetail);
            }
        }
    }

}
