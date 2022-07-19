package com.api.company.unitTests;

import com.api.company.enums.ResponseEnum;
import com.api.company.models.AddressModel;
import com.api.company.models.EmployeeModel;
import com.api.company.models.EmployeeProjectModel;
import com.api.company.repositories.EmployeeProjectRepository;
import com.api.company.repositories.EmployeeRepository;
import com.api.company.services.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private EmployeeProjectRepository employeeProjectRepository;
    @InjectMocks
    @Spy
    private EmployeeService employeeService;

    private EmployeeModel dummyEmployee;

    @BeforeEach
    void initEmployee() {
        dummyEmployee = populateAndReturnDummyEmployee();
    }

    public static EmployeeModel populateAndReturnDummyEmployee(){
        EmployeeModel employeeModel = new EmployeeModel();
        AddressModel dummyAddress = new AddressModel();
        dummyAddress.setId(1L);
        dummyAddress.setNumber(3L);
        employeeModel.setName("Test Employee");
        employeeModel.setCpf("843.894.140-03");
        employeeModel.setBirthdate(LocalDate.now());
        employeeModel.setGender("Male");
        employeeModel.setSalary(3784D);
        employeeModel.setAddress(dummyAddress);

        return employeeModel;
    }

    @Test
    void insertedEmployeeHasMandatoryFields(){
        EmployeeModel employeeModel = dummyEmployee;
        when(employeeRepository.save(any(EmployeeModel.class))).then(returnsFirstArg());

        EmployeeModel savedEmployee = employeeService.insert(employeeModel);

        assertEquals(savedEmployee.getName(),employeeModel.getName());
        assertEquals(savedEmployee.getCpf(),employeeModel.getCpf());
        assertEquals(savedEmployee.getBirthdate(),employeeModel.getBirthdate());
        assertEquals(savedEmployee.getGender(),employeeModel.getGender());
        assertEquals(savedEmployee.getSalary(),employeeModel.getSalary());
        assertEquals(savedEmployee.getAddress(),employeeModel.getAddress());
        verify(employeeRepository,times(1)).save(employeeModel);
    }

    @Test
    void findEmployeeByIdSuccess(){
        EmployeeModel employeeModel = dummyEmployee;
        employeeModel.setId(1L);
        when(employeeRepository.findById(employeeModel.getId())).thenReturn(Optional.of(employeeModel));

        EmployeeModel returnedEmployeeModel = employeeService.findById(employeeModel.getId());

        assertNotNull(returnedEmployeeModel);
        assertEquals(employeeModel.getId(),returnedEmployeeModel.getId());
        verify(employeeRepository,times(1)).findById(employeeModel.getId());
    }

    @Test
    void findEmployeeByIdThrowEntityNotFoundException(){
        EmployeeModel employeeModel = dummyEmployee;
        employeeModel.setId(1L);
        when(employeeRepository.findById(employeeModel.getId())).thenReturn(Optional.empty());

        Throwable exception = assertThrows(
                EntityNotFoundException.class, () -> employeeService.findById(employeeModel.getId())
        );

        assertEquals(ResponseEnum.EMPLOYEE_NOT_FOUND_BY_ID.getMessage()
                .concat(employeeModel.getId().toString()),exception.getMessage());
        verify(employeeRepository,times(1)).findById(employeeModel.getId());
    }

    @Test
    void deleteEmployeeSuccess(){
        EmployeeModel employeeModel = dummyEmployee;
        employeeRepository.delete(employeeModel);
        verify(employeeRepository, times(1)).delete(employeeModel);
    }

    @Test
    void findAllEmployeesByDepartmentId(){
        Long departmentId = 2L;
        EmployeeModel employeeModel = dummyEmployee;
        List<EmployeeModel> employeeModelList = new ArrayList<>();
        employeeModelList.add(employeeModel);
        when(employeeRepository.findAllEmployeesByDepartmentId(departmentId)).thenReturn(employeeModelList);

        List<EmployeeModel> returnedEmployees = employeeService.findAllEmployeesByDepartmentId(departmentId);

        assertEquals(returnedEmployees, employeeModelList);
        verify(employeeRepository,times(1)).findAllEmployeesByDepartmentId(departmentId);
    }

    @Test
    void findAllSupervisedBySupervisorId(){
        Long supervisorId = 3L;
        EmployeeModel employeeModel = dummyEmployee;
        List<EmployeeModel> employeeModelList = new ArrayList<>();
        employeeModelList.add(employeeModel);
        when(employeeRepository.findAllSupervisedBySupervisorId(supervisorId)).thenReturn(employeeModelList);

        List<EmployeeModel> returnedEmployees = employeeService.findAllSupervisedBySupervisorId(supervisorId);

        assertEquals(returnedEmployees, employeeModelList);
        verify(employeeRepository,times(1)).findAllSupervisedBySupervisorId(supervisorId);
    }

    @Test
    void updateItselfAndRelated(){
        EmployeeModel employeeDBModel = dummyEmployee;
        employeeDBModel.setId(1L);
        EmployeeModel employeeToUpdateModel = new EmployeeModel();
        employeeToUpdateModel.setId(1L);
        employeeToUpdateModel.setName("Test Employee Updated");
        employeeToUpdateModel.setCpf("5867459876");
        employeeToUpdateModel.setSalary(9843D);

        when(employeeRepository.findById(employeeDBModel.getId())).thenReturn(Optional.of(employeeDBModel));
        when(employeeRepository.save(employeeToUpdateModel)).thenReturn(employeeToUpdateModel);

        EmployeeModel employeeModelSaved = employeeService.updateItselfAndRelated(employeeToUpdateModel);
        assertNotNull(employeeModelSaved);
        assertEquals(employeeToUpdateModel.getId(),employeeModelSaved.getId());
        assertEquals(employeeToUpdateModel.getName(),employeeModelSaved.getName());
        assertEquals(employeeToUpdateModel.getCpf(),employeeModelSaved.getCpf());
        assertEquals(employeeToUpdateModel.getSalary(),employeeModelSaved.getSalary());
        verify(employeeRepository,times(1)).save(employeeToUpdateModel);
    }

    @Test
    void updateItselfAndRelatedCauseUpdateProjectsCost(){
        EmployeeModel employeeDBModel = dummyEmployee;
        employeeDBModel.setId(1L);
        EmployeeModel employeeToUpdateModel = new EmployeeModel();
        employeeToUpdateModel.setId(1L);
        employeeToUpdateModel.setSalary(employeeDBModel.getSalary() + 35D);

        when(employeeRepository.findById(employeeDBModel.getId())).thenReturn(Optional.of(employeeDBModel));
        when(employeeRepository.save(employeeToUpdateModel)).thenReturn(employeeToUpdateModel);
        when(employeeProjectRepository.findAllEmployeeProjectsByEmployeeId(employeeToUpdateModel.getId())).thenReturn(Collections.emptyList());

        EmployeeModel employeeModelSaved = employeeService.updateItselfAndRelated(employeeToUpdateModel);

        assertNotNull(employeeModelSaved);
        verify(employeeService,times(1)).updateProjectsCost(employeeToUpdateModel);
    }

    @Test
    void updateItselfAndRelatedNotCauseUpdateProjectsCost(){
        EmployeeModel employeeDBModel = dummyEmployee;
        employeeDBModel.setId(1L);
        EmployeeModel employeeToUpdateModel = new EmployeeModel();
        employeeToUpdateModel.setId(1L);
        employeeToUpdateModel.setSalary(employeeDBModel.getSalary());

        when(employeeRepository.findById(employeeDBModel.getId())).thenReturn(Optional.of(employeeDBModel));
        when(employeeRepository.save(employeeToUpdateModel)).thenReturn(employeeToUpdateModel);

        EmployeeModel employeeModel = employeeService.updateItselfAndRelated(employeeToUpdateModel);

        assertNotNull(employeeModel);
        verify(employeeService,times(0)).updateProjectsCost(employeeToUpdateModel);
    }

}
