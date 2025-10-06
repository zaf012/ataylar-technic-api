package com.ay_za.ataylar_technic.exception;

public class CategoryDeletionException extends InventoryCategoryException {

    public CategoryDeletionException(String categoryId) {
        super("Bu kategori silinemez. Alt kategorileri mevcut: " + categoryId, "CATEGORY_CANNOT_DELETE");
    }

    public CategoryDeletionException(String categoryName, int subCategoryCount) {
        super("'" + categoryName + "' kategorisi silinemez. " + subCategoryCount + " adet alt kategorisi mevcut", "CATEGORY_CANNOT_DELETE");
    }
}
