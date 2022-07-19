package com.api.company.dtos.addressDTOs;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class AddressInsertDTO {
    @NotBlank
    @Size(max = 45)
    private String country;
    @NotBlank
    @Size(max = 45)
    private String fu;
    @NotBlank
    @Size(max = 45)
    private String city;
    @NotBlank
    @Size(max = 150)
    private String street;
    @NotNull
    private Integer number;
    @NotBlank
    @Size(max = 45)
    private String postalCode;
}
