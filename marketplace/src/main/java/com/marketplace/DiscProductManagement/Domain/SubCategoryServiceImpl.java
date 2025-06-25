package com.marketplace.DiscProductManagement.Domain;

import com.marketplace.Util.HibernateUtil;
import jakarta.persistence.TypedQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SubCategoryServiceImpl implements SubCategoryService{
    @Override
    @Transactional
    public SubCategory getOrCreateSubCategory(Category category) {
        Transaction tx = null;
        SubCategory subCategory = null;

        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            TypedQuery<SubCategory> query = session.createQuery("FROM SubCategory SC WHERE SC.name = :sub_category_name", SubCategory.class);
            query.setParameter("sub_category_name", SubCategory.SubCategoryEnum.DISC);
            List<SubCategory> subCategories = query.getResultList();
            if (subCategories.isEmpty()) {
                tx = session.beginTransaction();
                subCategory = SubCategory.builder()
                        .name(SubCategory.SubCategoryEnum.DISC)
                        .category(category)
                        .build();
                System.out.println("create new sub category: " + subCategory);
                session.persist(subCategory);
                tx.commit();

            }
            subCategory = subCategories.getFirst();
            System.out.println("get sub category: " + subCategory);
        } catch (Exception ex) {
            if(tx != null) {
                tx.rollback();
            }
//            log.error("Something went wrong: ", ex.getMessage());
            ex.printStackTrace();
        }
        System.out.println("get sub category after try: " + subCategory);

        return subCategory;
    }
}
