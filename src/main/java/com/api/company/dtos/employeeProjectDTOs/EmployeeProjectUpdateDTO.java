package com.api.company.dtos.employeeProjectDTOs;

import com.api.company.dtos.employeeDTOs.EmployeeSlimDTO;
import com.api.company.dtos.projectDTOs.ProjectSlimDTO;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class EmployeeProjectUpdateDTO {
    @NotNull
    private Long id;
    @NotBlank
    @Size(max = 60)
    private String role;
    @NotNull
    private EmployeeSlimDTO employee;
    @NotNull
    private ProjectSlimDTO project;
}
