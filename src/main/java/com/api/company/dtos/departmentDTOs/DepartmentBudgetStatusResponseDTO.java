package com.api.company.dtos.departmentDTOs;

import com.api.company.enums.StatusBudgetEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DepartmentBudgetStatusResponseDTO {
    private StatusBudgetEnum status;
    private Double projectsCost;
    private Double budget;

    public DepartmentBudgetStatusResponseDTO(StatusBudgetEnum status, Double projectsCost, Double budget){
        this.status = status;
        this.projectsCost = projectsCost;
        this.budget = budget;
    }
}
