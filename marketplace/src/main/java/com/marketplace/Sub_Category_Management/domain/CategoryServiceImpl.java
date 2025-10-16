package com.marketplace.Sub_Category_Management.domain;

import com.marketplace.Util.HibernateUtil;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service("Category_CategoryServiceImpl_SubCategoryManagement")
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    @Override
    public Category getCategoryByName(String categoryName) {
        List<Category> categories = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()){
            System.out.println("get category begin");
//            category = session.get(Category.class, categoryName);
            System.out.println("categoryName = " + categoryName);
            TypedQuery<Category> categoryQuery = session.createQuery("FROM Category_Sub_Category_Management c WHERE c.name = :category_name", Category.class);
            categoryQuery.setParameter("category_name", categoryName);
            System.out.println("categoryQuery = " + categoryQuery);
            categories = categoryQuery.getResultList();
            return categories.getFirst();
        }
    }

    public Category saveCategoryTest(String categoryName) {
        Transaction tx = null;
        Category category = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            tx = session.beginTransaction();
            Set<SubCategory> subCategories = new HashSet<>();
            category = Category.builder()
                    .name(categoryName)
                    .createdAt(LocalDateTime.now())
                    .build();
            SubCategory subCategory = new SubCategory("test_subCategory", category);
            subCategories.add(subCategory);
            category.setSubCategories(subCategories);
            session.persist("Category_Sub_Category_Management", category);
            tx.commit();
            System.out.println("category = " + category);
            return category;
        } catch (Exception ex) {
            if (tx != null) {
                tx.rollback();
            }
            log.error("Transaction Error");
            ex.printStackTrace();
        } finally {
            session.close();
        }
        return category;
    }
}
