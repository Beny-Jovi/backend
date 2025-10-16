package com.marketplace.Sub_Category_Management.domain;

import com.marketplace.Sub_Category_Management.api.SubCategoryDto;
import com.marketplace.Util.HibernateUtil;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service("subCategory_SubCategoryManagement")
@Slf4j
public class SubCategoryServiceImpl implements SubCategoryService {

    @Override
    public List<SubCategoryDto> getSubCategories() {
        List<SubCategoryDto> subCategoryDtos;

        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            System.out.println("begin transaction");
            TypedQuery<SubCategoryDto> categoryQuery = session.createQuery("SELECT new com.marketplace.Sub_Category_Management.api.SubCategoryDto(" +
                    "s.id, s.subCategoryName) From Sub_Category_SubCategoryManagement s", SubCategoryDto.class);
            subCategoryDtos = categoryQuery.getResultList();
//            if (subCategoryDtos.isEmpty()) {
//                throw new IllegalArgumentException("There is no Category in here");
//            }
            return subCategoryDtos;
        }
    }

    @Override
    public void createSubCategory(String subCategoryName, Category category) {
        System.out.println("create sub category");
        boolean checkIsThatTheSameSubCategoryName = getSubCategories().stream()
                .anyMatch(subCategoryDto -> subCategoryDto.name().equals(subCategoryName));
        System.out.println("check is the same sub category name");
        if (checkIsThatTheSameSubCategoryName) {
            throw new IllegalArgumentException("You have already create subCategory");
        }
        Transaction tx = null;
        SubCategory subCategory;
        try (Session session = HibernateUtil.getSessionFactory().openSession()){
            tx = session.beginTransaction();
            subCategory = new SubCategory(subCategoryName, category);
            session.persist(subCategory);
            tx.commit();
            System.out.println("subCategory = " + subCategory);
        } catch (Exception ex) {
            if (tx != null) {
                tx.rollback();
            }
            log.error("Transaction Error");
            ex.printStackTrace();
        }
    }

    @Override
    public void createSubCategories(List<String> subCategoryNames, Category category) {
        Map<String, Long> frequencyMap = subCategoryNames.stream()
                .collect(Collectors.groupingBy(
                        String::toLowerCase,
                        Collectors.counting()
                ));

        // Step 2: Check for duplicates within input DTOs
        String duplicateElements = subCategoryNames.stream()
                .map(String::toLowerCase)
                .filter(name -> frequencyMap.get(name.toLowerCase()) > 1)
                .collect(Collectors.joining(", "));

        if (!duplicateElements.isBlank()) {
            throw new IllegalArgumentException("There are duplicated categories in the input: " + duplicateElements);
        }

        System.out.println("duplicated element is not blank");

        List<String> subCategoryNameRequestDtosList = subCategoryNames.stream()
                .map(String::toLowerCase)
                .toList();

        System.out.println("found the category name request dtos list");

        String dbDuplicates = getSubCategories().stream()
                .map(SubCategoryDto::name)
                .filter(name -> subCategoryNameRequestDtosList.contains(name.toLowerCase()))
                .collect(Collectors.joining());

        System.out.println("dbDuplicates = " + dbDuplicates);
        System.out.println("found duplicates nothing");
        if (!dbDuplicates.isBlank()) {
            throw new IllegalArgumentException("Categories already exist in the database: " + dbDuplicates);
        }

        Transaction tx = null;
        List<SubCategory> subCategories = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            subCategories = subCategoryNames.stream()
                    .map(s -> new SubCategory(s, category))
                    .toList();

            if (subCategories.size() < 2) {
                SubCategory subCategory = new SubCategory(subCategories.getFirst().getSubCategoryName(), category);
                Set<SubCategory> subCategories1 = new HashSet<>();
                subCategories1.add(subCategory);
                category.setSubCategories(subCategories1);
                session.persist(subCategory);
                tx.commit();
                return;
            }

            for (int i = 0; i < subCategories.size(); i++) {
                SubCategory subCategory = new SubCategory(subCategories.get(i).getSubCategoryName(), category);
                Set<SubCategory> subCategories1 = new HashSet<>();
                subCategories1.add(subCategory);
                category.setSubCategories(subCategories1);
                session.persist(subCategory);
            }
            tx.commit();
            System.out.println("subCategories = " + subCategories);

        } catch (Exception ex) {
            log.error("transaction error");
            if (tx != null) {
                tx.rollback();
            }
            ex.printStackTrace();
        }

    }
}
