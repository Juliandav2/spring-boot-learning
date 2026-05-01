package com.julian.spring_demo.repository;

import com.julian.spring_demo.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByNameContainingIgnoreCase (String name);
    List<Product> findByPriceLessThanEqual (Double maxPrice);
    Page<Product> findByCategoryId (Long categoryId, Pageable pageable);

    Page<Product> findAll (Pageable pageable);
    Page<Product> findByNameContainingIgnoreCase (String name, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :minPrice AND :maxPrice")
    List<Product> findByPriceRange (@Param("minPrice") double minPrice,
                                    @Param("maxPrice") double maxPrice);

    @Query ("SELECT p FROM Product p WHERE p.stock < :minStock")
    List<Product> findLowStock (@Param("minStock") int minStock);

    @Query ("SELECT p FROM Product p WHERE p.category.name = :categoryName")
    List<Product> findByCategoryName (@Param("categoryName") String categoryName);

}
