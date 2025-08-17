package com.example.WorkforceManagement.repository;

import com.example.WorkforceManagement.entity.WorkOrderAssignment;
import com.example.WorkforceManagement.entity.Employee;
import com.example.WorkforceManagement.entity.Interval;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface WorkOrderAssignmentRepository extends JpaRepository<WorkOrderAssignment, Long> {
    boolean existsByEmployeeAndWorkDateAndInterval(Employee employee, LocalDate workDate, Interval interval);
    List<WorkOrderAssignment> findByEmployeeEmployeeIdAndWorkDateBetween(Long employeeId, LocalDate start, LocalDate end);
}