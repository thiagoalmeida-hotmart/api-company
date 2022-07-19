package com.api.company.services;

import com.api.company.dtos.departmentDTOs.DepartmentBudgetStatusResponseDTO;
import com.api.company.enums.ResponseEnum;
import com.api.company.enums.StatusBudgetEnum;
import com.api.company.models.DepartmentModel;
import com.api.company.repositories.DepartmentRepository;
import com.api.company.repositories.BudgetRepository;
import com.api.company.repositories.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final ProjectRepository projectRepository;
    private final BudgetRepository budgetRepository;

    public DepartmentService(DepartmentRepository departmentRepository,
                             ProjectRepository projectRepository,
                             BudgetRepository budgetRepository) {
        this.departmentRepository = departmentRepository;
        this.projectRepository = projectRepository;
        this.budgetRepository = budgetRepository;
    }

    @Transactional
    public DepartmentModel save(DepartmentModel departmentModel){
        return departmentRepository.save(departmentModel);
    }

    public List<DepartmentModel> findAll(){
        return departmentRepository.findAll();
    }

    public DepartmentModel findById(Long id){
        Optional<DepartmentModel> departmentModelOptional = departmentRepository.findById(id);
        if(departmentModelOptional.isPresent() == Boolean.FALSE) {
            throw new EntityNotFoundException(ResponseEnum.DEPARTMENT_NOT_FOUND_BY_ID
                    .getMessage().concat(String.valueOf(id)));
        }
        return departmentModelOptional.get();
    }
    @Transactional
    public void delete(DepartmentModel departmentModel){
        departmentRepository.delete(departmentModel);
    }

    public DepartmentBudgetStatusResponseDTO getBudgetStatusByDate(LocalDate searchDate,
                                                                   DepartmentModel departmentModel){
        Double sumAllProjectsCostAvailableByDate = projectRepository
                .sumAllProjectsCostAvailableByDate(searchDate, departmentModel.getId());
        Double sumAllBudgetsAvailableByDate = budgetRepository
                .sumAllBudgetsAvailableByDate(searchDate, departmentModel.getId());

        StatusBudgetEnum statusBudgetEnum = getDepartmentBudgetStatus(sumAllProjectsCostAvailableByDate,
                                                                            sumAllBudgetsAvailableByDate);
        return new DepartmentBudgetStatusResponseDTO(statusBudgetEnum,
                sumAllProjectsCostAvailableByDate,sumAllBudgetsAvailableByDate);
    }

    private StatusBudgetEnum getDepartmentBudgetStatus(Double sumOfProjectsCost, Double sumOfBudgets){
        Double sumOfBudgetsPlusTenPercent = sumOfBudgets + calculatePercentage(sumOfBudgets,10D);
        if(sumOfProjectsCost <= sumOfBudgets){
            return StatusBudgetEnum.GREEN;
        }else if(sumOfProjectsCost <= sumOfBudgetsPlusTenPercent){
            return StatusBudgetEnum.YELLOW;
        }else{
            return StatusBudgetEnum.RED;
        }
    }

    private double calculatePercentage(double percent, double value) {
        return value * percent / 100;
    }

}
