package com.marketplace.CategoryManagement.domain;

import com.marketplace.CategoryManagement.api.CategoryDto;
import com.marketplace.CategoryManagement.api.CategoryRequestDto;
import com.marketplace.Util.HibernateUtil;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

    @Transactional
    public Category saveCategoryTest(Category category) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(category);
            tx.commit();
        }catch (Exception ex) {
            log.error("transaction error");
            if (tx != null) {
                tx.rollback();
            }
            ex.printStackTrace();
        }
        return category;
    }

    public List<CategoryDto> getCategories() {
        List<CategoryDto> categoriesDto;

        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            TypedQuery<CategoryDto> categoryQuery = session.createQuery("SELECT new com.marketplace.CategoryManagement.api.CategoryDto(" +
                    "c.id, c.name) From Category_Category_Management c", CategoryDto.class);
            categoriesDto = categoryQuery.getResultList();
//            if (categoriesDto.isEmpty()) {
//                throw new IllegalArgumentException("There is no Category in here");
//            }
            return categoriesDto;
        }
    }

    public void createCategory(CategoryRequestDto categoryDto) {
        boolean checkIsThatTheSameCategory = getCategories().stream()
                .anyMatch(categoryDto1 -> categoryDto.categoryName().equals(categoryDto1.name()));
        if (checkIsThatTheSameCategory) {
            throw new IllegalArgumentException("You have already send the same category in to db");
        }
        Transaction tx = null;
        Category category = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            category = new Category(categoryDto.categoryName());
            session.persist("Category_Management", category);
            tx.commit();
        } catch (Exception ex) {
            if (tx != null) {
                tx.rollback();
            }
            log.error("Transaction Error");
            ex.printStackTrace();
        }
        System.out.println("category = " + category);
    }

    public void createCategories(List<CategoryRequestDto> categoryRequestDtos) {
        System.out.println("create categories executed");
        Map<String, Long> frequencyMap = categoryRequestDtos.stream()
                .collect(Collectors.groupingBy(
                        dto -> dto.categoryName().toLowerCase(),
                        Collectors.counting()
                ));

        // Step 2: Check for duplicates within input DTOs
        String duplicatedElements = categoryRequestDtos.stream()
                .map(CategoryRequestDto::categoryName)
                .filter(name -> frequencyMap.get(name.toLowerCase()) > 1)
                .collect(Collectors.joining(", "));
        System.out.println("duplicated elements found nothing or not");

        if (!duplicatedElements.isBlank()) {
            throw new IllegalArgumentException("There are duplicated categories in the input: " + duplicatedElements);
        }

        System.out.println("duplicated element is not blank");

        // Step 3: Get lowercase category names from input DTOs
        List<String> categoryNameRequestDtosList = categoryRequestDtos.stream()
                .map(categoryRequestDto -> categoryRequestDto.categoryName().toLowerCase())
                .toList();

        System.out.println("found the category name request dtos list");
        // Step 4: Find duplicates between input DTOs and database categories
//        boolean isDbDuplicates = getCategories().stream()
//                .anyMatch(name -> name.equals())
        if (getCategories().size() > 1) {
            System.out.println("Still one");
            System.out.println("getCategories().getFirst().name() = " + getCategories().getFirst().name());
        }
        for (int i = 0; i < getCategories().size(); i++) {
            System.out.println("getCategories().get(i).name() = " + getCategories().get(i).name());
        }
//        System.out.println("categoryNameRequestDtosList = " + categoryNameRequestDtosList);
        String dbDuplicates = getCategories().stream()
                .map(CategoryDto::name)
                .filter(name -> categoryNameRequestDtosList.contains(name.toLowerCase()))
                .collect(Collectors.joining(", "));

        // Step 5: Throw exception if duplicates are found in the database
        System.out.println("dbDuplicates = " + dbDuplicates);
        System.out.println("found duplicates nothing");
        if (!dbDuplicates.isBlank()) {
            throw new IllegalArgumentException("Categories already exist in the database: " + dbDuplicates);
        }

//        createCategories(categoryRequestDtos);
        Transaction tx = null;
        List<Category> categories = List.of();
            System.out.println("transaction executed");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            categories = categoryRequestDtos.stream()
                    .map(s -> new Category(s.categoryName().toLowerCase()))
                    .toList();
            System.out.println("categories = " + categories);
            System.out.println("categories.getFirst().getName() = " + categories.getFirst().getName());
            System.out.println("start to do transaction");
            if (categories.size() < 2) {
                System.out.println("category is less than two");
                Category category = new Category(categories.getFirst().getName());
                session.persist("Category_Category_Management", category);
                tx.commit();
                return;
            }

            for (int i = 0; i < categories.size(); i++) {
                System.out.println("category is two or more");
                Category category = new Category(categories.get(i).getName());
                session.persist("Category_Category_Management", category);

            }
            System.out.println("after persisting category");
            tx.commit();
            System.out.println("categories = " + categories);
        } catch (Exception ex) {
            log.error("transaction error");
            if (tx != null) {
                tx.rollback();
            }
            ex.printStackTrace();
        }
        System.out.println("categories = " + categories);
    }

}
