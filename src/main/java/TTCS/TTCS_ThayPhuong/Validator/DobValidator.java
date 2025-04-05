package TTCS.TTCS_ThayPhuong.Validator;

import TTCS.TTCS_ThayPhuong.Validator.Anotation.DobAnotation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = {DobAnotation.class})
public @interface DobValidator {
    String message() default "Must be over {min} years old";

    int min();


    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}