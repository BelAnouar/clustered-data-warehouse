package org.hotelreservation.clustereddatawarehouse.mapper;


import org.hotelreservation.clustereddatawarehouse.model.dto.FxDealRequest;
import org.hotelreservation.clustereddatawarehouse.model.dto.FxDealResponse;
import org.hotelreservation.clustereddatawarehouse.model.entity.FxDeal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface FxDealMapper {

    @Mapping(target = "dealUniqueId", source = "dealUniqueId")
    @Mapping(target = "fromCurrencyIsoCode", source = "fromCurrencyIsoCode", qualifiedByName = "toUpperCase")
    @Mapping(target = "toCurrencyIsoCode", source = "toCurrencyIsoCode", qualifiedByName = "toUpperCase")
    @Mapping(target = "createdAt", ignore = true)
    FxDeal toEntity(FxDealRequest request);


    FxDealResponse toResponse(FxDeal deal);

    @Named("toUpperCase")
    default String toUpperCase(String value) {
        return value != null ? value.toUpperCase() : null;
    }
}
