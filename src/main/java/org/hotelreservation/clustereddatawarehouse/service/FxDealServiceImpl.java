package org.hotelreservation.clustereddatawarehouse.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hotelreservation.clustereddatawarehouse.exception.DuplicateDealException;
import org.hotelreservation.clustereddatawarehouse.mapper.FxDealMapper;
import org.hotelreservation.clustereddatawarehouse.model.dto.FxDealRequest;
import org.hotelreservation.clustereddatawarehouse.model.dto.FxDealResponse;
import org.hotelreservation.clustereddatawarehouse.model.entity.FxDeal;
import org.hotelreservation.clustereddatawarehouse.repository.FxDealRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j

public class FxDealServiceImpl implements FxDealService {
    private final FxDealRepository fxDealRepository;
    private final FxDealMapper dealMapper;
    @Override
    @Transactional
    public FxDealResponse createDeal(FxDealRequest request) {
        log.info("Create new deal : {}", request );

        checkDuplicate(request.dealUniqueId());
        FxDeal deal = dealMapper.toEntity(request);

        try {
            FxDeal savedDeal = fxDealRepository.save(deal);
            log.info("Successfully saved FX deal: {} ", savedDeal.getDealUniqueId());

            return dealMapper.toResponse(savedDeal);

        } catch (DataIntegrityViolationException e) {
            throw new DuplicateDealException(
                    String.format("Deal with ID %s already exists", request.dealUniqueId())
            );
        }
    }

    private void checkDuplicate(final String dealUniqueId) {
        log.info("Checking for duplicate deal: {}", dealUniqueId);

        if (fxDealRepository.existsByDealUniqueId(dealUniqueId)) {
            log.warn("Duplicate deal detected: {}", dealUniqueId);
            throw new DuplicateDealException(
                    String.format("Deal with ID %s already exists", dealUniqueId)
            );
        }
    }
}
