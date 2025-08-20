package com.example.WorkforceManagement.repository;

import com.example.WorkforceManagement.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmail(String email);
    @Query("""
        SELECT e 
        FROM Employee e 
        LEFT JOIN e.assignments a 
        ON a.workDate = :workDate
        GROUP BY e
        HAVING COUNT(a) < 6
    """)
    List<Employee> findEmployeesWithLessThanSixAssignments(@Param("workDate") LocalDate workDate);
    long countByRole(String role);
}