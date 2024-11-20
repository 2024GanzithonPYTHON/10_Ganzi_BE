package com.example.APT.repository;

import com.example.APT.entity.UserCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

//@Repository
//public interface UserCategoryRepository extends JpaRepository<UserCategory, Long> {
//    @Query("""
//    SELECT c.categoryName
//    FROM UserCategory uc
//    JOIN uc.category c
//    JOIN uc.user u
//    WHERE u.name = :userName
//""")
//    List<String> findCategoriesByUserName(@Param("userName") String userName);
//}
