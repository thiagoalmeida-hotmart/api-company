package com.api.company.services;

import com.api.company.enums.ResponseEnum;
import com.api.company.models.BudgetModel;
import com.api.company.repositories.BudgetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final DepartmentService departmentService;

    public BudgetService(BudgetRepository budgetRepository, DepartmentService departmentService) {
        this.budgetRepository = budgetRepository;
        this.departmentService = departmentService;
    }

    @Transactional
    public BudgetModel save(BudgetModel budgetModel){
        return budgetRepository.save(budgetModel);
    }

    public List<BudgetModel> findAll(){
        return budgetRepository.findAll();
    }

    public BudgetModel findById(Long id){
        Optional<BudgetModel> budgetModelOptional = budgetRepository.findById(id);
        if(budgetModelOptional.isPresent() == Boolean.FALSE) {
            throw new EntityNotFoundException(ResponseEnum.BUDGET_NOT_FOUND_BY_ID
                    .getMessage().concat(String.valueOf(id)));
        }
        return budgetModelOptional.get();
    }
    @Transactional
    public void delete(BudgetModel budgetModel){
        budgetRepository.delete(budgetModel);
    }

    public void validateModel (BudgetModel budgetModel){
        departmentService.findById(budgetModel.getDepartment().getId());
    }

    public List<BudgetModel> findAllBudgetsByDepartmentId(Long idDepartment){
        return budgetRepository.findAllByDepartmentId(idDepartment);
    }
}
