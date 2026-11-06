package com.restauranttablereservationapi;

import com.restauranttablereservationapi.model.Reservation;
import com.restauranttablereservationapi.repo.ReservationRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Black-box tests over HTTP with the H2 test profile: every request goes through
// controller -> service -> repository, so the seven rules are checked end to end.
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ReservationApiIntegrationTest {

    private static final AtomicInteger SEQ = new AtomicInteger(1000);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ReservationRepo reservationRepo;

    // ---------------------------------------------------------------- helpers

    private long createCustomer() throws Exception {
        int n = SEQ.incrementAndGet();
        String body = "{\"name\":\"Customer " + n + "\",\"phone\":\"01000" + n
                + "\",\"email\":\"c" + n + "@example.com\"}";

        MvcResult result = mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andReturn();

        return json(result).get("id").asLong();
    }

    private long createTable(int capacity) throws Exception {
        return createTable(capacity, true);
    }

    private long createTable(int capacity, boolean active) throws Exception {
        int n = SEQ.incrementAndGet();
        String body = "{\"tableNumber\":\"T" + n + "\",\"capacity\":" + capacity
                + ",\"active\":" + active + "}";

        MvcResult result = mockMvc.perform(post("/tables")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andReturn();

        return json(result).get("id").asLong();
    }

    private String reservationBody(long tableId, long customerId, LocalDateTime start,
                                   int durationMinutes, int guests) {
        return "{\"tableId\":" + tableId
                + ",\"customerId\":" + customerId
                + ",\"startTime\":\"" + start + "\""
                + ",\"durationMinutes\":" + durationMinutes
                + ",\"guests\":" + guests + "}";
    }

    private long createReservation(long tableId, long customerId, LocalDateTime start,
                                   int durationMinutes, int guests) throws Exception {
        MvcResult result = mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reservationBody(tableId, customerId, start, durationMinutes, guests)))
                .andExpect(status().isCreated())
                .andReturn();

        return json(result).get("id").asLong();
    }

    private JsonNode json(MvcResult result) throws Exception {
        return objectMapper.readTree(result.getResponse().getContentAsString());
    }

    private String statusName(JsonNode reservation) {
        return reservation.get("status").get("name").asText();
    }

    private boolean isNull(JsonNode node) {
        return node == null || node.isNull();
    }

    // Tomorrow 13:00 is always in the future, inside 12:00-23:00 and at least
    // 30 minutes ahead, so it is a valid base slot for every working-hours test.
    private LocalDateTime tomorrowAt(int hour) {
        return LocalDate.now().plusDays(1).atTime(hour, 0);
    }

    // ------------------------------------------------------------ create rules

    @Test
    void creatingAReservationStoresPendingWithComputedEndAndExpiry() throws Exception {
        long table = createTable(4);
        long customer = createCustomer();
        LocalDateTime start = tomorrowAt(13);

        MvcResult result = mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reservationBody(table, customer, start, 90, 2)))
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode body = json(result);
        assertEquals("PENDING", statusName(body));
        assertEquals(start, LocalDateTime.parse(body.get("startTime").asText()));
        assertEquals(start.plusMinutes(90), LocalDateTime.parse(body.get("endTime").asText()));

        LocalDateTime createdAt = LocalDateTime.parse(body.get("createdAt").asText());
        assertEquals(createdAt.plusMinutes(15), LocalDateTime.parse(body.get("expiresAt").asText()));
        assertTrue(isNull(body.get("confirmedAt")));
        assertTrue(isNull(body.get("cancelledAt")));
    }

    @Test
    void overlappingReservationOnTheSameTableIsRejected() throws Exception {
        long table = createTable(4);
        long customer = createCustomer();
        LocalDateTime start = tomorrowAt(13);

        createReservation(table, customer, start, 90, 2);

        JsonNode body = json(mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reservationBody(table, customer, start.plusMinutes(30), 60, 2)))
                .andExpect(status().isBadRequest())
                .andReturn());

        assertTrue(body.get("message").asText().contains("already reserved"));
    }

    @Test
    void backToBackReservationOnTheSameTableIsAllowed() throws Exception {
        long table = createTable(4);
        long customer = createCustomer();
        LocalDateTime start = tomorrowAt(13);

        createReservation(table, customer, start, 90, 2);
        // starts exactly when the previous one ends: half-open intervals do not overlap
        createReservation(table, customer, start.plusMinutes(90), 60, 2);
    }

    @Test
    void partyLargerThanTableCapacityIsRejected() throws Exception {
        long table = createTable(4);
        long customer = createCustomer();

        JsonNode body = json(mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reservationBody(table, customer, tomorrowAt(13), 60, 6)))
                .andExpect(status().isBadRequest())
                .andReturn());

        assertTrue(body.get("message").asText().contains("seats"));
    }

    @Test
    void startBeforeOpeningTimeIsRejected() throws Exception {
        long table = createTable(4);
        long customer = createCustomer();

        JsonNode body = json(mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reservationBody(table, customer, tomorrowAt(10), 60, 2)))
                .andExpect(status().isBadRequest())
                .andReturn());

        assertTrue(body.get("message").asText().contains("between"));
    }

    @Test
    void reservationCrossingMidnightIsRejected() throws Exception {
        long table = createTable(4);
        long customer = createCustomer();

        // 22:30 + 120 minutes ends at 00:30 the next day, after the 23:00 close
        JsonNode body = json(mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reservationBody(table, customer, tomorrowAt(22).plusMinutes(30), 120, 2)))
                .andExpect(status().isBadRequest())
                .andReturn());

        assertTrue(body.get("message").asText().contains("between"));
    }

    @Test
    void thirdActiveReservationForTheSameCustomerIsRejected() throws Exception {
        long customer = createCustomer();
        long tableOne = createTable(4);
        long tableTwo = createTable(4);
        long tableThree = createTable(4);

        createReservation(tableOne, customer, tomorrowAt(13), 60, 2);
        createReservation(tableTwo, customer, tomorrowAt(16), 60, 2);

        JsonNode body = json(mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reservationBody(tableThree, customer, tomorrowAt(19), 60, 2)))
                .andExpect(status().isBadRequest())
                .andReturn());

        assertTrue(body.get("message").asText().contains("active reservations"));
    }

    @Test
    void reservationLessThanThirtyMinutesAheadIsRejected() throws Exception {
        // Yesterday 13:00 is always inside working hours and always in the past,
        // so the 30-minute advance rule is exercised without any clock assumption.
        LocalDateTime start = LocalDate.now().minusDays(1).atTime(13, 0);

        long table = createTable(4);
        long customer = createCustomer();

        JsonNode body = json(mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reservationBody(table, customer, start, 60, 2)))
                .andExpect(status().isBadRequest())
                .andReturn());

        assertTrue(body.get("message").asText().contains("in advance"));
    }

    @Test
    void inactiveTableCannotBeReserved() throws Exception {
        long table = createTable(4, false);
        long customer = createCustomer();

        JsonNode body = json(mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reservationBody(table, customer, tomorrowAt(13), 60, 2)))
                .andExpect(status().isBadRequest())
                .andReturn());

        assertTrue(body.get("message").asText().contains("out of service"));
    }

    // ----------------------------------------------------------- confirm rules

    @Test
    void confirmingPendingReservationStampsConfirmedAt() throws Exception {
        long id = createReservation(createTable(4), createCustomer(), tomorrowAt(13), 60, 2);

        JsonNode body = json(mockMvc.perform(put("/reservations/{id}/confirm", id))
                .andExpect(status().isOk())
                .andReturn());

        assertEquals("CONFIRMED", statusName(body));
        assertFalse(isNull(body.get("confirmedAt")));
    }

    @Test
    void confirmingAnAlreadyConfirmedReservationIsRejected() throws Exception {
        long id = createReservation(createTable(4), createCustomer(), tomorrowAt(13), 60, 2);

        mockMvc.perform(put("/reservations/{id}/confirm", id)).andExpect(status().isOk());

        JsonNode body = json(mockMvc.perform(put("/reservations/{id}/confirm", id))
                .andExpect(status().isBadRequest())
                .andReturn());

        assertTrue(body.get("message").asText().contains("cannot be confirmed"));
    }

    @Test
    void confirmingAnExpiredReservationIsRejected() throws Exception {
        long id = createReservation(createTable(4), createCustomer(), tomorrowAt(13), 60, 2);

        // simulate the 15-minute window closing after the row was saved
        Reservation reservation = reservationRepo.findById(id).orElseThrow();
        reservation.setExpiresAt(LocalDateTime.now().minusMinutes(1));
        reservationRepo.save(reservation);

        JsonNode body = json(mockMvc.perform(put("/reservations/{id}/confirm", id))
                .andExpect(status().isBadRequest())
                .andReturn());

        assertTrue(body.get("message").asText().contains("EXPIRED"));
    }

    // ------------------------------------------------------------ cancel rules

    @Test
    void cancellingPendingReservationStampsCancelledAt() throws Exception {
        long id = createReservation(createTable(4), createCustomer(), tomorrowAt(13), 60, 2);

        JsonNode body = json(mockMvc.perform(put("/reservations/{id}/cancel", id))
                .andExpect(status().isOk())
                .andReturn());

        assertEquals("CANCELLED", statusName(body));
        assertFalse(isNull(body.get("cancelledAt")), "cancelledAt must be stamped on cancellation");
    }

    @Test
    void cancellingPendingReservationDoesNotStampConfirmedAt() throws Exception {
        long id = createReservation(createTable(4), createCustomer(), tomorrowAt(13), 60, 2);

        JsonNode body = json(mockMvc.perform(put("/reservations/{id}/cancel", id))
                .andExpect(status().isOk())
                .andReturn());

        assertTrue(isNull(body.get("confirmedAt")),
                "a cancelled reservation must not look confirmed");
    }

    @Test
    void cancellingConfirmedReservationFarEnoughAheadIsAllowed() throws Exception {
        long id = createReservation(createTable(4), createCustomer(), tomorrowAt(13), 60, 2);
        mockMvc.perform(put("/reservations/{id}/confirm", id)).andExpect(status().isOk());

        // rule 6 only forbids cancellation inside the last hour; tomorrow 13:00 is
        // far more than one hour away
        JsonNode body = json(mockMvc.perform(put("/reservations/{id}/cancel", id))
                .andExpect(status().isOk())
                .andReturn());

        assertEquals("CANCELLED", statusName(body));
    }

    @Test
    void cancellingInsideTheLastHourIsRejected() throws Exception {
        // Create a normal slot first, then move it to 40 minutes from now directly
        // in the repository. That keeps the test independent of the wall clock
        // (creating "now + 40" can fall outside the working hours).
        long id = createReservation(createTable(4), createCustomer(), tomorrowAt(13), 60, 2);

        Reservation reservation = reservationRepo.findById(id).orElseThrow();
        LocalDateTime soon = LocalDateTime.now().plusMinutes(40).withSecond(0).withNano(0);
        reservation.setStartTime(soon);
        reservation.setEndTime(soon.plusMinutes(60));
        reservationRepo.save(reservation);

        JsonNode body = json(mockMvc.perform(put("/reservations/{id}/cancel", id))
                .andExpect(status().isBadRequest())
                .andReturn());

        assertTrue(body.get("message").asText().contains("minutes of its start time"));
    }

    @Test
    void cancellingAnExpiredReservationIsRejected() throws Exception {
        long id = createReservation(createTable(4), createCustomer(), tomorrowAt(13), 60, 2);

        // the 15-minute confirmation window already closed
        Reservation reservation = reservationRepo.findById(id).orElseThrow();
        reservation.setExpiresAt(LocalDateTime.now().minusMinutes(1));
        reservationRepo.save(reservation);

        JsonNode body = json(mockMvc.perform(put("/reservations/{id}/cancel", id))
                .andExpect(status().isBadRequest())
                .andReturn());

        assertTrue(body.get("message").asText().contains("EXPIRED"));
    }

    @Test
    void cancellingTwiceIsRejected() throws Exception {
        long id = createReservation(createTable(4), createCustomer(), tomorrowAt(13), 60, 2);

        mockMvc.perform(put("/reservations/{id}/cancel", id)).andExpect(status().isOk());

        JsonNode body = json(mockMvc.perform(put("/reservations/{id}/cancel", id))
                .andExpect(status().isBadRequest())
                .andReturn());

        assertTrue(body.get("message").asText().contains("CANCELLED"));
    }

    // ------------------------------------------------------------- read rules

    @Test
    void getByIdReturnsTheReservationAndMissingIdReturns404() throws Exception {
        long id = createReservation(createTable(4), createCustomer(), tomorrowAt(13), 60, 2);

        JsonNode body = json(mockMvc.perform(get("/reservations/{id}", id))
                .andExpect(status().isOk())
                .andReturn());

        assertEquals(id, body.get("id").asLong());

        JsonNode missing = json(mockMvc.perform(get("/reservations/{id}", 999_999))
                .andExpect(status().isNotFound())
                .andReturn());

        assertTrue(missing.get("message").asText().contains("not found"));
    }

    @Test
    void unknownPathReturns404() throws Exception {
        JsonNode body = json(mockMvc.perform(get("/does-not-exist"))
                .andExpect(status().isNotFound())
                .andReturn());

        assertTrue(body.get("message").asText().contains("not found"));
    }

    @Test
    void customerHistoryReturnsOnlyThatCustomersReservations() throws Exception {
        long firstCustomer = createCustomer();
        long secondCustomer = createCustomer();
        createReservation(createTable(4), firstCustomer, tomorrowAt(13), 60, 2);
        createReservation(createTable(4), secondCustomer, tomorrowAt(13), 60, 2);

        JsonNode body = json(mockMvc.perform(get("/reservations/customer/{id}", firstCustomer))
                .andExpect(status().isOk())
                .andReturn());

        assertEquals(1, body.size());
        assertEquals(firstCustomer, body.get(0).get("customer").get("id").asLong());
    }

    @Test
    void customerWithoutReservationsReturns404() throws Exception {
        long customer = createCustomer();

        JsonNode body = json(mockMvc.perform(get("/reservations/customer/{id}", customer))
                .andExpect(status().isNotFound())
                .andReturn());

        assertTrue(body.get("message").asText().contains("has no reservations"));
    }

    @Test
    void tableHistoryReturnsItsReservations() throws Exception {
        long table = createTable(4);
        createReservation(table, createCustomer(), tomorrowAt(13), 60, 2);

        JsonNode body = json(mockMvc.perform(get("/reservations/table/{id}", table))
                .andExpect(status().isOk())
                .andReturn());

        assertEquals(1, body.size());
        assertEquals(table, body.get(0).get("table").get("id").asLong());
    }

    @Test
    void tableWithoutReservationsReturns404() throws Exception {
        long table = createTable(4);

        JsonNode body = json(mockMvc.perform(get("/reservations/table/{id}", table))
                .andExpect(status().isNotFound())
                .andReturn());

        assertTrue(body.get("message").asText().contains("has no reservations"));
    }

    @Test
    void unconfirmedReservationIsReportedAsExpiredAfterItsWindow() throws Exception {
        long id = createReservation(createTable(4), createCustomer(), tomorrowAt(13), 60, 2);

        // the row was never confirmed and its 15-minute window has passed
        Reservation reservation = reservationRepo.findById(id).orElseThrow();
        reservation.setExpiresAt(LocalDateTime.now().minusMinutes(1));
        reservationRepo.save(reservation);

        // the read itself applies rule 7, so the client sees the real state
        JsonNode body = json(mockMvc.perform(get("/reservations/{id}", id))
                .andExpect(status().isOk())
                .andReturn());

        assertEquals("EXPIRED", statusName(body));

        // and the new state is stored, not just rendered
        Reservation stored = reservationRepo.findAllByOrderByStartTimeAsc().stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .orElseThrow();
        assertEquals("EXPIRED", stored.getStatus().getName());
    }

    @Test
    void getAllReservationsReturnsCreatedRows() throws Exception {
        createReservation(createTable(4), createCustomer(), tomorrowAt(13), 60, 2);

        JsonNode body = json(mockMvc.perform(get("/reservations"))
                .andExpect(status().isOk())
                .andReturn());

        assertTrue(body.isArray());
        assertTrue(body.size() >= 1);
    }
}
