package com.libraryapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Full membership-type view, nested inside MemberDTO.
// loanDays and maxActiveBorrows are the two rules the borrowing service reads.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberTypeDTO {

    private Long id;

    private String name;

    private Integer loanDays;

    // null means "no cap"
    private Integer maxActiveBorrows;
}
