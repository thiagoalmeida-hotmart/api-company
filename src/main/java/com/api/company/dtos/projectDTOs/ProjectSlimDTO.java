package com.api.company.dtos.projectDTOs;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectSlimDTO {
    @NotNull
    private Long id;
    private String name;

}
