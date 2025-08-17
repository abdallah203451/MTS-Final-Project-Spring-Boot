package com.example.WorkforceManagement.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "intervals_am")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Interval {
    @Id
    private Integer intervalId;

    private String label;
    private String startTime;
    private String endTime;
}