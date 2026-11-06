package com.restauranttablereservationapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RestaurantTableReservationApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestaurantTableReservationApiApplication.class, args);
    }

}
