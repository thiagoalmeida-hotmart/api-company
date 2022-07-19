package com.api.company.dtos.employeeProjectDTOs;

import com.api.company.dtos.employeeDTOs.EmployeeSlimDTO;
import com.api.company.dtos.projectDTOs.ProjectSlimDTO;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class EmployeeProjectInsertDTO {
    @NotBlank
    @Size(max = 60)
    private String role;
    @NotNull
    @Valid
    private EmployeeSlimDTO employee;
    @NotNull
    @Valid
    private ProjectSlimDTO project;
}
