package com.api.company.services;

import com.api.company.enums.ResponseEnum;
import com.api.company.models.EmployeeModel;
import com.api.company.models.EmployeeProjectModel;
import com.api.company.repositories.EmployeeProjectRepository;
import com.api.company.repositories.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class EmployeeService {

    final EmployeeRepository employeeRepository;

    final EmployeeProjectRepository employeeProjectRepository;
    final AddressService addressService;
    final ProjectService projectService;

    public EmployeeService(EmployeeRepository employeeRepository, AddressService addressService,
                           EmployeeProjectRepository employeeProjectRepository,
                           ProjectService projectService) {
        this.employeeRepository = employeeRepository;
        this.addressService = addressService;
        this.employeeProjectRepository = employeeProjectRepository;
        this.projectService = projectService;
    }

    public EmployeeModel insert(EmployeeModel employeeModel){
        return employeeRepository.save(employeeModel);
    }

    public List<EmployeeModel> findAll(){
        return employeeRepository.findAll();
    }

    public EmployeeModel findById(Long id){
        Optional<EmployeeModel> employeeModelOptional = employeeRepository.findById(id);
        if(employeeModelOptional.isPresent() == Boolean.FALSE) {
            throw new EntityNotFoundException(ResponseEnum.EMPLOYEE_NOT_FOUND_BY_ID
                    .getMessage().concat(String.valueOf(id)));
        }
        return employeeModelOptional.get();
    }
    @Transactional
    public void delete(EmployeeModel employeeModel){
        employeeRepository.delete(employeeModel);
    }

    public List<EmployeeModel> findAllByNameContaining(String name){
        List<EmployeeModel> employeeModels = employeeRepository.findAllByNameContaining(name);
        return employeeModels;
    }

    public List<EmployeeModel> findAllEmployeesByDepartmentId(Long idDepartment){
        List<EmployeeModel> employeeModels = employeeRepository.findAllEmployeesByDepartmentId(idDepartment);
        return employeeModels;
    }

    public List<EmployeeModel> findAllSupervisedBySupervisorId(Long idSupervisor){
        List<EmployeeModel> employeeModels = employeeRepository.findAllSupervisedBySupervisorId(idSupervisor);
        return employeeModels;
    }

    @Transactional
    public EmployeeModel updateItselfAndRelated(EmployeeModel employeeModel){
        EmployeeModel employeeModelDB = findById(employeeModel.getId());
        Double salaryEmployeeDB = employeeModelDB.getSalary();

        EmployeeModel employeeModelSaved = employeeRepository.save(employeeModel);

        if(salaryEmployeeDB.equals(employeeModel.getSalary()) == Boolean.FALSE){
            updateProjectsCost(employeeModel);
        }

        return employeeModelSaved;
    }

    public void updateProjectsCost(EmployeeModel employeeModel){
        List<EmployeeProjectModel> employeeProjectModels = employeeProjectRepository
                .findAllEmployeeProjectsByEmployeeId(employeeModel.getId());

        employeeProjectModels.forEach(employeeProjectModel ->
                projectService.setProjectCostUpdated(employeeProjectModel.getProject())
        );
    }
}
