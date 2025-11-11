package org.hotelreservation.clustereddatawarehouse.validation.validator;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.hotelreservation.clustereddatawarehouse.model.dto.FxDealRequest;
import org.hotelreservation.clustereddatawarehouse.validation.annotation.DifferentCurrencies;

public class DifferentCurrenciesValidator implements ConstraintValidator<DifferentCurrencies, FxDealRequest> {

    @Override
    public boolean isValid(FxDealRequest request, ConstraintValidatorContext context) {
        if (request == null) return true;

        String from = request.fromCurrencyIsoCode();
        String to = request.toCurrencyIsoCode();

        if (from == null || to == null) return true;

        return !from.equalsIgnoreCase(to);
    }
}
