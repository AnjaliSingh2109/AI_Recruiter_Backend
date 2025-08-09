// --- 3. Entity: Company.java ---
package com.example.ai_recruiter.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "companies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String companyName;

    @Column(unique = true)
    private String companyId;

    @Column(unique = true)
    private String email;

    private String number;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;
}