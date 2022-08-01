package com.kavka.apiservices.repository;

import com.kavka.apiservices.model.Product;
import com.kavka.apiservices.model.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findAllByProductCategory(ProductCategory productCategory);

    Optional<Product> findByCode(String code);

    @Query("SELECT p FROM Product p " +
            "WHERE lower(p.name) like %:searchText% OR lower(p.artist) like %:searchText% OR " +
            "lower(p.shopifyTags) like %:searchText% OR lower(p.description) like %:searchText% OR " +
            "lower(p.countryOfManufacture) like %:searchText%")
    Page<Product> search(@Param("searchText") String searchText, Pageable pageable);
}
