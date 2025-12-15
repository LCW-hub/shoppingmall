package com.example.shopmall.repository;

import com.example.shopmall.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // 카테고리로 상품 목록 조회
    List<Product> findByCategory(String category);

    // 상품명에 특정 키워드가 포함된 상품 목록 조회 (검색 기능)
    List<Product> findByNameContainingIgnoreCase(String keyword);

    // 모든 카테고리 목록을 중복 없이 조회
    @Query("SELECT DISTINCT p.category FROM Product p ORDER BY p.category")
    List<String> findDistinctCategories();
}