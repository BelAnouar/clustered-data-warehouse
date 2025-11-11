package org.hotelreservation.clustereddatawarehouse.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.hotelreservation.clustereddatawarehouse.validation.validator.DifferentCurrenciesValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import  java.lang.annotation.RetentionPolicy;

@Documented
@Constraint(validatedBy = DifferentCurrenciesValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface DifferentCurrencies {

    String message() default "From and To currencies must be different";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
