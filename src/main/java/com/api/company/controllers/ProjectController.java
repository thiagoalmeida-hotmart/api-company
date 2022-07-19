package com.api.company.controllers;

import com.api.company.dtos.projectDTOs.ProjectInsertDTO;
import com.api.company.dtos.projectDTOs.ProjectUpdateDTO;
import com.api.company.dtos.projectDTOs.ProjectResponseDTO;
import com.api.company.models.ProjectModel;
import com.api.company.services.EmployeeService;
import com.api.company.services.ProjectService;
import com.api.company.utils.ObjectMapperUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/project")
public class ProjectController {

    private final ProjectService projectService;
    private final EmployeeService employeeService;
    private final ObjectMapperUtils objectMapperUtils;

    public ProjectController(ProjectService projectService, ObjectMapperUtils objectMapperUtils,
                             EmployeeService employeeService){
        this.projectService = projectService;
        this.objectMapperUtils = objectMapperUtils;
        this.employeeService = employeeService;
    }

    @PostMapping("/insert")
    public ResponseEntity<Object> save(@Valid @RequestBody ProjectInsertDTO projectInsertDTO){
        ProjectModel projectModel = objectMapperUtils.map(projectInsertDTO, ProjectModel.class);
        projectService.validateModel(projectModel);
        projectModel.setCost(projectModel.getValue());
        ProjectModel projectModelSaved = projectService.save(projectModel);
        ProjectResponseDTO projectResponseDTO = objectMapperUtils.map(projectModelSaved, ProjectResponseDTO.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(projectResponseDTO);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<ProjectResponseDTO>> getAll(){
        List<ProjectResponseDTO> projectResponseDTOS = objectMapperUtils
                .mapAll(projectService.findAll(),ProjectResponseDTO.class);
        return ResponseEntity.status(HttpStatus.OK).body(projectResponseDTOS);
    }

    @GetMapping("/getAllByEmployeeId/{idEmployee}")
    public ResponseEntity<Object> getAllByEmployeeId(@PathVariable(value = "idEmployee") Long idEmployee){
        employeeService.findById(idEmployee);
        List<ProjectModel> projectModels = projectService.findAllProjectsByEmployeeId(idEmployee);
        List<ProjectResponseDTO> employeeResponseDTOS = objectMapperUtils.mapAll(projectModels,
                ProjectResponseDTO.class);
        return ResponseEntity.status(HttpStatus.OK).body(employeeResponseDTOS);
    }

    @GetMapping("/getOneById/{id}")
    public ResponseEntity<Object> getOneById(@PathVariable(value = "id") Long id){
        ProjectModel projectModel = projectService.findById(id);
        ProjectResponseDTO projectResponseDTO = objectMapperUtils.map(projectModel, ProjectResponseDTO.class);
        return ResponseEntity.status(HttpStatus.OK).body(projectResponseDTO);
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable(value = "id") Long id){
        ProjectModel projectModel = projectService.findById(id);
        projectService.delete(projectModel);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/update")
    public ResponseEntity<Object> update(@RequestBody @Valid ProjectUpdateDTO projectUpdateDTO){
        ProjectResponseDTO projectResponseDTO;
        ProjectModel projectModel;

        projectService.findById(projectUpdateDTO.getId());
        projectModel = objectMapperUtils.map(projectUpdateDTO, ProjectModel.class);
        projectModel.setCost(projectUpdateDTO.getValue());
        projectModel = projectService.setProjectCostUpdated(projectModel);
        ProjectModel projectModelSaved = projectService.save(projectModel);
        projectResponseDTO = objectMapperUtils.map(projectModelSaved, ProjectResponseDTO.class);

        return ResponseEntity.status(HttpStatus.OK).body(projectResponseDTO);
    }
}
