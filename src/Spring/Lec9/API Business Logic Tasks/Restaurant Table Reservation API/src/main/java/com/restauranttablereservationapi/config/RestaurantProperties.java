package com.restauranttablereservationapi.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
@ConfigurationProperties(prefix = "restaurant")
@Getter
@Setter

public class RestaurantProperties {

    // "12:00" is converted to LocalTime by Spring Boot's built-in converter.
    private LocalTime openTime;

    // "23:00"
    private LocalTime closeTime;

    // rule 5: booking must be at least this many minutes ahead.
    private int minAdvanceMinutes;

    // rule 6: no cancellation inside this window before startTime.
    private int cancelCutoffMinutes;

    // rule 7: PENDING expires this many minutes after createdAt.
    private int confirmWindowMinutes;

    // rule 4: how many PENDING/CONFIRMED rows a customer may hold.
    private int maxActiveReservations;

}