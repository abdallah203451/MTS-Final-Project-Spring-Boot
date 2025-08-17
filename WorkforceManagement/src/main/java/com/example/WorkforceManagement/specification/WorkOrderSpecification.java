package com.example.WorkforceManagement.specification;

import com.example.WorkforceManagement.entity.WorkOrder;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import com.example.WorkforceManagement.dto.workorder.WorkOrderSearchCriteria;

public class WorkOrderSpecification {
    public static Specification<WorkOrder> byCriteria(WorkOrderSearchCriteria c) {
        return (root, query, cb) -> {
            Predicate p = cb.conjunction();
            if (c == null) return p;

            // q: search in title, customerName, description (case-insensitive)
            if (c.getQ() != null && !c.getQ().trim().isEmpty()) {
                String like = "%" + c.getQ().trim().toLowerCase() + "%";
                Expression<String> titleEx = cb.lower(cb.coalesce(root.get("title"), ""));
                Expression<String> customerEx = cb.lower(cb.coalesce(root.get("customerName"), ""));
                Expression<String> descEx = cb.lower(cb.coalesce(root.get("description"), ""));
                Predicate title = cb.like(titleEx, like);
                Predicate customer = cb.like(customerEx, like);
                Predicate description = cb.like(descEx, like);
                p = cb.and(p, cb.or(title, customer, description));
            }

            // status filter (exact match)
            if (c.getStatus() != null && !c.getStatus().trim().isEmpty()) {
                p = cb.and(p, cb.equal(root.get("status"), c.getStatus().trim()));
            }

            // date range filters on proposedSchedulingDate (inclusive)
            if (c.getFromDate() != null) {
                p = cb.and(p, cb.greaterThanOrEqualTo(root.get("proposedSchedulingDate"), c.getFromDate()));
            }
            if (c.getToDate() != null) {
                p = cb.and(p, cb.lessThanOrEqualTo(root.get("proposedSchedulingDate"), c.getToDate()));
            }

            // NOTE: technicianId would require join to assignment entity - omitted here as you indicated.
            return p;
        };
    }
}