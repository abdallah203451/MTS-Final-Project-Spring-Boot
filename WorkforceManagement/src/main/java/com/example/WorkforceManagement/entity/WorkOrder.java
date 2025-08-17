package com.example.WorkforceManagement.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "work_orders_am")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long workOrderId;

    @Column(nullable = false, length = 400)
    private String title;

    @Lob
    private String description;

    @Column(nullable = false, length = 200)
    private String customerName;

    @Column(nullable = false, length = 30)
    private String customerMobile;

    @Column(length = 320)
    private String customerEmail;

    @Lob
    private String customerAddress;

    @Column(nullable = false)
    private LocalDate proposedSchedulingDate;

    @Column(nullable = false)
    private OffsetDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private Employee createdBy;

    @Column(nullable = false, length = 30)
    private String status; // NotAssigned, Assigned, InProgress, Completed, Cancelled
}