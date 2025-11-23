package com.ay_za.ataylar_technic.service.base;

import com.ay_za.ataylar_technic.dto.ProductInventoryCategoryDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductInventoryCategoryServiceImpl {
    ProductInventoryCategoryDto createCategory(ProductInventoryCategoryDto dto);

    ProductInventoryCategoryDto updateCategory(String id, ProductInventoryCategoryDto dto);

    void deleteCategory(String id);

    @Transactional(readOnly = true)
    ProductInventoryCategoryDto getCategoryById(String id);

    @Transactional(readOnly = true)
    List<ProductInventoryCategoryDto> getAllCategories();

    @Transactional(readOnly = true)
    List<ProductInventoryCategoryDto> getMainCategories();

    @Transactional(readOnly = true)
    List<ProductInventoryCategoryDto> getSubCategories(String parentId);

    @Transactional(readOnly = true)
    List<ProductInventoryCategoryDto> getCategoryTree();

    @Transactional(readOnly = true)
    List<ProductInventoryCategoryDto> getActiveCategories();

    @Transactional(readOnly = true)
    List<ProductInventoryCategoryDto> searchCategories(String name);

    ProductInventoryCategoryDto getById(String id);
}
