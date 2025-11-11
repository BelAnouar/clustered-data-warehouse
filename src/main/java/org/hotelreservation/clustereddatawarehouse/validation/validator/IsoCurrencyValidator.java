package org.hotelreservation.clustereddatawarehouse.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.hotelreservation.clustereddatawarehouse.validation.annotation.ValidIsoCurrency;

import java.util.Currency;

public class IsoCurrencyValidator implements ConstraintValidator<ValidIsoCurrency, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) return true;
        try {
            Currency.getInstance(value.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
