package com.marketplace.Auth.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Entity(name = "User_Auth")
@Table(name = "`Users`")
@Getter
@Setter
@NoArgsConstructor
@Slf4j
public class User implements UserDetails {

    @SuppressWarnings("deprecation")
    @Id
    @GeneratedValue(generator = "custom-generator")
    @GenericGenerator(name = "custom-generator",
            strategy = "com.marketplace.Util.CustomGenerator"
    )
    private String id;

    @Column(name = "email", length = 150, nullable = false)
    private String email;

    @Column(name = "name", length = 80, nullable = false)
    private String name;

    @Column(name = "password", length = 200, nullable = false)
    private String password;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_times", nullable = false)
    @CreationTimestamp
    private LocalDateTime updatedTimes = LocalDateTime.now();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "`Account_Roles`",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name="role_id")
    )
    private Set<Role> accountRoles;

    public User(String email, String name, String password, Set<Role> sellerRoles) {
        this.setEmail(email);
        this.setName(name);
        this.password = this.doEncryptPassword(password);
        this.setAccountRoles(sellerRoles);
    }

    public void addRole(Role role) {
        accountRoles.add(role);
    }

    public String doEncryptPassword(String sellerPassword) {
        return BCrypt.hashpw(sellerPassword, BCrypt.gensalt(10));
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.getAccountRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName().name()))
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return this.getEmail();
    }
}