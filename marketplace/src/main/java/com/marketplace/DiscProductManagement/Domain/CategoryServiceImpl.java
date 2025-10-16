package com.marketplace.DiscProductManagement.Domain;

import com.marketplace.Util.HibernateUtil;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("CategoryProductManagement")
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    public Category getCategoryByName(String name) {

        Transaction tx = null;
        Category category = null;

        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            TypedQuery<Category> query = session.createQuery("FROM Category C WHERE C.name = :category_name", Category.class);
            query.setParameter("category_name", name);
            List<Category> categories = query.getResultList();
            if (categories.isEmpty()) {
                throw new IllegalArgumentException("this category hasn't created yet");
            }
            category = categories.getFirst();
        }
//        System.out.println("category after operation = " + category);
        return category;
    }

    public Category saveCategoryTest(String categoryName) {
        Transaction tx = null;
        Category category = null;

        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            category = new Category(categoryName);
            session.persist(category);
            tx.commit();
        } catch (Exception ex) {
            log.error("transaction error");
            if (tx != null) {
                tx.rollback();
            }
            ex.printStackTrace();
        }
        return category;
    }


}
