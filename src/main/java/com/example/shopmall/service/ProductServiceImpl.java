package com.example.shopmall.service;

import com.example.shopmall.entity.Product;
import com.example.shopmall.repository.CartItemRepository;
import com.example.shopmall.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;  // 추가

    // 생성자에서 두 Repository 모두 주입
    public ProductServiceImpl(ProductRepository productRepository, CartItemRepository cartItemRepository) {
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    @Override
    public List<Product> searchProducts(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }

    @Override
    public List<String> getAllCategories() {
        return productRepository.findDistinctCategories();
    }

    @Override
    public List<String> getDefaultCategories() {
        return Product.DEFAULT_CATEGORIES;
    }

    @Override
    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다. ID: " + id));
    }

    @Override
    public void save(Product product) {
        productRepository.save(product);
    }

    @Override
    @Transactional
    public void deleteProduct(Long productId) {
        cartItemRepository.deleteByProductId(productId); // 장바구니에서 먼저 삭제
        productRepository.deleteById(productId);         // 상품 삭제
    }
}
