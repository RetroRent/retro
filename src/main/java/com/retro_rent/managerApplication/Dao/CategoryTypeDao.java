package com.retro_rent.managerApplication.Dao;


import com.retro_rent.managerApplication.modle.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("CategoryTypeRepository")
public interface CategoryTypeDao extends JpaRepository<CategoryType, Long> {
    Boolean existsByName(String name);

    List<CategoryType> findAllByType(int type);

    Optional<CategoryType> findByName(String name);
}
