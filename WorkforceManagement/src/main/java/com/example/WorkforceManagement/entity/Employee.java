package com.example.WorkforceManagement.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "employees_am")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(unique = true, length = 320)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(length = 20)
    private String phoneNumber;

    @Column(nullable = false, length = 20)
    private String role; // 'Technician' or 'TeamLeader'
}