package com.api.company.repositories;

import com.api.company.models.EmployeeProjectModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeProjectRepository extends JpaRepository<EmployeeProjectModel,Long> {

    @Query("SELECT employee_project FROM employee employee INNER JOIN employee_project employee_project " +
            "ON employee.id = employee_project.employee.id WHERE employee.id = :idEmployee")
    List<EmployeeProjectModel> findAllEmployeeProjectsByEmployeeId(@Param("idEmployee") Long idEmployee);
}
