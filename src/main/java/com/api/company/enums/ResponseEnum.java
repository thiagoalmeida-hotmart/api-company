package com.api.company.enums;

import lombok.Getter;

@Getter
public enum ResponseEnum {

    DEPARTMENT_NOT_FOUND_BY_ID("None department was found with id "),
    ADDRESS_NOT_FOUND_BY_ID("None address was found with id "),
    EMPLOYEE_NOT_FOUND_BY_ID("None employee was found with id "),
    EMPLOYEE_PROJECT_NOT_FOUND_BY_ID("None employee_project was found with id "),
    BUDGET_NOT_FOUND_BY_ID("None budget was found with id "),
    PROJECT_NOT_FOUND_BY_ID("None project was found with id ");
    private String message;

    ResponseEnum(String message){
        this.message = message;
    }
}
