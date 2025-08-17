package com.example.WorkforceManagement.repository;

import com.example.WorkforceManagement.entity.Interval;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IntervalRepository extends JpaRepository<Interval, Integer> {}