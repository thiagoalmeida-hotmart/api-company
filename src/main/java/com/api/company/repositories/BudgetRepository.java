package com.api.company.repositories;

import com.api.company.models.BudgetModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BudgetRepository extends JpaRepository<BudgetModel, Long> {

    List<BudgetModel> findAllByDepartmentId(Long idDepartment);

    @Query("SELECT COALESCE(SUM(budget.value),0) FROM budget budget WHERE budget.startDate <= :searchDate " +
            "AND budget.endDate >= :searchDate " +
            "AND budget.department.id = :idDepartment")
    Double sumAllBudgetsAvailableByDate(@Param("searchDate") LocalDate searchDate, Long idDepartment);
}
