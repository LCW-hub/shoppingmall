package com.example.shopmall.service;

import com.example.shopmall.entity.Product;
import java.util.List;

/**
 * 상품 관련 비즈니스 로직을 정의하는 서비스 인터페이스
 */
public interface ProductService {

    /**
     * 전체 상품 목록을 가져옵니다.
     * @return 전체 상품 리스트
     */
    List<Product> getAllProducts();

    /**
     * 특정 ID에 해당하는 상품을 조회합니다.
     * @param id 상품 ID
     * @return 해당 상품 객체 (없으면 null 또는 예외 처리 필요)
     */
    Product getProductById(Long id);

    /**
     * 상품을 저장합니다 (등록 또는 수정).
     * @param product 저장할 상품 객체
     */
    void saveProduct(Product product);

    /**
     * 상품을 ID로 삭제합니다.
     * @param id 삭제할 상품 ID
     */
    void deleteProduct(Long id);

    /**
     * 특정 카테고리에 해당하는 상품 목록을 조회합니다.
     * @param category 카테고리명
     * @return 해당 카테고리의 상품 리스트
     */
    List<Product> getProductsByCategory(String category);

    /**
     * 키워드로 상품명을 검색합니다.
     * @param keyword 검색어
     * @return 검색된 상품 리스트
     */
    List<Product> searchProducts(String keyword);

    /**
     * 데이터베이스에 존재하는 모든 카테고리를 조회합니다.
     * @return 중복 없는 카테고리 리스트
     */
    List<String> getAllCategories();

    /**
     * 기본적으로 제공하는 카테고리 목록을 반환합니다.
     * (ex. '전자제품', '의류', '식품' 등)
     * @return 기본 카테고리 리스트
     */
    List<String> getDefaultCategories();

    /**
     * 상품을 ID로 조회합니다 (일반적으로 getProductById와 동일하거나 대체 용도).
     * @param id 상품 ID
     * @return 조회된 상품
     */
    Product findById(Long id);

    /**
     * 상품 저장 처리 (기본 JPA save 호출 목적).
     * @param product 저장할 상품
     */
    void save(Product product);
}
