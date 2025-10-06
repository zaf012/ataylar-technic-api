package com.ay_za.ataylar_technic.exception;

public class CategoryAlreadyExistsException extends InventoryCategoryException {

    public CategoryAlreadyExistsException(String categoryName) {
        super("Bu kategoride aynı isimde başka bir kategori zaten mevcut: " + categoryName, "CATEGORY_ALREADY_EXISTS");
    }

    public CategoryAlreadyExistsException(String categoryName, String parentCategoryName) {
        super("'" + parentCategoryName + "' kategorisinde '" + categoryName + "' adında zaten bir kategori mevcut", "CATEGORY_ALREADY_EXISTS");
    }
}
