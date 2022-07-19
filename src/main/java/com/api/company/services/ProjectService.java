package com.api.company.services;

import com.api.company.enums.ResponseEnum;
import com.api.company.models.EmployeeModel;
import com.api.company.models.ProjectModel;
import com.api.company.repositories.EmployeeRepository;
import com.api.company.repositories.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final DepartmentService departmentService;
    private final EmployeeRepository employeeRepository;

    public ProjectService(ProjectRepository projectRepository, DepartmentService departmentService,
                          EmployeeRepository employeeRepository) {
        this.projectRepository = projectRepository;
        this.departmentService = departmentService;
        this.employeeRepository = employeeRepository;
    }

    @Transactional
    public ProjectModel save(ProjectModel projectModel){
        return projectRepository.save(projectModel);
    }

    public List<ProjectModel> findAll(){
        return projectRepository.findAll();
    }

    public ProjectModel findById(Long id){
        Optional<ProjectModel> projectModelOptional = projectRepository.findById(id);
        if(projectModelOptional.isPresent() == Boolean.FALSE) {
            throw new EntityNotFoundException(ResponseEnum.PROJECT_NOT_FOUND_BY_ID
                    .getMessage().concat(String.valueOf(id)));
        }
        return projectModelOptional.get();
    }
    @Transactional
    public void delete(ProjectModel projectModel){
        projectRepository.delete(projectModel);
    }

    public void validateModel (ProjectModel projectModel){
        departmentService.findById(projectModel.getDepartment().getId());
    }

    public List<ProjectModel> findAllProjectsByEmployeeId(Long idEmployee){
        return projectRepository.findAllProjectsByEmployeeId(idEmployee);
    }

    public ProjectModel setProjectCostUpdated(ProjectModel projectModel){
        Double sumProjectCost = projectModel.getCost();

        List<EmployeeModel> employeesOfProject = employeeRepository
                .findAllEmployeesByProjectId(projectModel.getId());

        for(EmployeeModel employee : employeesOfProject){
            sumProjectCost += employee.getSalary();
        }

        projectModel.setCost(sumProjectCost);
        return projectModel;
    }
}
