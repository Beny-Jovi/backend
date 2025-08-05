package com.marketplace.Auth.domain;

import com.marketplace.Util.HibernateUtil;
import jakarta.persistence.TypedQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    public Role getOrCreateRoleAccount(Role.RoleEnum roleName) {
        Transaction tx = null;
        Role role = null;
        System.out.println("get or create role account run");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            System.out.println("Come in");
            TypedQuery<Role> query = session.createQuery("FROM Role r WHERE r.roleName =:role_name", Role.class);
            query.setParameter("role_name", roleName);
            List<Role> roles = query.getResultList();
            if (roles.isEmpty()) {
                tx = session.beginTransaction();
                Role newRole = new Role(roleName);
                System.out.println("newRole = " + newRole);
                System.out.println("newRole name = " + newRole.getRoleName());
                session.persist(newRole);
                role = newRole;
                tx.commit();
                System.out.println("new role is = " + role);
//                return role;
            }
            role = roles.getFirst();
            System.out.println("old role is = " + role);

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        return role;
    }

}
