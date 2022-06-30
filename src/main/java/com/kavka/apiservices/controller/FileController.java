package com.kavka.apiservices.controller;

import com.kavka.apiservices.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @GetMapping("/load/products")
    public void loadProducts(@RequestParam String path) {
        fileService.loadFromExcel(path, "INSERT INTO product (artist,code,color_wayfair,country_of_manufacture,description,feature_wayfair1,feature_wayfair2,feature_wayfair3,feature_wayfair4,feature_wayfair5,feature_wayfair6,holiday_wayfair,kavka_collection,lead_time_hours_wayfair,replacement_time_hours_wayfair,ship_type_wayfair,shopify_tags,name,product_category_id,image_url) VALUES (");
    }

    @GetMapping("/load/product-details")
    public void loadProductDetails(@RequestParam String path) {
        fileService.loadFromExcel(path, "INSERT INTO product_detail (code,sku,ct_sku,ct_print_template,carton_depth,carton_height,carton_width,other_image2,other_image3,other_image4,other_image5,other_image6,other_image7,other_image8,main_image_url,print_size,product_dimensions,product_max_depth,product_max_height,product_max_width,product_weight,other_image1,product_id,tier_one_price,tier_two_price,tier_three_price,product_type_wayfair,shipping_weight,price) VALUES (");
    }
}
