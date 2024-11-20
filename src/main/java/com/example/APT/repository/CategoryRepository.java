package com.example.APT.repository;

import com.example.APT.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("""
    SELECT c.categoryName
    FROM Category c
    JOIN c.userCategories uc
    JOIN uc.user u
    WHERE u.loginId = :userName
""")
    List<String> findCategoriesByUserName(@Param("userName") String userName);
}
