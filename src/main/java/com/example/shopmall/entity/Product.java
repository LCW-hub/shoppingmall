package com.example.shopmall.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Arrays;
import java.util.List;

@Entity
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private int price;
    private String imageUrl;
    private int stock;
    private String category;
    
    // 기본 카테고리 목록
    public static final List<String> DEFAULT_CATEGORIES = Arrays.asList(
    		"CPU",
    	    "메인보드",
    	    "메모리",
    	    "그래픽카드",
    	    "SSD/HDD",
    	    "케이스/파워"
    );
}
