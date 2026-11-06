package com.libraryapi.repo;

import com.libraryapi.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepo extends JpaRepository<Member, Long> {

    boolean existsByEmail(String email);

    // used on update so the member itself does not count as a duplicate
    boolean existsByEmailAndIdNot(String email, Long id);

    // used to refuse deleting a member type that is still assigned to members
    boolean existsByTypeId(Long typeId);

    List<Member> findAllByOrderByIdAsc();
}
