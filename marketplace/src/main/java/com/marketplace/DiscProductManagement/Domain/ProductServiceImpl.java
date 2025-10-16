package com.marketplace.DiscProductManagement.Domain;

import com.marketplace.DiscProductManagement.Api.ProductDto;
import com.marketplace.Exception.ResourceNotFoundException;
import com.marketplace.Util.HibernateUtil;
import jakarta.persistence.TypedQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Override
    public Optional<Product> getProduct(String id) {
        return Optional.empty();
    }

    @Override
    public List<Product> getAllProducts() {
        return List.of();
    }

    public List<ProductDto> getAllProductsFromAStore(String id) {
        List<Product> products;
        List<ProductDto> productDtos;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            TypedQuery<ProductDto> query = session.createQuery("SELECT new com.marketplace.DiscProductManagement.Api.ProductDto(" +
                    "c.name, s.name, p.orderMinimum, d.title, d.productCondition, " +
                    "d.description, d.unitOfProduct, d.publisher, pv.productRate, pv.numberOfView) " +
                    "FROM Product p " +
                    "JOIN p.subCategory s " +
                    "JOIN s.category c " +
                    "JOIN p.Disc d " +
                    "JOIN p.productView pv " +
                    "WHERE p.store.id = :store_id AND p.isDeleted IS null", ProductDto.class);
            query.setParameter("store_id", id);
            productDtos = query.getResultList();
        }
        return productDtos;
    }

    @Transactional
    public Product createProduct(Product product) {
        Transaction tx = null;
        System.out.println("product.getDisc() = " + product.getDisc());
        System.out.println("product.getProductView() = " + product.getProductView());
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            tx = session.beginTransaction();
            session.persist("Product_Management", product);
            tx.commit();
        } catch (Exception ex) {
            if (tx != null) {
                tx.rollback();
            }
            ex.printStackTrace();
        } finally {
            session.close();
        }
        return product;
    }

    @Transactional
    public ProductDto getSpecificProduct(String storeId, String productId) {
        ProductDto productDto;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            TypedQuery<ProductDto> query = session.createQuery("SELECT new com.marketplace.DiscProductManagement.Api.ProductDto(" +
                    "c.name, s.name, p.orderMinimum, d.title, d.productCondition, " +
                    "d.description, d.unitOfProduct, d.publisher, pv.productRate, pv.numberOfView) " +
                    "FROM Product p " +
                    "JOIN p.subCategory s " +
                    "JOIN s.category c " +
                    "JOIN p.Disc d " +
                    "JOIN p.productView pv " +
                    "WHERE p.store.id = :store_id AND" + " p.id =: product_id AND p.isDeleted IS null", ProductDto.class);
            query.setParameter("store_id", storeId);
            query.setParameter("product_id", productId);
            List<ProductDto> productDtos = query.getResultList();
            if (productDtos.isEmpty()) {
                throw new ResourceNotFoundException("There is no product with this id in this store");
            }
//            if (productDtos.getFirst().)
            productDto = productDtos.getFirst();
            System.out.println("productDto = " + productDto);
        }
        return productDto;
    }

    @Transactional
    public void deleteSpecificProduct(String storeId, String productId) {
        Transaction tx = null;
        Product product;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            TypedQuery<Product> query = session.createQuery("FROM Product P WHERE P.store.id = :store_id AND P.id =: product_id", Product.class);
            query.setParameter("store_id", storeId);
            query.setParameter("product_id", productId);
            List<Product> products = query.getResultList();
            if (products.isEmpty()) {
                throw new ResourceNotFoundException("There is no product with this id in this store");
            }
            product = products.getFirst();
            if (product.getIsDeleted() == null) {
                tx = session.beginTransaction();
                product.setIsDeleted(true);
                System.out.println("product = " + product.getIsDeleted());
                session.merge("Product_Management", product);
                tx.commit();
            }
            System.out.println("product = " + product);
            System.out.println("product = " + product.getIsDeleted());

        } catch (Exception ex) {
            if (tx != null) {
                tx.rollback();
            }
            ex.printStackTrace();

        }
    }

}
