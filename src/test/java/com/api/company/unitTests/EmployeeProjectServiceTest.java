package com.api.company.unitTests;

import com.api.company.enums.ResponseEnum;
import com.api.company.models.EmployeeModel;
import com.api.company.models.EmployeeProjectModel;
import com.api.company.models.ProjectModel;
import com.api.company.repositories.EmployeeProjectRepository;
import com.api.company.services.EmployeeProjectService;
import com.api.company.services.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeProjectServiceTest {

    @Mock
    private EmployeeProjectRepository employeeProjectRepository;
    @Mock
    private ProjectService projectService;
    @InjectMocks
    @Spy
    private EmployeeProjectService employeeProjectService;

    private EmployeeProjectModel dummyEmployeeProjectModel;

    @BeforeEach
    void initEmployeeProject() {
        dummyEmployeeProjectModel = new EmployeeProjectModel();
        dummyEmployeeProjectModel.setRole("Developer");
        dummyEmployeeProjectModel.setEmployee(EmployeeServiceTest.populateAndReturnDummyEmployee());
        dummyEmployeeProjectModel.setProject(ProjectServiceTest.populateAndReturnDummyProject());
    }

    @Test
    void insertedEmployeeProjectHasMandatoryFields(){
        EmployeeProjectModel employeeProjectModel = dummyEmployeeProjectModel;
        when(employeeProjectRepository.save(any(EmployeeProjectModel.class))).then(returnsFirstArg());

        EmployeeProjectModel savedEmployeeProjectModel = employeeProjectService
                .insertAndUpdateRelated(employeeProjectModel);

        assertEquals(savedEmployeeProjectModel.getProject(),employeeProjectModel.getProject());
        assertEquals(savedEmployeeProjectModel.getEmployee(),employeeProjectModel.getEmployee());
        assertEquals(savedEmployeeProjectModel.getRole(),employeeProjectModel.getRole());
        verify(employeeProjectRepository,times(1)).save(employeeProjectModel);
    }

    @Test
    void updateEmployeeProject(){
        EmployeeProjectModel employeeProjectDBModel = dummyEmployeeProjectModel;
        employeeProjectDBModel.setId(1L);
        employeeProjectDBModel.getEmployee().setId(2L);
        employeeProjectDBModel.getProject().setId(3L);
        EmployeeProjectModel employeeProjectToUpdateModel = new EmployeeProjectModel();
        employeeProjectToUpdateModel.setId(1L);
        employeeProjectToUpdateModel.setEmployee(employeeProjectDBModel.getEmployee());
        employeeProjectToUpdateModel.setProject(employeeProjectDBModel.getProject());
        employeeProjectToUpdateModel.setRole("Manager");

        when(employeeProjectRepository.save(employeeProjectToUpdateModel))
                .thenReturn(employeeProjectToUpdateModel);
        when(employeeProjectRepository.findById(employeeProjectToUpdateModel.getId()))
                .thenReturn(Optional.of(employeeProjectToUpdateModel));

        EmployeeProjectModel employeeProjectModelSaved = employeeProjectService
                .updateItselfAndRelated(employeeProjectToUpdateModel);

        assertNotNull(employeeProjectModelSaved);
        assertEquals(employeeProjectToUpdateModel.getId(),employeeProjectModelSaved.getId());
        assertEquals(employeeProjectToUpdateModel.getRole(),employeeProjectModelSaved.getRole());
        verify(employeeProjectRepository,times(1)).save(employeeProjectToUpdateModel);
    }

    @Test
    void updateEmployeeProjectAndUpdateProjectCostForBothProjects(){
        EmployeeProjectModel employeeProjectDBModel = dummyEmployeeProjectModel;
        employeeProjectDBModel.setId(1L);
        employeeProjectDBModel.getEmployee().setId(2L);
        employeeProjectDBModel.getProject().setId(3L);

        ProjectModel newProjectModel = ProjectServiceTest.populateAndReturnDummyProject();
        newProjectModel.setId(1L);
        EmployeeProjectModel employeeProjectToUpdateModel = new EmployeeProjectModel();
        employeeProjectToUpdateModel.setId(1L);
        employeeProjectToUpdateModel.setEmployee(employeeProjectDBModel.getEmployee());
        employeeProjectToUpdateModel.setProject(newProjectModel);
        employeeProjectToUpdateModel.setRole("Manager");

        when(employeeProjectRepository.findById(employeeProjectToUpdateModel.getId()))
                .thenReturn(Optional.of(employeeProjectDBModel));
        when(employeeProjectRepository.save(employeeProjectToUpdateModel))
                .thenReturn(employeeProjectToUpdateModel);

        employeeProjectService.updateItselfAndRelated(employeeProjectToUpdateModel);

        verify(employeeProjectService,times(1))
                .updateProjectCost(employeeProjectDBModel.getProject());
        verify(employeeProjectService,times(1))
                .updateProjectCost(newProjectModel);
    }

    @Test
    void updateEmployeeProjectAndUpdateProjectCostForSameProjectOnlyOnce(){
        EmployeeProjectModel employeeProjectDBModel = dummyEmployeeProjectModel;
        employeeProjectDBModel.setId(1L);
        employeeProjectDBModel.getEmployee().setId(2L);
        employeeProjectDBModel.getProject().setId(3L);

        EmployeeModel newEmployeeModel = EmployeeServiceTest.populateAndReturnDummyEmployee();
        newEmployeeModel.setId(1L);
        EmployeeProjectModel employeeProjectToUpdateModel = new EmployeeProjectModel();
        employeeProjectToUpdateModel.setId(1L);
        employeeProjectToUpdateModel.setEmployee(newEmployeeModel);
        employeeProjectToUpdateModel.setProject(employeeProjectDBModel.getProject());
        employeeProjectToUpdateModel.setRole("Manager");

        when(employeeProjectRepository.findById(employeeProjectToUpdateModel.getId()))
                .thenReturn(Optional.of(employeeProjectDBModel));
        when(employeeProjectRepository.save(employeeProjectToUpdateModel))
                .thenReturn(employeeProjectToUpdateModel);

        employeeProjectService.updateItselfAndRelated(employeeProjectToUpdateModel);

        verify(employeeProjectService,times(1))
                .updateProjectCost(employeeProjectDBModel.getProject());
    }

    @Test
    void findEmployeeProjectByIdSuccess(){
        EmployeeProjectModel employeeProjectModel = dummyEmployeeProjectModel;
        employeeProjectModel.setId(1L);
        when(employeeProjectRepository.findById(employeeProjectModel.getId())).thenReturn(Optional.of(employeeProjectModel));

        EmployeeProjectModel returnedEmployeeProjectModel = employeeProjectService.findById(employeeProjectModel.getId());

        assertNotNull(returnedEmployeeProjectModel);
        assertEquals(employeeProjectModel.getId(),returnedEmployeeProjectModel.getId());
        verify(employeeProjectRepository,times(1)).findById(employeeProjectModel.getId());
    }

    @Test
    void findEmployeeProjectByIdThrowEntityNotFoundException(){
        EmployeeProjectModel employeeProjectModel = dummyEmployeeProjectModel;
        employeeProjectModel.setId(1L);
        when(employeeProjectRepository.findById(employeeProjectModel.getId())).thenReturn(Optional.empty());

        Throwable exception = assertThrows(
                EntityNotFoundException.class, () -> employeeProjectService.findById(employeeProjectModel.getId())
        );

        assertEquals(ResponseEnum.EMPLOYEE_PROJECT_NOT_FOUND_BY_ID.getMessage()
                .concat(employeeProjectModel.getId().toString()),exception.getMessage());
        verify(employeeProjectRepository,times(1)).findById(employeeProjectModel.getId());
    }

    @Test
    void deleteAddressSuccess(){
        EmployeeProjectModel employeeProjectModel = dummyEmployeeProjectModel;
        employeeProjectRepository.delete(employeeProjectModel);
        verify(employeeProjectRepository, times(1)).delete(employeeProjectModel);
    }
}
