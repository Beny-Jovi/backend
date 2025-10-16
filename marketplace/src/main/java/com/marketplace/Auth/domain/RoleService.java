package com.marketplace.Auth.domain;

import com.marketplace.Util.HibernateUtil;
import jakarta.persistence.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;


    public Role getOrCreateRoleAccount(Role.RoleEnum roleName) {
        Role role = null;
        System.out.println("get or create role account run");
        entityManagerFactory = Persistence.createEntityManagerFactory("my_persistence_unit");
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            System.out.println("Come in");
            TypedQuery<Role> query = entityManager.createQuery("SELECT r FROM Role_Auth r WHERE r.roleName =:role_name", Role.class);
            query.setParameter("role_name", roleName);
            List<Role> roles = query.getResultList();
            System.out.println("roles = " + role);
            if (roles.isEmpty()) {
                System.out.println("create role");
                EntityTransaction transaction = entityManager.getTransaction();
                transaction.begin();
                Role newRole = new Role(roleName);
                System.out.println("newRole = " + newRole);
                System.out.println("newRole name = " + newRole.getRoleName());
//                session.persist(newRole);
                entityManager.persist(newRole);
                role = newRole;
                System.out.println("new role is = " + role);
                return role;
            }
            System.out.println("old role is = " + role);
//            entityManager.close();
            return roles.get(0);

        }
//        return role;
    }

    public Role saveRoleTest(Role.RoleEnum roleName) {
        entityManagerFactory = Persistence.createEntityManagerFactory("my_persistence_unit");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Role role = new Role(roleName);
        entityManager.persist(role);
        entityManager.close();
        return role;
    }

}
