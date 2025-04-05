package TTCS.TTCS_ThayPhuong.Validator;

import TTCS.TTCS_ThayPhuong.Validator.Anotation.MinSizeAnotation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = {MinSizeAnotation.class})
public @interface MinSizeValidator {
    String message() default "must enter more than {min} characters";

    int min();


    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

