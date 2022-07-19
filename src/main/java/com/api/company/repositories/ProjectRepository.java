package com.api.company.repositories;

import com.api.company.models.ProjectModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectModel,Long> {

    @Query("SELECT project FROM employee employee INNER JOIN employee_project employee_project " +
            "ON employee.id = employee_project.employee.id INNER JOIN project project " +
            "ON project.id = employee_project.project.id " +
            "WHERE employee.id = :idEmployee")
    List<ProjectModel> findAllProjectsByEmployeeId(@Param("idEmployee") Long idEmployee);

    @Query("SELECT COALESCE(SUM(project.cost),0) FROM project project WHERE project.startDate <= :searchDate " +
            "AND project.endDate >= :searchDate " +
            "AND project.department.id = :idDepartment")
    Double sumAllProjectsCostAvailableByDate(@Param("searchDate") LocalDate searchDate, Long idDepartment);
}
