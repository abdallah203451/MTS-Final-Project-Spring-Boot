package com.example.WorkforceManagement.repository;

import com.example.WorkforceManagement.entity.WorkOrder;
import com.example.WorkforceManagement.entity.WorkOrderAssignment;
import com.example.WorkforceManagement.entity.Employee;
import com.example.WorkforceManagement.entity.Interval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface WorkOrderAssignmentRepository extends JpaRepository<WorkOrderAssignment, Long> {
    boolean existsByEmployeeAndWorkDateAndInterval(Employee employee, LocalDate workDate, Interval interval);

    List<WorkOrderAssignment> findByEmployeeEmployeeIdAndWorkDateBetween(Long employeeId, LocalDate start, LocalDate end);

    List<WorkOrderAssignment> findByWorkOrder(WorkOrder workOrder);

    void deleteByWorkOrder(WorkOrder workOrder);

    @Query("SELECT DISTINCT e FROM Employee e WHERE e.role = 'Technician' AND " +
            "(SELECT COUNT(woa) FROM WorkOrderAssignment woa WHERE woa.employee = e AND woa.workDate = :date) < " +
            "(SELECT COUNT(i) FROM Interval i)")
//    @Query("SELECT e FROM Employee e " +
//            "LEFT JOIN WorkOrderAssignment woa ON woa.employee = e AND woa.workDate = :date " +
//            "WHERE e.role = 'Technician' " +
//            "GROUP BY e.employeeId " + // Grouping by ID is often safer
//            "HAVING COUNT(woa.assignmentId) < (SELECT COUNT(i) FROM Interval i)")
    List<Employee> findTechniciansWithAvailableIntervals(@Param("date") LocalDate date);

    // Count assigned intervals for an employee on a specific date
    @Query("SELECT COUNT(woa) FROM WorkOrderAssignment woa WHERE woa.employee.employeeId = :employeeId AND woa.workDate = :date")
    long countAssignedIntervals(@Param("employeeId") Long employeeId, @Param("date") LocalDate date);

    @Query("""
      SELECT a.workDate, COUNT(a)
      FROM WorkOrderAssignment a
      WHERE a.workDate BETWEEN :start AND :end
        AND a.employee.role = 'Technician'
      GROUP BY a.workDate
    """)
    List<Object[]> countTechnicianAssignmentsGroupedByDate(@Param("start") LocalDate start,
                                                           @Param("end") LocalDate end);
}