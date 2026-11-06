package com.libraryapi.repo;

import com.libraryapi.model.BorrowStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowStatusRepo extends JpaRepository<BorrowStatus, Long> {

    Optional<BorrowStatus> findByName(String name);

    boolean existsByName(String name);

    // used on update so the row itself does not count as a duplicate
    boolean existsByNameAndIdNot(String name, Long id);

    List<BorrowStatus> findAllByOrderByIdAsc();
}
