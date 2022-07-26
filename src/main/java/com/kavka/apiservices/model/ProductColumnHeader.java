package com.kavka.apiservices.model;

public enum ProductColumnHeader {

    SKU("sku"),
    ACTUAL_PRODUCT_WEIGHT("ActualProductWeight"),
    ARTIST("artist"),
    ARTIST_PRINT_SERIAL("ArtistPrintSerial"),
    BRAND_NAME_WAYFAIR("brandName-wayfair"),
    CARTON_DEPTH("CartonDepth"),
    CARTON_HEIGHT("CartonHeight"),
    CARTON_WIDTH("CartonWidth"),
    COLOR_WAYFAIR("COLOR-wayfair"),
    COUNTRY_OF_MANUFACTURE("CountryofManufacture"),
    CT_PRINT_TEMPLATE("CTPrintTemplate"),
    CT_SKU("CTSku"),
    DESCRIPTION_WAYFAIR("description_wayfair"),
    FEATURE1_WAYFAIR("feature1-wayfair"),
    FEATURE2_WAYFAIR("feature2-wayfair"),
    FEATURE3_WAYFAIR("feature3-wayfair"),
    FEATURE4_WAYFAIR("feature4-wayfair"),
    FEATURE5_WAYFAIR("feature5-wayfair"),
    FEATURE6_WAYFAIR("feature6-wayfair"),
    HOLIDAY_WAYFAIR("HOLIDAY-wayfair"),
    KAVKA_COLLECTION("kavkaCollection"),
    LEAD_TIME_HOURS_WAYFAIR("LeadTimeHours-wayfair"),
    MAIN_IMAGE_URL("MainImageURL"),
    MSRP_WAYFAIR("msrp-wayfair"),
    OTHER_IMAGE1("OtherImage1"),
    OTHER_IMAGE2("OtherImage2"),
    OTHER_IMAGE3("OtherImage3"),
    OTHER_IMAGE4("OtherImage4"),
    OTHER_IMAGE5("OtherImage5"),
    OTHER_IMAGE6("OtherImage6"),
    OTHER_IMAGE7("OtherImage7"),
    OTHER_IMAGE8("OtherImage8"),
    PRINT_SIZE("PrintSize"),
    PRODUCT_DIMENSTIONS("ProductDimensions"),
    PRODUCT_MAX_DEPTH("ProductMaxDepth"),
    PRODUCT_MAX_HEIGHT("ProductMaxHeight"),
    PRODUCT_MAX_WIDTH("ProductMaxWidth"),
    PRODUCT_WEIGHT("productWeight"),
    PRODUCT_TYPE_WAYFAIR("PRODUCT_TYPE-wayfair"),
    REPLACEMENT_TIME_HOURS_WAYFAIR("ReplacementTimeHours-wayfair"),
    SEASON_WAYFAIR("SEASON-wayfair"),
    SHIPPING_WEIGHT_WAYFAIR("ShippingWeight-wayfair"),
    SHIP_TYPE_WAYFAIR("ShipType-wayfair"),
    SHOPIFY_TAGS("shipifyTags"),
    TITLE("Title"),
    UPC("UPC"),
    WPRICE_WAYFAIR("wPrice-wayfair");

    final String value;

    ProductColumnHeader(String value) {
        this.value = value;
    }
}
