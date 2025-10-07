    package com.ceos22.springcgv.domain;

    import com.ceos22.springcgv.domain.common.BaseEntity;
    import jakarta.persistence.*;
    import lombok.*;

    @Entity
    @Getter
    @Setter
    @NoArgsConstructor
    @Table(name = "users")
    public class User extends BaseEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "user_id")
        private Long id;

        @Column(unique = true, nullable = false, length = 50)
        private String username;

        @Column(nullable = false)
        private String password;

        @Column(nullable = false, length = 50)
        private String name;

        @Column(unique = true, nullable = false, length = 100)
        private String email;

        @Column(name = "phone_number", length = 20)
        private String phoneNumber;

        @Column(unique = true, nullable = false, length = 50)
        private String nickname;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false, length = 50)
        private Role role = Role.ROLE_USER;

        public enum Role {
            ROLE_USER, ROLE_ADMIN
        }

        @Builder
        private User(String username, String password, String name,
                     String email, String phoneNumber, String nickname, Role role) {
            this.username = username;
            this.password = password;
            this.name = name;
            this.email = email;
            this.phoneNumber = phoneNumber;
            this.nickname = nickname;
            if (role != null) this.role = role;
        }
    }
