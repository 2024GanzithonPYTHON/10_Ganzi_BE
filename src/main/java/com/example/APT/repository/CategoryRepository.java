package com.example.APT.repository;

import com.example.APT.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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
    @Query("""
    SELECT c.id
    FROM Category c
    JOIN c.userCategories uc
    JOIN uc.user u
    WHERE u.loginId = :userName
""")
    List<Long> findIdsByUserName(@Param("userName") String userName);

    @Query("""
    SELECT c.id
    FROM Category c
    WHERE c.categoryName = :categoryName
""")
    Long findIdByCategoryName(@Param("categoryName") String categoryName);

    List<Category> findByCategoryNameIn(List<String> categoryNames);
    Optional<Category> findByCategoryName(String categoryName);
}
