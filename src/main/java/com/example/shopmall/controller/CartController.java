package com.example.shopmall.controller;

import com.example.shopmall.entity.CartItem;
import com.example.shopmall.entity.Product;
import com.example.shopmall.entity.User;
import com.example.shopmall.repository.CartItemRepository;
import com.example.shopmall.repository.ProductRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cart") // 모든 URL이 /cart/로 시작됨
public class CartController {

    @Autowired
    private ProductRepository productRepository; // 상품 정보를 가져오기 위해 사용

    @Autowired
    private CartItemRepository cartItemRepository; // 장바구니 항목 저장/조회용

    /**
     * 장바구니에 상품 추가
     * - 로그인한 유저 세션에서 사용자 정보 가져옴
     * - 이미 장바구니에 해당 상품이 있으면 수량을 증가시킴
     * - 없으면 새로 장바구니 항목을 생성
     */
    @PostMapping("/add/{id}")
    public String addToCart(@PathVariable("id") Long productId,
                            @RequestParam(defaultValue = "1") int quantity, // 수량 파라미터 (기본값 1)
                            HttpSession session) {
        User user = (User) session.getAttribute("user"); // 세션에서 사용자 가져오기
        if (user == null) return "redirect:/login"; // 로그인 안 한 경우 로그인 페이지로 이동

        // 상품 ID로 상품 조회 (없으면 예외 발생)
        Product product = productRepository.findById(productId).orElseThrow();

        // 유저와 상품 기준으로 장바구니 항목 조회
        Optional<CartItem> optional = cartItemRepository.findByUserAndProduct(user, product);
        CartItem cartItem;

        if (optional.isPresent()) {
            // 기존 장바구니 항목이 있으면 수량을 누적
            cartItem = optional.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
            // 새 장바구니 항목 생성
            cartItem = new CartItem();
            cartItem.setUser(user);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
        }

        // DB에 저장
        cartItemRepository.save(cartItem);

        return "redirect:/cart/view"; // 장바구니 페이지로 이동
    }

    /**
     * 장바구니 목록 보기
     * - 로그인한 사용자 기준으로 장바구니 항목들을 조회해서 모델에 담음
     */
    @GetMapping("/view")
    public String viewCart(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        // 로그인한 사용자에 해당하는 장바구니 항목 조회
        List<CartItem> cartItems = cartItemRepository.findByUser(user);

        model.addAttribute("cartItems", cartItems); // 뷰에 전달
        return "cart"; // cart.html로 렌더링
    }

    /**
     * 장바구니 수량 수정
     * - 지정된 상품의 수량을 새로운 값으로 설정함
     */
    @PostMapping("/update/{id}")
    public String updateQuantity(@PathVariable("id") Long productId,
                                 @RequestParam("quantity") int quantity, // 수정할 수량
                                 HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        // 상품 및 해당 유저의 장바구니 항목 조회
        Product product = productRepository.findById(productId).orElseThrow();
        CartItem cartItem = cartItemRepository.findByUserAndProduct(user, product).orElseThrow();

        // 수량 업데이트 후 저장
        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);

        return "redirect:/cart/view"; // 장바구니로 돌아감
    }

    /**
     * 장바구니 항목 삭제
     * - 특정 상품을 장바구니에서 제거
     */
    @PostMapping("/remove/{id}")
    public String removeFromCart(@PathVariable("id") Long productId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        // 상품과 사용자 기준으로 장바구니 항목 찾기
        Product product = productRepository.findById(productId).orElseThrow();
        Optional<CartItem> optional = cartItemRepository.findByUserAndProduct(user, product);

        // 항목이 존재하면 삭제
        optional.ifPresent(cartItemRepository::delete);

        return "redirect:/cart/view"; // 장바구니 페이지로 리다이렉트
    }
}




