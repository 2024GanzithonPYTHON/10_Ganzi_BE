package com.example.APT.repository;

import com.example.APT.entity.Category;
import com.example.APT.entity.Member;
import com.example.APT.entity.UserCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCategoryRepository extends JpaRepository<UserCategory, Long> {
    @Query("SELECT CASE WHEN COUNT(uc) > 0 THEN true ELSE false END " +
            "FROM UserCategory uc " +
            "WHERE uc.user = :user AND uc.category = :category")
    boolean existsByUserAndCategory(@Param("user") Member user, @Param("category") Category category);
}

