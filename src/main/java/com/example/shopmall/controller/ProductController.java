package com.example.shopmall.controller;

import com.example.shopmall.entity.Product;
import com.example.shopmall.entity.User;
import com.example.shopmall.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<Product> allProducts = productService.getAllProducts();
        int productCount = Math.min(allProducts.size(), 8);
        model.addAttribute("products", allProducts.subList(0, productCount));
        return "index";
    }

    @GetMapping("/products/{id}")
    public String productDetail(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        if (product == null) {
            return "redirect:/";
        }
        model.addAttribute("product", product);
        return "product_detail";
    }

    @GetMapping("/admin/add")
    public String addForm(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/login";
        }
        model.addAttribute("product", new Product());
        model.addAttribute("categories", productService.getDefaultCategories());
        return "product_form";
    }

    @PostMapping("/admin/add")
    public String addProduct(@ModelAttribute Product product,
                             @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        if (!imageFile.isEmpty()) {
            String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
            String uploadDir = System.getProperty("user.dir") + "/uploaded-images/";
            File saveFile = new File(uploadDir + fileName);
            saveFile.getParentFile().mkdirs();
            imageFile.transferTo(saveFile);
            product.setImageUrl("/images/" + fileName);
        }

        productService.saveProduct(product);
        return "redirect:/";
    }

    @GetMapping("/products/edit/{id}")
    public String editProductForm(@PathVariable Long id, Model model) {
        Product product = productService.findById(id);
        model.addAttribute("product", product);
        return "edit_product";
    }

    @PostMapping("/products/update")
    public String updateProduct(@ModelAttribute Product product) {
        productService.save(product);
        return "redirect:/products";
    }

    @PostMapping("/admin/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return "redirect:/admin/products"; // 삭제 후 이동 경로
    }

    // ✅ 관리자 상품 목록 추가
    @GetMapping("/admin/products")
    public String adminProductList(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/login";
        }

        List<Product> products = productService.getAllProducts();

        model.addAttribute("products", products);
        model.addAttribute("categories", productService.getAllCategories());
        model.addAttribute("selectedCategory", null);
        model.addAttribute("keyword", null);
        model.addAttribute("isAdminView", true); // 필요 시 뷰에서 구분 가능

        return "product_list"; // 기존 product_list.html 재사용
    }

    @GetMapping("/products")
    public String listProducts(@RequestParam(value = "category", required = false) String category,
                               @RequestParam(value = "keyword", required = false) String keyword,
                               Model model) {
        List<Product> products;

        if (keyword != null && !keyword.isEmpty()) {
            products = productService.searchProducts(keyword);
        } else if (category != null && !category.isEmpty()) {
            products = productService.getProductsByCategory(category);
        } else {
            products = productService.getAllProducts();
        }

        Set<String> allCategories = new HashSet<>(productService.getDefaultCategories());
        allCategories.addAll(productService.getAllCategories());
        List<String> categories = allCategories.stream()
                .filter(Objects::nonNull)
                .sorted()
                .collect(Collectors.toList());

        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("keyword", keyword);
        model.addAttribute("isAdminView", false); // 사용자 모드

        return "product_list";
    }
}

