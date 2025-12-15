package com.example.shopmall.controller;

import com.example.shopmall.entity.CartItem;
import com.example.shopmall.entity.User;
import com.example.shopmall.repository.CartItemRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import com.example.shopmall.entity.Product; // 추가

import java.util.List;

@ControllerAdvice // 모든 컨트롤러 전역에서 동작하도록 설정
public class GlobalControllerAdvice {

    @Autowired
    private CartItemRepository cartItemRepository;
    
    @ModelAttribute("menuCategories")
    public List<String> populateCategories() {
        return Product.DEFAULT_CATEGORIES;
    }

    // 모든 요청에 대해 "cartCount"라는 이름으로 모델에 값을 자동으로 추가
    @ModelAttribute("cartCount")
    public int populateCartCount(HttpSession session) {
        // 1. 세션에서 로그인한 사용자 정보 가져오기
        User user = (User) session.getAttribute("user");

        // 2. 로그인하지 않은 경우 개수는 0
        if (user == null) {
            return 0;
        }

        // 3. 로그인한 경우, 해당 사용자의 장바구니 아이템을 모두 가져옴
        List<CartItem> cartItems = cartItemRepository.findByUser(user);

        // 4. 각 아이템의 수량(Quantity)을 모두 더해서 반환
        // (상품 종류 수가 아니라, 담은 물건의 총 개수를 원하시는 경우)
        return cartItems.size();
    }
}