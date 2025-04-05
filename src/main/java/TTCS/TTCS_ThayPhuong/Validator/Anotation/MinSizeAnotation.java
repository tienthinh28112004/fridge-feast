package TTCS.TTCS_ThayPhuong.Validator.Anotation;

import TTCS.TTCS_ThayPhuong.Validator.MinSizeValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MinSizeAnotation implements ConstraintValidator<MinSizeValidator, String> {
    private int min;
    @Override
    public void initialize(MinSizeValidator constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        min=constraintAnnotation.min();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s.length()>=min;
    }
}
