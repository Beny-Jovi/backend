package com.marketplace.DiscProductManagement.Domain;

import com.marketplace.Util.HibernateUtil;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    public Category getOrCreateCategoryByName() {

        Transaction tx = null;
        Category category = null;

        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            TypedQuery<Category> query = session.createQuery("FROM Category C WHERE C.name = :category_name", Category.class);
            query.setParameter("category_name", Category.CategoryEnum.DIGITAL_WORK);
            List<Category> categories = query.getResultList();
            if (categories.isEmpty()) {
                System.out.println("Categories is empty");
                tx = session.beginTransaction();
                category = new Category(Category.CategoryEnum.DIGITAL_WORK);
                session.persist(category);
                tx.commit();

                return category;
            }
            category = categories.getFirst();
        } catch (Exception ex) {
            if(tx != null) {
                tx.rollback();
            }
            log.error("Something went wrong:{} ", ex.getMessage());
            ex.printStackTrace();
        }
//        System.out.println("category after operation = " + category);
        return category;
    }


}
