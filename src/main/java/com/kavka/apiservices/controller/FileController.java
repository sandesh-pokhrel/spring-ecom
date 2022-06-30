package com.kavka.apiservices.controller;

import com.kavka.apiservices.service.FileService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;
    private static final String PRODUCT_INSERT_QUERY = "INSERT INTO product (artist,code,color_wayfair,country_of_manufacture,description,feature_wayfair1,feature_wayfair2,feature_wayfair3,feature_wayfair4,feature_wayfair5,feature_wayfair6,holiday_wayfair,kavka_collection,lead_time_hours_wayfair,replacement_time_hours_wayfair,ship_type_wayfair,shopify_tags,name,product_category_id,image_url) VALUES (";
    private static final String PRODUCT_DETAIL_INSERT_QUERY = "INSERT INTO product_detail (code,sku,ct_sku,ct_print_template,carton_depth,carton_height,carton_width,other_image2,other_image3,other_image4,other_image5,other_image6,other_image7,other_image8,main_image_url,print_size,product_dimensions,product_max_depth,product_max_height,product_max_width,product_weight,other_image1,tier_one_price,tier_two_price,tier_three_price,product_type_wayfair,shipping_weight,price,product_id) VALUES (";

    @PostMapping("/load/products/file")
    @ResponseStatus(HttpStatus.OK)
    public void loadProductFromFile(@RequestParam MultipartFile excel) throws IOException {
        if (Objects.requireNonNull(excel.getContentType()).contains("spreadsheet")) {
            XSSFWorkbook workbook = new XSSFWorkbook(excel.getInputStream());
            fileService.loadFromExcel(workbook, PRODUCT_INSERT_QUERY);
        }
    }

    @PostMapping("/load/product-details/file")
    @ResponseStatus(HttpStatus.OK)
    public void loadProductDetailsFromFile(@RequestParam MultipartFile excel) throws IOException {
        if (Objects.requireNonNull(excel.getContentType()).contains("spreadsheet")) {
            XSSFWorkbook workbook = new XSSFWorkbook(excel.getInputStream());
            fileService.loadFromExcel(workbook, PRODUCT_DETAIL_INSERT_QUERY);
        }
    }

    @GetMapping("/load/products")
    public void loadProducts(@RequestParam String path) throws IOException {
        FileInputStream file = new FileInputStream(path);
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        fileService.loadFromExcel(workbook, PRODUCT_INSERT_QUERY);
        file.close();
        workbook.close();
    }

    @GetMapping("/load/product-details")
    public void loadProductDetails(@RequestParam String path) throws IOException {
        FileInputStream file = new FileInputStream(path);
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        fileService.loadFromExcel(workbook, PRODUCT_DETAIL_INSERT_QUERY);
        file.close();
        workbook.close();
    }
}
