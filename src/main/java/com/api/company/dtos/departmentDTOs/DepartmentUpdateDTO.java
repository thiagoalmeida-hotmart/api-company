package com.api.company.dtos.departmentDTOs;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class DepartmentUpdateDTO {

    @NotNull
    private Long id;
    @NotBlank
    @Size(max = 60)
    private String name;
    @NotNull
    private Long number;
}
