package com.api.company.dtos.projectDTOs;

import com.api.company.dtos.departmentDTOs.DepartmentSlimDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
public class ProjectInsertDTO {

    @NotBlank
    @Size(max = 60)
    private String name;
    @NotNull
    private Double value;
    @NotNull
    @JsonFormat(pattern="dd/MM/yyyy")
    private LocalDate startDate;
    @JsonFormat(pattern="dd/MM/yyyy")
    @NotNull
    private LocalDate endDate;
    @Valid
    private DepartmentSlimDTO department;
}
