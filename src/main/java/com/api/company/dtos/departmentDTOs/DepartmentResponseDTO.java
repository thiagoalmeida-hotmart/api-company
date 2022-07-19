package com.api.company.dtos.departmentDTOs;

import com.api.company.dtos.projectDTOs.ProjectSlimDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DepartmentResponseDTO {

    private Long id;
    private String name;
    private Long number;
    @Valid
    private List<ProjectSlimDTO> projects;
}
