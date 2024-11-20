package com.example.APT.service;

import com.example.APT.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository userCategoryRepository;

    // userId를 사용하여 관심 카테고리 조회
    public List<String> getInterestedCategoriesByUserName(String userName) {
        return userCategoryRepository.findCategoriesByUserName(userName);
    }
}
