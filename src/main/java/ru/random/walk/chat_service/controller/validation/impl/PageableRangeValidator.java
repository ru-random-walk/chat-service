package ru.random.walk.chat_service.controller.validation.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.data.domain.Pageable;
import ru.random.walk.chat_service.controller.validation.PageableConstraint;

public class PageableRangeValidator implements ConstraintValidator<PageableConstraint, Pageable> {
    private PageableConstraint pageableConstraint;

    @Override
    public void initialize(PageableConstraint constraintAnnotation) {
        this.pageableConstraint = constraintAnnotation;
    }

    @Override
    public boolean isValid(Pageable pageable, ConstraintValidatorContext context) {
        return pageable.getPageSize() >= 0 && pageable.getPageSize() <= pageableConstraint.maxPageSize();
    }
}
