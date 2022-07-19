package com.api.company.controllers;

import com.api.company.dtos.employeeProjectDTOs.EmployeeProjectInsertDTO;
import com.api.company.dtos.employeeProjectDTOs.EmployeeProjectUpdateDTO;
import com.api.company.dtos.employeeProjectDTOs.EmployeeProjectResponseDTO;
import com.api.company.models.EmployeeProjectModel;
import com.api.company.services.EmployeeProjectService;
import com.api.company.utils.ObjectMapperUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/employee_project")
public class EmployeeProjectController {

    private final EmployeeProjectService employeeProjectService;
    private final ObjectMapperUtils objectMapperUtils;

    public EmployeeProjectController(EmployeeProjectService employeeProjectService, ObjectMapperUtils objectMapperUtils) {
        this.employeeProjectService = employeeProjectService;
        this.objectMapperUtils = objectMapperUtils;
    }

    @PostMapping("/insert")
    public ResponseEntity<Object> insert(@RequestBody @Valid EmployeeProjectInsertDTO employeeProjectInsertDTO){
        EmployeeProjectModel employeeProjectModel = objectMapperUtils.map(employeeProjectInsertDTO, EmployeeProjectModel.class);
        EmployeeProjectModel employeeProjectModelSaved = employeeProjectService.insertAndUpdateRelated(employeeProjectModel);
        EmployeeProjectResponseDTO employeeProjectResponseDTO = objectMapperUtils.map(employeeProjectModelSaved, EmployeeProjectResponseDTO.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeProjectResponseDTO);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<EmployeeProjectResponseDTO>> getAll(){
        List<EmployeeProjectResponseDTO> employeeProjectResponseDTOS = objectMapperUtils
                .mapAll(employeeProjectService.findAll(),EmployeeProjectResponseDTO.class);
        return ResponseEntity.status(HttpStatus.OK).body(employeeProjectResponseDTOS);
    }

    @GetMapping("/getOneById/{id}")
    public ResponseEntity<Object> getOneById(@PathVariable(value = "id") Long id){
        EmployeeProjectModel employeeProjectModel = employeeProjectService.findById(id);
        EmployeeProjectResponseDTO employeeProjectResponseDTO = objectMapperUtils
                .map(employeeProjectModel, EmployeeProjectResponseDTO.class);
        return ResponseEntity.status(HttpStatus.OK).body(employeeProjectResponseDTO);
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable(value = "id") Long id){
        EmployeeProjectModel employeeProjectModel = employeeProjectService.findById(id);
        employeeProjectService.deleteAndUpdateRelated(employeeProjectModel);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/update")
    public ResponseEntity<Object> update(@RequestBody @Valid EmployeeProjectUpdateDTO employeeProjectUpdateDTO){
        employeeProjectService.findById(employeeProjectUpdateDTO.getId());
        EmployeeProjectModel employeeProjectModel = objectMapperUtils.map(employeeProjectUpdateDTO, EmployeeProjectModel.class);
        EmployeeProjectModel employeeProjectModelSaved = employeeProjectService.updateItselfAndRelated(employeeProjectModel);
        EmployeeProjectResponseDTO employeeProjectResponseDTO = objectMapperUtils.map(employeeProjectModelSaved, EmployeeProjectResponseDTO.class);
        return ResponseEntity.status(HttpStatus.OK).body(employeeProjectResponseDTO);
    }
}
