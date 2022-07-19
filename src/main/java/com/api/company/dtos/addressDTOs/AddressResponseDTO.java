package com.api.company.dtos.addressDTOs;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import com.api.company.dtos.employeeDTOs.EmployeeSlimDTO;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressResponseDTO {
    @NotNull
    private Long id;
    private String country;
    private String fu;
    private String city;
    private String street;
    private Integer number;
    private String postalCode;
    @Valid
    private EmployeeSlimDTO employee;
}
