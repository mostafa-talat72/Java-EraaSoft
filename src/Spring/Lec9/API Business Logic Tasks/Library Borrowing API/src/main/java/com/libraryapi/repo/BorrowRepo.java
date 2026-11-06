package com.libraryapi.repo;

import com.libraryapi.model.Borrow;
import com.libraryapi.model.BorrowStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowRepo extends JpaRepository<Borrow, Long> {

    // Referential guards used by BookServiceImpl and MemberServiceImpl
    // before deleting a row that still appears in LB_BORROW.
    boolean existsByBookId(Long bookId);

    boolean existsByMemberId(Long memberId);

    // used to refuse deleting a borrow status that is still assigned to borrows
    boolean existsByStatusId(Long statusId);

    // Rule 2: a student's cap counts only copies that are still out.
    long countByMemberIdAndReturnDateIsNull(Long memberId);


    // Rule 4: does the member still hold a borrow that is already overdue?
    boolean existsByMemberIdAndStatus(Long memberId, BorrowStatus status);

    // Open rows past their due date. Used to refresh OVERDUE state
    // and by GET /borrows/overdue (oldest due date first).
    @EntityGraph(attributePaths = {"book", "member", "status"})
    List<Borrow> findByReturnDateIsNullAndDueDateBefore(LocalDate date);


    @EntityGraph(attributePaths = {"book", "member", "status"})
    List<Borrow> findByStatus(BorrowStatus status);

    // List endpoints, newest first. The entity graph fetches the nested
    // rows in the same query, so the DTO mapping does not trigger N+1.
    @EntityGraph(attributePaths = {"book", "member", "status"})
    List<Borrow> findAllByOrderByIdDesc();


    @EntityGraph(attributePaths = {"book", "member", "status"})
    List<Borrow> findByMemberIdOrderByIdDesc(Long memberId);

    @EntityGraph(attributePaths = {"book", "member", "status"})
    List<Borrow> findByBookIdOrderByIdDesc(Long bookId);

    // Plain reads that expose the status (e.g. assertions after a request)
    // get the status eagerly too, so it is safe to read outside a transaction.
    @EntityGraph(attributePaths = {"book", "member", "status"})
    Optional<Borrow> findById(Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select b from Borrow b where b.id = :id")
    Optional<Borrow> findByIdForUpdate(@Param("id") Long id);

}
