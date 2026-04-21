package com.julian.spring_demo.repository;

import com.julian.spring_demo.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long>  {

    boolean existsByName (String name);

}
