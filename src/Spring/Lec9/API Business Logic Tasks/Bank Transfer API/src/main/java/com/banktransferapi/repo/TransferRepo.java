package com.banktransferapi.repo;

import com.banktransferapi.model.Transfer;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransferRepo extends JpaRepository<Transfer, Long> {

    /**
     * Total amount already transferred today by the sender, used to enforce the daily limit.
     * coalesce keeps the result 0 instead of null when the sender has no transfers in the period.
     */
    @Query("""
            select coalesce(sum(t.amount), 0)
            from Transfer t
            where t.sender.id = :senderId
              and t.createdAt >= :start
              and t.createdAt < :end
            """)
    BigDecimal sumAmountBySenderAndPeriod(@Param("senderId") Long senderId,
                                          @Param("start") LocalDateTime start,
                                          @Param("end") LocalDateTime end);

    boolean existsBySenderIdOrReceiverId(Long senderId, Long receiverId);

    @EntityGraph(attributePaths = {"sender", "receiver"})
    List<Transfer> findAllByOrderByCreatedAtDesc();

    @EntityGraph(attributePaths = {"sender", "receiver"})
    List<Transfer> findBySenderIdOrderByCreatedAtDesc(Long senderId);

    @EntityGraph(attributePaths = {"sender", "receiver"})
    List<Transfer> findByReceiverIdOrderByCreatedAtDesc(Long receiverId);
}
