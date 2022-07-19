package com.api.company.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
@Getter
@Setter
@Entity(name = "address")
public class AddressModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, length = 45)
    private String country;
    @Column(nullable = false, length = 45)
    private String fu;
    @Column(nullable = false, length = 45)
    private String city;
    @Column(nullable = false, length = 150)
    private String street;
    @Column(nullable = false, length = 45)
    private Long number;
    @Column(nullable = false, length = 45)
    private String postalCode;
    @OneToOne(mappedBy = "address")
    private EmployeeModel employee;
}
