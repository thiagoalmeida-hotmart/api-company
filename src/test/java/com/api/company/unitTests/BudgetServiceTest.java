package com.api.company.unitTests;

import com.api.company.enums.ResponseEnum;
import com.api.company.models.BudgetModel;
import com.api.company.models.DepartmentModel;
import com.api.company.repositories.BudgetRepository;
import com.api.company.services.BudgetService;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BudgetServiceTest {
    @Mock
    private BudgetRepository budgetRepository;
    @InjectMocks
    private BudgetService budgetService;

    private BudgetModel dummyBudget;

    @BeforeEach
    void initBudget() {
        DepartmentModel departmentModel = new DepartmentModel();
        departmentModel.setId(2L);
        departmentModel.setNumber(12L);
        departmentModel.setName("HR Department");
        dummyBudget = new BudgetModel();
        dummyBudget.setDescription("Q1 Budget");
        dummyBudget.setValue(634475D);
        dummyBudget.setStartDate(LocalDate.now());
        dummyBudget.setEndDate(LocalDate.now().plus(10L, ChronoUnit.DAYS));
        dummyBudget.setDepartment(departmentModel);
    }

    @Test
    void insertedBudgetHasMandatoryFields(){
        BudgetModel budgetModel = dummyBudget;
        when(budgetRepository.save(any(BudgetModel.class))).then(returnsFirstArg());

        BudgetModel savedBudget = budgetService.save(budgetModel);

        assertEquals(savedBudget.getDepartment(),budgetModel.getDepartment());
        assertEquals(savedBudget.getDescription(),budgetModel.getDescription());
        assertEquals(savedBudget.getId(),budgetModel.getId());
        assertEquals(savedBudget.getValue(),budgetModel.getValue());
        assertEquals(savedBudget.getStartDate(),budgetModel.getStartDate());
        assertEquals(savedBudget.getEndDate(),budgetModel.getEndDate());
        verify(budgetRepository,times(1)).save(budgetModel);
    }

    @Test
    void updateBudget(){
        BudgetModel budgetDBModel = dummyBudget;
        budgetDBModel.setId(1L);
        BudgetModel budgetToUpdateModel = new BudgetModel();
        budgetToUpdateModel.setId(1L);
        budgetToUpdateModel.setDescription("Q2 Budget");
        budgetToUpdateModel.setValue(3222D);

        when(budgetRepository.save(budgetToUpdateModel)).thenReturn(budgetToUpdateModel);

        BudgetModel budgetModelSaved = budgetService.save(budgetToUpdateModel);
        assertNotNull(budgetModelSaved);
        assertEquals(budgetToUpdateModel.getId(),budgetModelSaved.getId());
        assertEquals(budgetToUpdateModel.getDepartment(),budgetModelSaved.getDepartment());
        assertEquals(budgetToUpdateModel.getValue(),budgetModelSaved.getValue());
        verify(budgetRepository,times(1)).save(budgetToUpdateModel);
    }

    @Test
    void findAddressByIdSuccess(){
        BudgetModel budgetModel = dummyBudget;
        budgetModel.setId(1L);
        when(budgetRepository.findById(budgetModel.getId())).thenReturn(Optional.of(budgetModel));

        BudgetModel returnedBudgetModel = budgetService.findById(budgetModel.getId());

        assertNotNull(returnedBudgetModel);
        assertEquals(budgetModel.getId(),returnedBudgetModel.getId());
        verify(budgetRepository,times(1)).findById(budgetModel.getId());
    }

    @Test
    void findAddressByIdThrowEntityNotFoundException(){
        BudgetModel budgetModel = dummyBudget;
        budgetModel.setId(1L);
        when(budgetRepository.findById(budgetModel.getId())).thenReturn(Optional.empty());

        Throwable exception = assertThrows(
                EntityNotFoundException.class, () -> budgetService.findById(budgetModel.getId())
        );

        assertEquals(ResponseEnum.BUDGET_NOT_FOUND_BY_ID.getMessage()
                .concat(budgetModel.getId().toString()),exception.getMessage());
        verify(budgetRepository,times(1)).findById(budgetModel.getId());
    }

    @Test
    void deleteAddressSuccess(){
        BudgetModel budgetModel = dummyBudget;
        budgetRepository.delete(budgetModel);
        verify(budgetRepository, times(1)).delete(budgetModel);
    }

    @Test
    void findAllBudgetsByDepartmentId(){
        Long departmentId = 3L;
        BudgetModel budgetModel = dummyBudget;
        List<BudgetModel> budgetModelList = new ArrayList<>();
        budgetModelList.add(budgetModel);
        when(budgetRepository.findAllByDepartmentId(departmentId)).thenReturn(budgetModelList);

        List<BudgetModel> returnedBudgets = budgetService.findAllBudgetsByDepartmentId(departmentId);

        assertEquals(returnedBudgets, budgetModelList);
        verify(budgetRepository,times(1)).findAllByDepartmentId(departmentId);
    }
}
