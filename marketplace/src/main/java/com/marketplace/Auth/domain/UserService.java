package com.marketplace.Auth.domain;

import com.marketplace.Auth.api.UserAccountCreationDTO;
import com.marketplace.Auth.api.UserLoginDto;
import com.marketplace.Auth.api.UserMapper;
import com.marketplace.Exception.ResourceNotFoundException;
import com.marketplace.Util.HibernateUtil;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service("CommonUserService")
public class UserService {

    @PersistenceUnit(name = "my_persistence_unit")
    private EntityManagerFactory entityManagerFactory;

    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    public Boolean checkUserByEmail(String email) {
        boolean isThereAnyUserWithTheEmail;
        System.out.println("check user by email method start");
        entityManagerFactory = Persistence.createEntityManagerFactory("my_persistence_unit");
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()){
            TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT(u) FROM User_Auth u WHERE u.email = :user_email", Long.class);
            query.setParameter("user_email", email);
            Long count = query.getSingleResult();
            isThereAnyUserWithTheEmail = count > 0;
        }
        return isThereAnyUserWithTheEmail;
    }

    @Transactional
    public User createUserAccount(Role role, UserMapper mapper, UserAccountCreationDTO accountDTO) {
        entityManagerFactory = Persistence.createEntityManagerFactory("my_persistence_unit");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();
        User user = mapper.toUserAccount(accountDTO);
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        System.out.println("roles = " + roles);
        Set<User> users = new HashSet<>();
        users.add(user);
        System.out.println("users = " + users);
        role.setUsers(users);
        entityManager.persist(user);
        entityManager.close();
        return user;
//        Transaction tx = null;
//        User user = null;
//        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
//            user = mapper.toUserAccount(accountDTO);
//            user.addRole(role);
//
//            Set<User> users = new HashSet<>();
//            users.add(user);
//            role.setUsers(users);
//            System.out.println("user = " + user);
//            tx = session.beginTransaction();
//            session.save(user);
//            tx.commit();
//            return user;
//        } catch (Exception e) {
//            if (tx != null) {
//                tx.rollback();
//            }
//            e.printStackTrace();
//        }
//        return user;
    }


    public User getUserByEmail(String email) {
        User user;
        System.out.println("get user by email executed");
        entityManagerFactory = Persistence.createEntityManagerFactory("my_persistence_unit");
        entityManagerFactory = Persistence.createEntityManagerFactory("my_persistence_unit");
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            System.out.println("get user by email session executed");
            TypedQuery<User> query = entityManager.createQuery("From User_Auth U LEFT JOIN FETCH U.accountRoles WHERE U.email =:user_email", User.class);
            query.setParameter("user_email", email);
            user = query.getSingleResult();
//            System.out.println("users.getFirst().getEmail() = " + users.getFirst().getEmail());
            if (user == null) {
                throw new UsernameNotFoundException("User with this email " + email + "not found");
            }
//            user = users.getFirst();
            return user;
        }
    }

//    private List<Role> getUserRoles(String email) {
//        User user;
//        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
//            TypedQuery<User> query = session.createQuery("From User U WHERE U.email =:user_email", User.class);
//            query.setParameter("user_email", email);
//            user = query.getSingleResult();
//            System.out.println("user.getAccountRoles() = " + user.getAccountRoles());
//        }
//        System.out.println("user account roles is " + user.getAccountRoles().stream().toList());
//        return user.getAccountRoles().stream().toList();
//    }
//
//    public String authenticateUser(UserLoginDto userLoginDto) {
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        userLoginDto.email(),
//                        userLoginDto.password()
//                )
//        );
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//        System.out.println("user details get authorities is " + userDetails.getAuthorities());
//        return jwtUtil.generateToken(userDetails.getUsername(), getUserRoles(userDetails.getUsername()));
//    }

}
