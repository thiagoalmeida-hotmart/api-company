package com.api.company.services;

import com.api.company.enums.ResponseEnum;
import com.api.company.models.EmployeeModel;
import com.api.company.models.EmployeeProjectModel;
import com.api.company.models.ProjectModel;
import com.api.company.repositories.EmployeeProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeProjectService {

    private final EmployeeProjectRepository employeeProjectRepository;
    private final ProjectService projectService;

    public EmployeeProjectService(EmployeeProjectRepository employeeProjectRepository,
                                  ProjectService projectService) {
        this.employeeProjectRepository = employeeProjectRepository;
        this.projectService = projectService;
    }

    @Transactional
    public EmployeeProjectModel updateItselfAndRelated(EmployeeProjectModel employeeProjectModel){
        EmployeeProjectModel employeeProjectModelDB = findById(employeeProjectModel.getId());
        EmployeeProjectModel employeeProjectModelSaved = employeeProjectRepository
                .save(employeeProjectModel);

        checkAndUpdateProjectCostIfNeeded(employeeProjectModel,employeeProjectModelDB);
        return employeeProjectModelSaved;
    }

    public void checkAndUpdateProjectCostIfNeeded(EmployeeProjectModel employeeProjectModel,
                                              EmployeeProjectModel employeeProjectModelDB){

        EmployeeModel dbEmployee = employeeProjectModelDB.getEmployee();
        EmployeeModel newEmployee = employeeProjectModel.getEmployee();
        ProjectModel dbProject = employeeProjectModelDB.getProject();
        ProjectModel newProject = employeeProjectModel.getProject();

        Boolean shouldUpdateProjectCost = (
                (checkIdsEquals(dbEmployee.getId(),newEmployee.getId()) == Boolean.FALSE) ||
                        (checkIdsEquals(dbProject.getId(),newProject.getId()) == Boolean.FALSE)
        );

        if(shouldUpdateProjectCost == Boolean.TRUE) {
            checkAndUpdateProjectCost(dbProject,newProject);
        }
    }

    private void checkAndUpdateProjectCost(ProjectModel dbProject, ProjectModel newProject){
        if(checkIdsEquals(dbProject.getId(), newProject.getId()) == Boolean.FALSE){
            updateProjectCost(dbProject);
            updateProjectCost(newProject);
        }else{
            updateProjectCost(newProject);
        }
    }

    @Transactional
    public EmployeeProjectModel insertAndUpdateRelated(EmployeeProjectModel employeeProjectModel){
        EmployeeProjectModel employeeProjectModelSaved = employeeProjectRepository
                .save(employeeProjectModel);
        updateProjectCost(employeeProjectModel.getProject());
        return employeeProjectModelSaved;
    }

    public List<EmployeeProjectModel> findAll(){
        return employeeProjectRepository.findAll();
    }

    public EmployeeProjectModel findById(Long id){
        Optional<EmployeeProjectModel> employeeProjectModelOptional = employeeProjectRepository.findById(id);
        if(employeeProjectModelOptional.isPresent() == Boolean.FALSE) {
            throw new EntityNotFoundException(ResponseEnum.EMPLOYEE_PROJECT_NOT_FOUND_BY_ID
                    .getMessage().concat(String.valueOf(id)));
        }
        return employeeProjectModelOptional.get();
    }
    @Transactional
    public void deleteAndUpdateRelated(EmployeeProjectModel employeeProjectModel){
        ProjectModel projectModel = employeeProjectModel.getProject();
        employeeProjectRepository.delete(employeeProjectModel);
        updateProjectCost(projectModel);
    }

    public void updateProjectCost(ProjectModel projectModel){
        ProjectModel projectModelDB = projectService.findById(projectModel.getId());
        projectService.setProjectCostUpdated(projectModelDB);
    }

    private Boolean checkIdsEquals(Long idOne, Long idTwo){
        return idOne.equals(idTwo);
    }
}
