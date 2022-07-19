package com.api.company.dtos.departmentDTOs;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DepartmentBudgetStatusDTO {

    @NotNull
    @JsonFormat(pattern="dd/MM/yyyy")
    private LocalDate searchDate;
}
