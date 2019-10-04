package com.retro_rent.managerApplication;

import com.retro_rent.managerApplication.Dao.*;
import com.retro_rent.managerApplication.config.AppProperties;
import com.retro_rent.managerApplication.config.FileStorageProperties;
import com.retro_rent.managerApplication.modle.*;
import com.retro_rent.managerApplication.Dao.CategoryTypeDao;
import com.retro_rent.managerApplication.Dao.ItemCategoryDao;
import com.retro_rent.managerApplication.Dao.RoleDao;
import com.retro_rent.managerApplication.modle.CategoryType;
import com.retro_rent.managerApplication.modle.ItemCategory;
import com.retro_rent.managerApplication.modle.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import java.util.Optional;

@SpringBootApplication
@EnableConfigurationProperties({AppProperties.class, FileStorageProperties.class})
public class ManagerApplication  extends SpringBootServletInitializer {

    private static final Logger logger = LoggerFactory.getLogger(ManagerApplication.class);

    public static void main(String[] args) {
        try {
            SpringApplicationBuilder builder = new SpringApplicationBuilder(ManagerApplication.class);
            builder.headless(false).run(args);
            logger.info("Start Analysis Manager Application");
        }
        catch (Exception e) {
            logger.error(e.getMessage());
        }

    }


    @Autowired
    private Environment env;



    @Autowired
    private RoleDao roleDao;
//
    @Autowired
    private CategoryTypeDao categoryTypeDao;

    @Autowired
    private ItemCategoryDao itemCategoryDao;

    @Bean
    public boolean dbInit()
    {
        logger.info("Start Init of Db Static Values from application.property file");
        LoadUsersPermissions();
        LoadCategory();
        return true;
    }

    private void LoadCategory()
    {
        for (String itemC: env.getProperty("spring.db.category.type").split("-")) {
            ItemCategory itemCategory = new ItemCategory();
            int index = 1;
            for (String category : itemC.split(",")) {
                Optional<CategoryType> categoryType = categoryTypeDao.findByName(category);
                if (!categoryType.isPresent()) {
                    CategoryType categoryTypeNew = new CategoryType();
                    categoryTypeNew.setName(category);
                    categoryTypeNew.setType(index);
                    categoryTypeDao.save(categoryTypeNew);
                    itemCategory.setCategory(index, categoryTypeNew);
                } else {
                    itemCategory.setCategory(index, categoryType.get());
                }
                index++;
            }

            if (itemCategoryDao.findByFCategoryAndSCategoryAndTCategory(itemCategory.getfCategory(), itemCategory.getsCategory(), itemCategory.gettCategory()) == null) {
                itemCategoryDao.save(itemCategory);
            }
        }
    }

    private void LoadUsersPermissions() {
        if (roleDao.findByRole("ADMIN") == null) {
            Role role = new Role();
            role.setRole("ADMIN");
            roleDao.save(role);
        }

        if (roleDao.findByRole("GUEST") == null) {
            Role role = new Role();
            role.setRole("GUEST");
            roleDao.save(role);
        }

        if (roleDao.findByRole("RENTER") == null) {
            Role role = new Role();
            role.setRole("RENTER");
            roleDao.save(role);
        }

        if (roleDao.findByRole("TENANT") == null) {
            Role role = new Role();
            role.setRole("TENANT");
            roleDao.save(role);

        }

        if (roleDao.findByRole("BOTH") == null) {
            Role role = new Role();
            role.setRole("BOTH");
            roleDao.save(role);

        }

        if (roleDao.findByRole("USER") == null) {
            Role role = new Role();
            role.setRole("USER");
            roleDao.save(role);

        }
    }
}
