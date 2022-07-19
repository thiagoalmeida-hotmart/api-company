package com.api.company.controllers;

import com.api.company.dtos.departmentDTOs.*;
import com.api.company.models.DepartmentModel;
import com.api.company.services.DepartmentService;
import com.api.company.utils.ObjectMapperUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/department")
public class DepartmentController {

    private final DepartmentService departmentService;
    private final ObjectMapperUtils objectMapperUtils;

    public DepartmentController(DepartmentService departmentService, ObjectMapperUtils objectMapperUtils) {
        this.departmentService = departmentService;
        this.objectMapperUtils = objectMapperUtils;
    }

    @PostMapping("/insert")
    public ResponseEntity<Object> save(@Valid @RequestBody DepartmentInsertDTO departmentInsertDto){
        DepartmentModel departmentModel = objectMapperUtils.map(departmentInsertDto, DepartmentModel.class);
        DepartmentModel departmentModelSaved = departmentService.save(departmentModel);
        DepartmentResponseDTO departmentResponseDTO = objectMapperUtils.map(departmentModelSaved, DepartmentResponseDTO.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(departmentResponseDTO);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<DepartmentResponseDTO>> getAll(){
        List<DepartmentResponseDTO> departmentResponseDTOS = objectMapperUtils
                .mapAll(departmentService.findAll(),DepartmentResponseDTO.class);
        return ResponseEntity.status(HttpStatus.OK).body(departmentResponseDTOS);
    }

    @GetMapping("/getOneById/{id}")
    public ResponseEntity<Object> getOneById(@PathVariable(value = "id") Long id){
        DepartmentModel departmentModel = departmentService.findById(id);
        DepartmentResponseDTO departmentResponseDTO = objectMapperUtils.map(departmentModel, DepartmentResponseDTO.class);
        return ResponseEntity.status(HttpStatus.OK).body(departmentResponseDTO);
    }

    @PostMapping("/getBudgetStatusById/{id}")
    public ResponseEntity<Object> getBudgetStatusById(@PathVariable(value = "id") Long id,
                                                         @Valid @RequestBody DepartmentBudgetStatusDTO
                                                                 departmentBudgetStatusDTO){
        DepartmentModel departmentModel = departmentService.findById(id);
        DepartmentBudgetStatusResponseDTO responseDTO = departmentService
                .getBudgetStatusByDate(departmentBudgetStatusDTO.getSearchDate(), departmentModel);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable(value = "id") Long id){
        DepartmentModel departmentModel = departmentService.findById(id);
        departmentService.delete(departmentModel);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/update")
    public ResponseEntity<Object> update(@RequestBody @Valid DepartmentUpdateDTO departmentUpdateDTO){
        departmentService.findById(departmentUpdateDTO.getId());
        DepartmentModel departmentModel = objectMapperUtils.map(departmentUpdateDTO, DepartmentModel.class);
        DepartmentModel departmentModelSaved = departmentService.save(departmentModel);
        DepartmentResponseDTO departmentResponseDTO = objectMapperUtils.map(departmentModelSaved, DepartmentResponseDTO.class);
        return ResponseEntity.status(HttpStatus.OK).body(departmentResponseDTO);
    }

}
