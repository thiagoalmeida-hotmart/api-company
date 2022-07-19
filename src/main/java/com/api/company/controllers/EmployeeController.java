package com.api.company.controllers;

import com.api.company.dtos.employeeDTOs.EmployeeInsertDTO;
import com.api.company.dtos.employeeDTOs.EmployeeUpdateDTO;
import com.api.company.dtos.employeeDTOs.EmployeeResponseDTO;
import com.api.company.models.EmployeeModel;
import com.api.company.services.DepartmentService;
import com.api.company.services.EmployeeService;
import com.api.company.utils.ObjectMapperUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final DepartmentService departmentService;
    private final ObjectMapperUtils objectMapperUtils;

    public EmployeeController(EmployeeService employeeService, ObjectMapperUtils objectMapperUtils,
                              DepartmentService departmentService) {
        this.employeeService = employeeService;
        this.objectMapperUtils = objectMapperUtils;
        this.departmentService = departmentService;
    }

    @PostMapping("/insert")
    public ResponseEntity<Object> insert(@RequestBody @Valid EmployeeInsertDTO employeeInsertDto){
        EmployeeModel employeeModel = objectMapperUtils.map(employeeInsertDto, EmployeeModel.class);
        EmployeeModel employeeModelSaved = employeeService.insert(employeeModel);
        EmployeeResponseDTO employeeResponseDTO = objectMapperUtils.map(employeeModelSaved, EmployeeResponseDTO.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeResponseDTO);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<EmployeeResponseDTO>> getAll(){
        List<EmployeeResponseDTO> employeeResponseDTOS = objectMapperUtils
                .mapAll(employeeService.findAll(),EmployeeResponseDTO.class);
        return ResponseEntity.status(HttpStatus.OK).body(employeeResponseDTOS);
    }

    @GetMapping("/getOneById/{id}")
    public ResponseEntity<Object> getOneById(@PathVariable(value = "id") Long id){
        EmployeeModel employeeModel = employeeService.findById(id);
        EmployeeResponseDTO employeeResponseDTO = objectMapperUtils.map(employeeModel, EmployeeResponseDTO.class);
        return ResponseEntity.status(HttpStatus.OK).body(employeeResponseDTO);
    }

    @GetMapping("/getAllByDepartmentId/{idDepartment}")
    public ResponseEntity<Object> getAllByDepartmentId(@PathVariable(value = "idDepartment") Long idDepartment){
        departmentService.findById(idDepartment);
        List<EmployeeModel> employeeModels = employeeService.
                findAllEmployeesByDepartmentId(idDepartment);
        List<EmployeeResponseDTO> employeeResponseDTOS = objectMapperUtils.mapAll(employeeModels,
                                                EmployeeResponseDTO.class);
        return ResponseEntity.status(HttpStatus.OK).body(employeeResponseDTOS);
    }

    @GetMapping("/getAllByNameContaining/{nome}")
    public ResponseEntity<Object> getAllByNameContaining(@PathVariable(value = "nome") String nome){
        List<EmployeeModel> employeeModels = employeeService.findAllByNameContaining(nome);
        List<EmployeeResponseDTO> employeeResponseDTOS = objectMapperUtils.mapAll(employeeModels, EmployeeResponseDTO.class);
        return ResponseEntity.status(HttpStatus.OK).body(employeeResponseDTOS);
    }

    @GetMapping("/getAllSupervisedBySupervisorId/{idSupervisor}")
    public ResponseEntity<Object> getAllSupervisedBySupervisorId(@PathVariable(value = "idSupervisor") Long idSupervisor){
        employeeService.findById(idSupervisor);
        List<EmployeeModel> employeeModels = employeeService.findAllSupervisedBySupervisorId(idSupervisor);
        List<EmployeeResponseDTO> employeeResponseDTOS = objectMapperUtils.mapAll(employeeModels, EmployeeResponseDTO.class);
        return ResponseEntity.status(HttpStatus.OK).body(employeeResponseDTOS);
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable(value = "id") Long id){
        EmployeeModel employeeModel = employeeService.findById(id);
        employeeService.delete(employeeModel);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/update")
    public ResponseEntity<Object> update(@RequestBody @Valid EmployeeUpdateDTO employeeUpdateDTO){
        EmployeeModel employeeModel = objectMapperUtils.map(employeeUpdateDTO, EmployeeModel.class);
        EmployeeModel employeeModelSaved = employeeService.updateItselfAndRelated(employeeModel);
        EmployeeResponseDTO employeeResponseDTO = objectMapperUtils.map(employeeModelSaved, EmployeeResponseDTO.class);
        return ResponseEntity.status(HttpStatus.OK).body(employeeResponseDTO);
    }
}
