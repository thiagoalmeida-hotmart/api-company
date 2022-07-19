package com.api.company.dtos.projectDTOs;

import com.api.company.dtos.departmentDTOs.DepartmentSlimDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import java.time.LocalDate;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectResponseDTO {

    private Long id;
    private String name;
    private Double value;
    private Double cost;
    @JsonFormat(pattern="dd/MM/yyyy")
    private LocalDate startDate;
    @JsonFormat(pattern="dd/MM/yyyy")
    private LocalDate endDate;
    @Valid
    private DepartmentSlimDTO department;
}
