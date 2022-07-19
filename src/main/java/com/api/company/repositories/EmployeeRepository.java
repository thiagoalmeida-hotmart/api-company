package com.api.company.repositories;

import com.api.company.models.EmployeeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeModel, Long> {

    @Query("SELECT employee FROM employee employee INNER JOIN employee_project employee_project " +
            "ON employee.id = employee_project.employee.id INNER JOIN project project " +
            "ON project.id = employee_project.project.id " +
            "WHERE project.department.id = :idDepartment")
    List<EmployeeModel> findAllEmployeesByDepartmentId(@Param("idDepartment") Long idDepartment);

    @Query("SELECT employee FROM employee employee WHERE employee.supervisor.id = :idSupervisor")
    List<EmployeeModel> findAllSupervisedBySupervisorId(@Param("idSupervisor") Long idSupervisor);

    @Query("SELECT employee FROM employee employee INNER JOIN employee_project employee_project " +
            "ON employee.id = employee_project.employee.id INNER JOIN project project " +
            "ON project.id = employee_project.project.id " +
            "WHERE project.id = :idProject")
    List<EmployeeModel> findAllEmployeesByProjectId(@Param("idProject") Long idProject);

    List<EmployeeModel> findAllByNameContaining(String name);
}
