package com.api.company.unitTests;

import com.api.company.enums.ResponseEnum;
import com.api.company.models.EmployeeModel;
import com.api.company.models.ProjectModel;
import com.api.company.repositories.EmployeeRepository;
import com.api.company.repositories.ProjectRepository;
import com.api.company.services.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private EmployeeRepository employeeRepository;
    @InjectMocks
    private ProjectService projectService;

    private ProjectModel dummyProjectModel;

    @BeforeEach
    void initEmployeeProject() {
        dummyProjectModel = populateAndReturnDummyProject();
    }

    public static ProjectModel populateAndReturnDummyProject(){
        ProjectModel projectModel = new ProjectModel();
        projectModel.setCost(2500D);
        projectModel.setValue(2500D);
        projectModel.setName("SAP Project");
        projectModel.setStartDate(LocalDate.now());
        projectModel.setEndDate(LocalDate.now().plus(10, ChronoUnit.DAYS));

        return projectModel;
    }

    @Test
    void insertedProjectHasMandatoryFields(){
        ProjectModel projectModel = dummyProjectModel;
        when(projectRepository.save(any(ProjectModel.class))).then(returnsFirstArg());

        ProjectModel savedProject = projectService.save(projectModel);

        assertEquals(savedProject.getId(),projectModel.getId());
        assertEquals(savedProject.getName(),projectModel.getName());
        assertEquals(savedProject.getCost(),projectModel.getCost());
        assertEquals(savedProject.getValue(),projectModel.getValue());
        assertEquals(savedProject.getStartDate(),projectModel.getStartDate());
        assertEquals(savedProject.getEndDate(),projectModel.getEndDate());
        verify(projectRepository,times(1)).save(projectModel);
    }

    @Test
    void updateProject(){
        ProjectModel projectDBModel = dummyProjectModel;
        projectDBModel.setId(1L);
        ProjectModel projectToUpdateModel = new ProjectModel();
        projectToUpdateModel.setId(1L);
        projectToUpdateModel.setName("AWS Project");
        projectToUpdateModel.setValue(3450D);

        when(projectRepository.save(projectToUpdateModel)).thenReturn(projectToUpdateModel);

        ProjectModel projectModelSaved = projectService.save(projectToUpdateModel);
        assertNotNull(projectModelSaved);
        assertEquals(projectToUpdateModel.getId(),projectModelSaved.getId());
        assertEquals(projectToUpdateModel.getName(),projectModelSaved.getName());
        assertEquals(projectToUpdateModel.getValue(),projectModelSaved.getValue());
        verify(projectRepository,times(1)).save(projectToUpdateModel);
    }

    @Test
    void findDepartmentByIdSuccess(){
        ProjectModel projectModel = dummyProjectModel;
        projectModel.setId(1L);
        when(projectRepository.findById(projectModel.getId())).thenReturn(Optional.of(projectModel));

        ProjectModel returnedProjectModel = projectService.findById(projectModel.getId());

        assertNotNull(returnedProjectModel);
        assertEquals(projectModel.getId(),returnedProjectModel.getId());
        verify(projectRepository,times(1)).findById(projectModel.getId());
    }

    @Test
    void findAddressByIdThrowEntityNotFoundException(){
        ProjectModel projectModel = dummyProjectModel;
        projectModel.setId(1L);
        when(projectRepository.findById(projectModel.getId())).thenReturn(Optional.empty());

        Throwable exception = assertThrows(
                EntityNotFoundException.class, () -> projectService.findById(projectModel.getId())
        );

        assertEquals(ResponseEnum.PROJECT_NOT_FOUND_BY_ID.getMessage()
                .concat(projectModel.getId().toString()),exception.getMessage());
        verify(projectRepository,times(1)).findById(projectModel.getId());
    }

    @Test
    void deleteAddressSuccess(){
        ProjectModel projectModel = dummyProjectModel;
        projectRepository.delete(projectModel);
        verify(projectRepository, times(1)).delete(projectModel);
    }

    @Test
    void setProjectCostUpdatedByEmployee(){
        ProjectModel projectModel = dummyProjectModel;
        projectModel.setId(2L);
        Double previousProjectCost = projectModel.getCost();
        EmployeeModel employeeModel = EmployeeServiceTest.populateAndReturnDummyEmployee();

        when(employeeRepository.findAllEmployeesByProjectId(projectModel.getId()))
                .thenReturn(Arrays.asList(employeeModel));

        ProjectModel updatedProject = projectService.setProjectCostUpdated(projectModel);

        assertEquals(previousProjectCost + employeeModel.getSalary(),updatedProject.getCost());
    }

    @Test
    void setProjectCostUpdatedByCostChange(){
        ProjectModel projectModel = dummyProjectModel;
        projectModel.setId(2L);
        Double previousProjectCost = projectModel.getCost();
        projectModel.setCost(3450D);

        ProjectModel updatedProject = projectService.setProjectCostUpdated(projectModel);

        assertEquals(projectModel.getCost(),updatedProject.getCost());
        assertNotEquals(previousProjectCost,updatedProject.getCost());
    }

    @Test
    void findAllProjectsByEmployeeId(){
        Long employeeId = 3L;
        ProjectModel projectModel = dummyProjectModel;
        List<ProjectModel> projectModelList = new ArrayList<>();
        projectModelList.add(projectModel);
        when(projectRepository.findAllProjectsByEmployeeId(employeeId)).thenReturn(projectModelList);

        List<ProjectModel> returnedProjects = projectService.findAllProjectsByEmployeeId(employeeId);

        assertEquals(returnedProjects, projectModelList);
        verify(projectRepository,times(1)).findAllProjectsByEmployeeId(employeeId);
    }
}
