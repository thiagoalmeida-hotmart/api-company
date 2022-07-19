package com.api.company.dtos.employeeDTOs;

import com.api.company.dtos.addressDTOs.AddressResponseDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class EmployeeUpdateDTO {
    @NotNull
    private Long id;
    @NotBlank
    @Size(max = 60)
    private String name;
    @NotBlank
    @Size(max = 60)
    private String cpf;
    @NotNull
    @JsonFormat(pattern="dd/MM/yyyy")
    private LocalDate birthdate;
    @NotBlank
    private String gender;
    @NotNull
    private Double salary;
    @NotNull
    @Valid
    private AddressResponseDTO address;
    @Valid
    private List<EmployeeSlimDTO> supervised;
    @Valid
    private EmployeeSlimDTO supervisor;
}
