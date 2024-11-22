package com.example.APT.service;

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
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserCategoryRepository userCategoryRepository;

    // userId를 사용하여 관심 카테고리 조회
    public List<String> getInterestedCategoriesByUserName(String userName) {
        return categoryRepository.findCategoriesByUserName(userName);
    }
    public List<Long> getInterestedIdsByUserName(String userName) {
        return categoryRepository.findIdsByUserName(userName);
    }
    public Long getInterestedIdByCategoryName(String categoryName) {
        return categoryRepository.findIdByCategoryName(categoryName);
    }




    public void addUserCategories(List<String> categoryNames, Member currentUser) {
        // 1. 모든 카테고리 조회
        List<Category> categories = categoryRepository.findByCategoryNameIn(categoryNames);

        for (Category category : categories) {
            // 2. 중복 확인
            boolean exists = userCategoryRepository.existsByUserAndCategory(currentUser, category);
            if (exists) {
                continue; // 이미 존재하면 스킵
            }

            // 3. UserCategory 생성 및 저장
            UserCategory userCategory = new UserCategory();
            userCategory.setUser(currentUser);
            userCategory.setCategory(category);
            userCategoryRepository.save(userCategory);
        }
    }
}
