package com.api.company.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "department")
public class DepartmentModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, length = 60)
    private String name;
    @Column(nullable = false, unique = true)
    private Long number;
    @OneToMany(mappedBy="department", fetch = FetchType.LAZY)
    private List<ProjectModel> projects;

}
