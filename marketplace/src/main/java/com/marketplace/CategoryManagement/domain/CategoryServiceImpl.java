package com.marketplace.CategoryManagement.domain;

import com.marketplace.CategoryManagement.api.CategoryDto;
import com.marketplace.CategoryManagement.api.CategoryRequestDto;
import com.marketplace.Util.HibernateUtil;
import jakarta.persistence.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;




@Slf4j
@Service("CategoryService_CategoryManagement")
public class CategoryServiceImpl implements CategoryService{

    private EntityManagerFactory entityManagerFactory;



    public Category createCategory(CategoryRequestDto categoryDto) {
        entityManagerFactory = Persistence.createEntityManagerFactory("my_persistence_unit");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        boolean checkIsThatTheSameCategory = getCategories().stream()
                .anyMatch(categoryDto1 -> categoryDto.categoryName().equals(categoryDto1.categoryName()));
        if (checkIsThatTheSameCategory) {
            throw new IllegalArgumentException("You have already send the same category in to db");
        }

//        List<Category> categories = categoryRequestDtos.stream().map(s -> {
//            if (s.categoryName().isBlank()) {
//                throw new IllegalArgumentException("The category can't be empty");
//            }
//            return new Category(s.categoryName());
        Category category = new Category(categoryDto.categoryName());
        transaction.begin();

        entityManager.persist(category);
        transaction.commit();
        entityManager.close();
        return category;
        ////        categoryValidator(categoryRequestDtos);
        ////
        ////        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
        ////            System.out.println("try executed");
        ////
        ////            List<Category> categories = categoryRequestDtos.stream().map(s -> {
        ////                if (s.categoryName().isBlank()) {
        ////                    throw new IllegalArgumentException("The category can't be empty");
        ////                }
        ////                return new Category(s.categoryName());
        ////
        ////            }).toList();
        ////            System.out.println("categories = " + categories);
        ////            tx = session.beginTransaction();
        ////            session.save(categories);
    }

    @Override
    public List<CategoryDto> getCategories() {
        return List.of();
    }
}
//public class CategoryServiceImpl implements CategoryService {
//
//    @Override
//    public List<CategoryDto> getCategories() {
//        List<CategoryDto> categoriesDto;
//
//        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
//            TypedQuery<CategoryDto> categoryQuery = session.createQuery("SELECT new com.marketplace.CategoryManagement.api.CategoryDto(" +
//                    "c.id, c.name) From Category_Management c", CategoryDto.class);
//            categoriesDto = categoryQuery.getResultList();
////            if (categoriesDto.isEmpty()) {
////                throw new IllegalArgumentException("This category hasn't created yet");
////            }
//            return categoriesDto;
//        }
//    }
//
//    @Transactional
//    public Category saveCategoryTest(Category category) {
//        Transaction tx = null;
//        Category category1 = null;
//        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
//            tx = session.beginTransaction();
//            session.persist(category);
//            tx.commit();
//        }catch (Exception ex) {
//            log.error("transaction error");
//            if (tx != null) {
//                tx.rollback();
//            }
//            ex.printStackTrace();
//        }
//        return category;
//    }
//
//    public List<Category> categoryValidatorAndCreateCategories(List<CategoryRequestDto> categoryRequestDtos) {
//
//        Map<String, Long> frequencyMap = categoryRequestDtos.stream()
//                .collect(Collectors.groupingBy(
//                        dto -> dto.categoryName().toLowerCase(),
//                        Collectors.counting()
//                ));
//
//        // Step 2: Check for duplicates within input DTOs
//        String duplicatedElements = categoryRequestDtos.stream()
//                .map(CategoryRequestDto::categoryName)
//                .filter(name -> frequencyMap.get(name.toLowerCase()) > 1)
//                .collect(Collectors.joining(", "));
//
//        if (!duplicatedElements.isBlank()) {
//            throw new IllegalArgumentException("There are duplicated categories in the input: " + duplicatedElements);
//        }
//
//        // Step 3: Get lowercase category names from input DTOs
//        List<String> categoryNameRequestDtosList = categoryRequestDtos.stream()
//                .map(categoryRequestDto -> categoryRequestDto.categoryName().toLowerCase())
//                .toList();
//
//        // Step 4: Find duplicates between input DTOs and database categories
//        String dbDuplicates = getCategories().stream()
//                .map(CategoryDto::categoryName)
//                .filter(name -> categoryNameRequestDtosList.contains(name.toLowerCase()))
//                .collect(Collectors.joining(", "));
//
//        // Step 5: Throw exception if duplicates are found in the database
//        if (!dbDuplicates.isBlank()) {
//            throw new IllegalArgumentException("Categories already exist in the database: " + dbDuplicates);
//        }
//
////        createCategories(categoryRequestDtos);
//        Transaction tx = null;
//        List<Category> categories = List.of();
//        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
//            tx = session.beginTransaction();
//            categories = categoryRequestDtos.stream()
//                    .map(s -> new Category(s.categoryName().toLowerCase()))
//                    .toList();
//            System.out.println("categories = " + categories);
//            session.persist("Category_Management", categories);
//
//            tx.commit();
//            return categories;
//        } catch (Exception ex) {
//            log.error("transaction error");
//            if (tx != null) {
//                tx.rollback();
//            }
//            ex.printStackTrace();
//        }
//        return categories;
//    }
//
////    public List<Category> findAllCategory() {
////        List<Category> categories;
////
////        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
////            TypedQuery<Category> query = session.createQuery("FROM Category_Management", Category.class);
////            categories = query.getResultList();
////            if (categories.isEmpty()) {
////                throw new ResourceNotFoundException("There is no categories");
////            }
////            System.out.println("categories = " + categories);
////        }
////        return categories;
////    }
//
////    @Override
////    public void createCategories(List<CategoryRequestDto> categoryRequestDtos) {
////        System.out.println("create categories executed");
////        Transaction tx = null;
////
////        if (categoryRequestDtos.size() < 2) {
////            throw new IllegalArgumentException("You should go into another categories");
//////                tx.commit();
////        }
////        categoryValidator(categoryRequestDtos);
////
////        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
////            System.out.println("try executed");
////
////            List<Category> categories = categoryRequestDtos.stream().map(s -> {
////                if (s.categoryName().isBlank()) {
////                    throw new IllegalArgumentException("The category can't be empty");
////                }
////                return new Category(s.categoryName());
////
////            }).toList();
////            System.out.println("categories = " + categories);
////            tx = session.beginTransaction();
////            session.save(categories);
////
////            tx.commit();
////        } catch (Exception ex) {
////            if (tx != null) {
////                tx.rollback();
////            }
////            log.error("Transaction Error");
////            ex.printStackTrace();
////        }
////    }
//
//    public Category createCategory(CategoryRequestDto categoryDto) {
//        boolean checkIsThatTheSameCategory = getCategories().stream()
//                .anyMatch(categoryDto1 -> categoryDto.categoryName().equals(categoryDto1.categoryName()));
//        if (checkIsThatTheSameCategory) {
//            throw new IllegalArgumentException("You have already send the same category in to db");
//        }
//        Transaction tx = null;
//        Category category = null;
//        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
//            tx = session.beginTransaction();
////            Category category = Category.builder()
////                    .name(categoryDto.categoryName())
//////                    .createdAt(LocalDateTime.now())
////                    .build();
//            category = new Category(categoryDto.categoryName());
//            session.persist("Category_Management", category);
//            tx.commit();
//        } catch (Exception ex) {
//            if (tx != null) {
//                tx.rollback();
//            }
//            log.error("Transaction Error");
//            ex.printStackTrace();
//        }
//        return category;
//    }
//
//}
