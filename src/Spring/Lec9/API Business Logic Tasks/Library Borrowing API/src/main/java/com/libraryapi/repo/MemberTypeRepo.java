package com.libraryapi.repo;

import com.libraryapi.model.MemberType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberTypeRepo extends JpaRepository<MemberType, Long> {

    Optional<MemberType> findByName(String name);

    boolean existsByName(String name);

    // used on update so the row itself does not count as a duplicate
    boolean existsByNameAndIdNot(String name, Long id);

    List<MemberType> findAllByOrderByIdAsc();
}
