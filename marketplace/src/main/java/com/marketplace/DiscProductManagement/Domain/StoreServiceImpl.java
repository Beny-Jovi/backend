package com.marketplace.DiscProductManagement.Domain;

import com.marketplace.DiscProductManagement.Api.ProductDto;
import com.marketplace.DiscProductManagement.Api.ProductMapper;
import com.marketplace.DiscProductManagement.Api.ProductReqDto;
import com.marketplace.Exception.ResourceNotFoundException;
import com.marketplace.Util.HibernateUtil;
import jakarta.persistence.TypedQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class StoreServiceImpl implements StoreService {
    @Override
    public Optional<Store> getStore(String id) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(Store.class, id));
        }
    }

    @Override
    public void deleteProduct(String storeId) {

    }

//    @Override
    public void getStoreProduct(String storeId, ProductMapper mapper) {
        List<Product> products;
        List<ProductDto> productDtos;
        try (Session session = HibernateUtil.getSessionFactory().openSession()){
//            Store foundStore = Optional.ofNullable(session.get(Store.class, storeId))
//                    .orElseThrow(() -> new ResourceNotFoundException("Store with this id not found"));
//            products = new ArrayList<>(foundStore.getProducts());
//            productDtos = products.stream()
//                    .map(mapper::toDto)
//                    .toList();
            TypedQuery<ProductDto> query = session.createQuery("SELECT s.products FROM Store s WHERE s.id =: store_id", ProductDto.class);
            query.setParameter("store_id", storeId);
            productDtos = query.getResultList();
            System.out.println("products = " + productDtos);
        }
//        System.out.println("products = " + products);
//        System.out.println("productDtos = " + productDtos);

    }

//    @Override
//    @Transactional(readOnly = true)
//    public List<Product> getStoreProduct(String storeId) {
//        Transaction tx= null;
//        Product product = null;
//        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
//            TypedQuery<Product> query = session.createQuery("FROM Product P WHERE P.storeId = : ")
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        return store.getProducts().stream().toList();
//    }

    @Transactional
    public Product createProduct(String id, ProductMapper productMapper, Category category, SubCategory subCategory,ProductReqDto productDto) {
        Store foundStore = getStore(id)
                .orElseThrow(() -> new ResourceNotFoundException("Store with this id not found"));
        System.out.println("foundStore = " + foundStore);
        Product product = null;
        Transaction tx = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            tx = session.beginTransaction();
            product = productMapper.toProduct(category, subCategory, foundStore, productDto);
//            product = new Product(productDto.productOrderMinimum(), foundStore);

            Set<Product> products = new HashSet<>();
            products.add(product);
            foundStore.setProducts(products);

//            foundStore.getProducts().add(product);
            session.persist(product);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
            throw new IllegalArgumentException("Something went wrong");
        } finally {
            session.close();
        }
        System.out.println("product = " + product);
        return product;
    }

    public Store saveStoreTest(String name) {
        Transaction tx = null;
        Store store = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            store = Store.builder()
                    .name(name)
                    .build();
            session.save(store);
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        return store;
    }

}
