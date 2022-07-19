package com.api.company.controllers;

import com.api.company.dtos.budgetDTOs.BudgetInsertDTO;
import com.api.company.dtos.budgetDTOs.BudgetUpdateDTO;
import com.api.company.dtos.budgetDTOs.BudgetResponseDTO;
import com.api.company.models.BudgetModel;
import com.api.company.services.DepartmentService;
import com.api.company.services.BudgetService;
import com.api.company.utils.ObjectMapperUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/budget")
public class BudgetController {

    private final BudgetService budgetService;
    private final DepartmentService departmentService;
    private final ObjectMapperUtils objectMapperUtils;

    public BudgetController(BudgetService budgetService, ObjectMapperUtils objectMapperUtils,
                            DepartmentService departmentService){
        this.budgetService = budgetService;
        this.objectMapperUtils = objectMapperUtils;
        this.departmentService = departmentService;
    }

    @PostMapping("/insert")
    public ResponseEntity<Object> save(@Valid @RequestBody BudgetInsertDTO budgetInsertDTO){
        BudgetModel budgetModel = objectMapperUtils.map(budgetInsertDTO, BudgetModel.class);
        budgetService.validateModel(budgetModel);
        BudgetModel budgetModelSaved = budgetService.save(budgetModel);
        BudgetResponseDTO budgetResponseDTO = objectMapperUtils
                .map(budgetModelSaved, BudgetResponseDTO.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(budgetResponseDTO);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<BudgetResponseDTO>> getAll(){
        List<BudgetResponseDTO> budgetResponseDTOS = objectMapperUtils
                .mapAll(budgetService.findAll(), BudgetResponseDTO.class);
        return ResponseEntity.status(HttpStatus.OK).body(budgetResponseDTOS);
    }

    @GetMapping("/getAllByDepartmentId/{idDepartment}")
    public ResponseEntity<Object> getAllByDepartmentId(@PathVariable(value = "idDepartment") Long idDepartment){
        departmentService.findById(idDepartment);
        List<BudgetModel> budgetModels = budgetService.findAllBudgetsByDepartmentId(idDepartment);
        List<BudgetResponseDTO> budgetResponseDTOS = objectMapperUtils.mapAll(budgetModels,
                BudgetResponseDTO.class);
        return ResponseEntity.status(HttpStatus.OK).body(budgetResponseDTOS);
    }

    @GetMapping("/getOneById/{id}")
    public ResponseEntity<Object> getOneById(@PathVariable(value = "id") Long id){
        BudgetModel budgetModel = budgetService.findById(id);
        BudgetResponseDTO budgetResponseDTO = objectMapperUtils.map(budgetModel, BudgetResponseDTO.class);
        return ResponseEntity.status(HttpStatus.OK).body(budgetResponseDTO);
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable(value = "id") Long id){
        BudgetModel budgetModel = budgetService.findById(id);
        budgetService.delete(budgetModel);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/update")
    public ResponseEntity<Object> update(@RequestBody @Valid BudgetUpdateDTO budgetUpdateDTO){
        budgetService.findById(budgetUpdateDTO.getId());
        BudgetModel budgetModel = objectMapperUtils.map(budgetUpdateDTO, BudgetModel.class);
        BudgetModel budgetModelSaved = budgetService.save(budgetModel);
        BudgetResponseDTO budgetResponseDTO = objectMapperUtils.map(budgetModelSaved, BudgetResponseDTO.class);
        return ResponseEntity.status(HttpStatus.OK).body(budgetResponseDTO);
    }

}
