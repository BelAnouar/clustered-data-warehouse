package org.hotelreservation.clustereddatawarehouse.repository;


import org.hotelreservation.clustereddatawarehouse.model.entity.FxDeal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FxDealRepository extends JpaRepository<FxDeal, String> {
    boolean existsByDealUniqueId(String s);
}
