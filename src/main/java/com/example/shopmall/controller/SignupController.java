package com.example.shopmall.controller;

import com.example.shopmall.entity.User;
import com.example.shopmall.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class SignupController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("user", new User());
        return "signup"; // signup.html 보여줌
    }

    @PostMapping("/signup")
    public String signupSubmit(@ModelAttribute User user, Model model) {
        // username 중복 체크
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            model.addAttribute("error", "이미 사용 중인 아이디입니다.");
            return "signup";
        }

        userRepository.save(user); // 사용자 저장
        return "redirect:/login"; // 회원가입 성공 후 로그인 페이지로 이동
    }
}


