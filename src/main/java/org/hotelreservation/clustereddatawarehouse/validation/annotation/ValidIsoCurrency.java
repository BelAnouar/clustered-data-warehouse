package org.hotelreservation.clustereddatawarehouse.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.hotelreservation.clustereddatawarehouse.validation.validator.IsoCurrencyValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import  java.lang.annotation.RetentionPolicy;

@Documented
@Constraint(validatedBy = IsoCurrencyValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidIsoCurrency {

    String message() default "Invalid ISO currency code";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}