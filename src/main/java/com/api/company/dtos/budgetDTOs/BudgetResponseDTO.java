package com.api.company.dtos.budgetDTOs;

import com.api.company.dtos.departmentDTOs.DepartmentSlimDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BudgetResponseDTO {
    private Long id;
    private String description;
    private Double value;
    @JsonFormat(pattern="dd/MM/yyyy")
    private LocalDate startDate;
    @JsonFormat(pattern="dd/MM/yyyy")
    private LocalDate endDate;
    private DepartmentSlimDTO department;
}
