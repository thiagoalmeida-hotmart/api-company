package com.api.company.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
@Getter
@Setter
@Entity(name = "employee")
public class EmployeeModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, length = 60)
    private String name;
    @Column(nullable = false, length = 60)
    private String cpf;
    @Column(nullable = false)
    private LocalDate birthdate;
    @Column(nullable = false)
    private String gender;
    @Column(nullable = false)
    private Double salary;
    @OneToOne
    @JoinColumn(name="fk_employee_address_idx", unique = true, nullable = false)
    private AddressModel address;
    @OneToMany(mappedBy = "supervisor", fetch = FetchType.LAZY)
    private List<EmployeeModel> supervised;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="fk_employee_employee_idx")
    private EmployeeModel supervisor;
}
