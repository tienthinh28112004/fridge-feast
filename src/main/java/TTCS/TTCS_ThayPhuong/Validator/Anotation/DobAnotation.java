package TTCS.TTCS_ThayPhuong.Validator.Anotation;

import TTCS.TTCS_ThayPhuong.Validator.DobValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class DobAnotation implements ConstraintValidator<DobValidator, LocalDate> {
    private int min;
    @Override
    public void initialize(DobValidator constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        min = constraintAnnotation.min();
    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        if (Objects.isNull(localDate)) return true;

        long years = ChronoUnit.YEARS.between(localDate,LocalDate.now());

        return years>=min;
    }
}
