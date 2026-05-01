package com.julian.spring_demo.repository;

import com.julian.spring_demo.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    boolean existsByName (String name);
}
