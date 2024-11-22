package com.example.APT.service;

import com.example.APT.dto.CategoryRequestDTO;
import com.example.APT.entity.Category;
import com.example.APT.entity.Member;
import com.example.APT.entity.UserCategory;
import com.example.APT.repository.CategoryRepository;
import com.example.APT.repository.UserCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserCategoryService {

    private final CategoryRepository categoryRepository;
    private final UserCategoryRepository userCategoryRepository;

    public void addUserCategories(CategoryRequestDTO categoryRequestDTO, Member currentUser) {
        List<String> categoryNames = categoryRequestDTO.getCategoryNames(); // DTO에서 카테고리 이름 리스트 추출

        for (String categoryName : categoryNames) {
            Category category = categoryRepository.findByCategoryName(categoryName)
                    .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다: " + categoryName));

            // 중복
            boolean exists = userCategoryRepository.existsByUserAndCategory(currentUser, category);
            if (exists) {
                continue;
            }

            UserCategory userCategory = new UserCategory();
            userCategory.setUser(currentUser);
            userCategory.setCategory(category);

            userCategoryRepository.save(userCategory); // DB 저장
        }
    }
}

