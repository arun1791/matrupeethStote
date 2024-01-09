package com.matrupeeth.store.repositories;

import com.matrupeeth.store.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category,String> {

//    @Query(value = "SELECT * FROM matrupeeth.categories where category_title LIKE '%+:keyword%';" ,nativeQuery = true)
    List<Category> findByTitleContaining(String keyword);
}
