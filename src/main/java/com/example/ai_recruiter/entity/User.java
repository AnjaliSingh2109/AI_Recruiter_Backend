// --- 2. Entity: User.java ---
package com.example.ai_recruiter.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    @Column(unique = true)
    private String email;

    private String number;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;
}
