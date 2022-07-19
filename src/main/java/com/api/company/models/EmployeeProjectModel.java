package com.api.company.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
@Getter
@Setter
@Entity(name = "employee_project")
@Table(uniqueConstraints= {@UniqueConstraint(
        columnNames = {"fk_employee_project_employee_idx",
                        "fk_employee_project_project_idx"})})
public class EmployeeProjectModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, length = 60)
    private String role;
    @ManyToOne
    @JoinColumn(name="fk_employee_project_employee_idx", nullable = false)
    private EmployeeModel employee;
    @ManyToOne
    @JoinColumn(name="fk_employee_project_project_idx", nullable = false)
    private ProjectModel project;
}
