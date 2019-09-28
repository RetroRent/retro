package com.retro_rent.managerApplication.Dao;


import com.retro_rent.managerApplication.modle.CategoryType;
import com.retro_rent.managerApplication.modle.ItemCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("ItemCategoryRepository")
public interface ItemCategoryDao extends JpaRepository<ItemCategory, Long> {
    List<ItemCategory> findAllByFCategory(CategoryType categoryType);
    List<ItemCategory> findAllByFCategoryAndAndSCategory(CategoryType f_category, CategoryType s_category);
    ItemCategory findByFCategoryAndSCategoryAndTCategory(CategoryType f_category, CategoryType s_category, CategoryType t_category);

}
