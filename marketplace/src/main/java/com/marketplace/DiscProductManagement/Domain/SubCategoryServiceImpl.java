package com.marketplace.DiscProductManagement.Domain;

import com.marketplace.Util.HibernateUtil;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class SubCategoryServiceImpl implements SubCategoryService{
    @Override
    @Transactional
    public SubCategory getSubCategory(Category category, String subCategoryName) {
        Transaction tx = null;
        SubCategory subCategory = null;

        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            TypedQuery<SubCategory> query = session.createQuery("FROM SubCategory SC WHERE SC.name = :sub_category_name", SubCategory.class);
            query.setParameter("sub_category_name", subCategoryName);
            List<SubCategory> subCategories = query.getResultList();
            if (subCategories.isEmpty()) {
                    throw new IllegalArgumentException("this category hasn't created yet");
            }
            subCategory = subCategories.getFirst();
            System.out.println("get sub category: " + subCategory);
        }
        System.out.println("get sub category after try: " + subCategory);

        return subCategory;
    }

    public SubCategory createSubCategoryTest(Category category, String subCategoryName) {
        Transaction tx = null;
        SubCategory subCategory = null;

        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            subCategory = new SubCategory(subCategoryName, category);
            session.persist(subCategory);
            tx.commit();
            return subCategory;
        } catch (Exception ex) {
            log.error("transaction error");
            if (tx != null) {
                tx.rollback();
            }
            ex.printStackTrace();
        }
        return subCategory;
    }
}
