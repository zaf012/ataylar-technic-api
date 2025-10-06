package com.ay_za.ataylar_technic.exception;

public class CategoryNotFoundException extends InventoryCategoryException {

    public CategoryNotFoundException(String categoryId) {
        super("Kategori bulunamadı: " + categoryId, "CATEGORY_NOT_FOUND");
    }

    public CategoryNotFoundException(String field, String value) {
        super(field + " ile kategori bulunamadı: " + value, "CATEGORY_NOT_FOUND");
    }
}
