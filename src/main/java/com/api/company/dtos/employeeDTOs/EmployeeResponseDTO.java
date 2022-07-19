package com.api.company.dtos.employeeDTOs;

import com.api.company.dtos.addressDTOs.AddressResponseDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmployeeResponseDTO {
    private Long id;
    private String name;
    private String cpf;
    @JsonFormat(pattern="dd/MM/yyyy")
    private LocalDate birthdate;
    private String gender;
    private Double salary;
    private AddressResponseDTO address;
    private List<EmployeeSlimDTO> supervised;
    private EmployeeSlimDTO supervisor;
}
