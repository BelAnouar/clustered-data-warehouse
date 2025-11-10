package org.hotelreservation.clustereddatawarehouse;

import org.springframework.boot.SpringApplication;

public class TestClusteredDataWarehouseApplication {

    public static void main(String[] args) {
        SpringApplication.from(ClusteredDataWarehouseApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
