package com.api.company.unitTests;

import com.api.company.dtos.departmentDTOs.DepartmentBudgetStatusResponseDTO;
import com.api.company.enums.ResponseEnum;
import com.api.company.enums.StatusBudgetEnum;
import com.api.company.models.DepartmentModel;
import com.api.company.repositories.BudgetRepository;
import com.api.company.repositories.DepartmentRepository;
import com.api.company.repositories.ProjectRepository;
import com.api.company.services.DepartmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;
    @Mock
    private BudgetRepository budgetRepository;
    @Mock
    private ProjectRepository projectRepository;
    @InjectMocks
    private DepartmentService departmentService;

    private DepartmentModel dummyDepartment;

    @BeforeEach
    void initDepartment() {
        dummyDepartment = new DepartmentModel();
        dummyDepartment.setId(2L);
        dummyDepartment.setNumber(12L);
        dummyDepartment.setName("HR Department");
    }

    @Test
    void insertedDepartmentHasMandatoryFields(){
        DepartmentModel departmentModel = dummyDepartment;
        when(departmentRepository.save(any(DepartmentModel.class))).then(returnsFirstArg());

        DepartmentModel savedDepartment = departmentService.save(departmentModel);

        assertEquals(savedDepartment.getId(),departmentModel.getId());
        assertEquals(savedDepartment.getNumber(),departmentModel.getNumber());
        assertEquals(savedDepartment.getName(),departmentModel.getName());
        verify(departmentRepository,times(1)).save(departmentModel);
    }

    @Test
    void updateDepartment(){
        DepartmentModel departmentDBModel = dummyDepartment;
        departmentDBModel.setId(1L);
        DepartmentModel departmentToUpdateModel = new DepartmentModel();
        departmentToUpdateModel.setId(1L);
        departmentToUpdateModel.setNumber(234L);
        departmentToUpdateModel.setName("New HR Department");

        when(departmentRepository.save(departmentToUpdateModel)).thenReturn(departmentToUpdateModel);

        DepartmentModel departmentModelSaved = departmentService.save(departmentToUpdateModel);
        assertNotNull(departmentModelSaved);
        assertEquals(departmentToUpdateModel.getId(),departmentModelSaved.getId());
        assertEquals(departmentToUpdateModel.getName(),departmentModelSaved.getName());
        assertEquals(departmentToUpdateModel.getNumber(),departmentModelSaved.getNumber());
        verify(departmentRepository,times(1)).save(departmentToUpdateModel);
    }

    @Test
    void findDepartmentByIdSuccess(){
        DepartmentModel departmentModel = dummyDepartment;
        departmentModel.setId(1L);
        when(departmentRepository.findById(departmentModel.getId())).thenReturn(Optional.of(departmentModel));

        DepartmentModel returnedDepartmentModel = departmentService.findById(departmentModel.getId());

        assertNotNull(returnedDepartmentModel);
        assertEquals(departmentModel.getId(),returnedDepartmentModel.getId());
        verify(departmentRepository,times(1)).findById(departmentModel.getId());
    }

    @Test
    void findAddressByIdThrowEntityNotFoundException(){
        DepartmentModel departmentModel = dummyDepartment;
        departmentModel.setId(1L);
        when(departmentRepository.findById(departmentModel.getId())).thenReturn(Optional.empty());

        Throwable exception = assertThrows(
                EntityNotFoundException.class, () -> departmentService.findById(departmentModel.getId())
        );

        assertEquals(ResponseEnum.DEPARTMENT_NOT_FOUND_BY_ID.getMessage()
                .concat(departmentModel.getId().toString()),exception.getMessage());
        verify(departmentRepository,times(1)).findById(departmentModel.getId());
    }

    @Test
    void deleteAddressSuccess(){
        DepartmentModel departmentModel = dummyDepartment;
        departmentRepository.delete(departmentModel);
        verify(departmentRepository, times(1)).delete(departmentModel);
    }

    @Test
    void getGreenDepartmentBudgetStatus(){
        Double sumAllProjectsCostAvailableByDate = 6000D;
        Double sumAllBudgetsAvailableByDate = 8000D;

        DepartmentBudgetStatusResponseDTO departmentBudgetStatusResponseDTO =
                prepareAndCallGetBudgetStatusByDate(sumAllProjectsCostAvailableByDate,sumAllBudgetsAvailableByDate);

        assertEquals(StatusBudgetEnum.GREEN,departmentBudgetStatusResponseDTO.getStatus());
        assertEquals(sumAllBudgetsAvailableByDate,departmentBudgetStatusResponseDTO.getBudget());
        assertEquals(sumAllProjectsCostAvailableByDate,departmentBudgetStatusResponseDTO.getProjectsCost());
    }

    @Test
    void getYellowDepartmentBudgetStatus(){
        Double sumAllProjectsCostAvailableByDate = 8700D;
        Double sumAllBudgetsAvailableByDate = 8000D;

        DepartmentBudgetStatusResponseDTO departmentBudgetStatusResponseDTO =
                prepareAndCallGetBudgetStatusByDate(sumAllProjectsCostAvailableByDate,sumAllBudgetsAvailableByDate);

        assertEquals(StatusBudgetEnum.YELLOW,departmentBudgetStatusResponseDTO.getStatus());
        assertEquals(sumAllBudgetsAvailableByDate,departmentBudgetStatusResponseDTO.getBudget());
        assertEquals(sumAllProjectsCostAvailableByDate,departmentBudgetStatusResponseDTO.getProjectsCost());
    }

    @Test
    void getRedDepartmentBudgetStatus(){
        Double sumAllProjectsCostAvailableByDate = 8900D;
        Double sumAllBudgetsAvailableByDate = 8000D;

        DepartmentBudgetStatusResponseDTO departmentBudgetStatusResponseDTO =
                prepareAndCallGetBudgetStatusByDate(sumAllProjectsCostAvailableByDate,sumAllBudgetsAvailableByDate);

        assertEquals(StatusBudgetEnum.RED,departmentBudgetStatusResponseDTO.getStatus());
        assertEquals(sumAllBudgetsAvailableByDate,departmentBudgetStatusResponseDTO.getBudget());
        assertEquals(sumAllProjectsCostAvailableByDate,departmentBudgetStatusResponseDTO.getProjectsCost());
    }

    private DepartmentBudgetStatusResponseDTO prepareAndCallGetBudgetStatusByDate
            (Double sumAllProjectsCostAvailableByDate,
             Double sumAllBudgetsAvailableByDate){

        DepartmentModel departmentModel = dummyDepartment;
        departmentModel.setId(1L);
        LocalDate searchDate = LocalDate.now();


        when(projectRepository.sumAllProjectsCostAvailableByDate(searchDate,departmentModel.getId()))
                .thenReturn(sumAllProjectsCostAvailableByDate);
        when(budgetRepository.sumAllBudgetsAvailableByDate(searchDate,departmentModel.getId()))
                .thenReturn(sumAllBudgetsAvailableByDate);

        return departmentService
                .getBudgetStatusByDate(searchDate,departmentModel);
    }
}
