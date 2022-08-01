package com.kavka.apiservices.service;

import com.kavka.apiservices.common.Constants;
import com.kavka.apiservices.exception.NotFoundException;
import com.kavka.apiservices.model.Product;
import com.kavka.apiservices.model.ProductCategory;
import com.kavka.apiservices.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class ProductService extends GenericService {

    private final ProductRepository productRepository;
    private final ProductCategoryService productCategoryService;

    public Product getById(Integer id) {
        return this.productRepository.findById(id).orElse(null);
    }

    public Product getByCode(String code) {
        return this.productRepository.findByCode(code).orElseThrow(() -> new NotFoundException("Product code does not exist"));
    }

    public Page<Product> getAll(Map<String, String> paramMap) {
        Pageable pageable = getPageable(paramMap, Constants.PRODUCT_DEFAULT_ORDER_BY_COLUMN);
        String searchText = getSearchString(paramMap);
        return this.productRepository.search(searchText, pageable);
    }

    public List<Product> getAllByProductCategory(Integer productCategoryId) {
        ProductCategory productCategory = this.productCategoryService.getById(productCategoryId);
        return this.productRepository.findAllByProductCategory(productCategory);
    }

    public Product save(Product product) {
        return this.productRepository.saveAndFlush(product);
    }
}
