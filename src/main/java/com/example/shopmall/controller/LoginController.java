package com.example.shopmall.controller;

import com.example.shopmall.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String loginForm() {
        return "login"; // login.html
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String username,
                               @RequestParam String password,
                               HttpSession session,
                               Model model) {
    	
        System.out.println("입력한 username: " + username);
        System.out.println("입력한 password: " + password);
        
        // username으로 사용자 조회
        return userRepository.findByUsername(username)
                .filter(user -> user.getPassword().equals(password)) // 비밀번호 비교
                .map(user -> {
                    session.setAttribute("user", user); // 로그인 세션 설정
                    return "redirect:/";
                })
                .orElseGet(() -> {
                    model.addAttribute("error", "아이디 또는 비밀번호가 틀렸습니다.");
                    return "login"; // 다시 로그인 페이지로
                });
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 세션 무효화
        return "redirect:/"; // 메인 페이지로 리다이렉트
    }
    
}



