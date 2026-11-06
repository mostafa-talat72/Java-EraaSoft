package com.libraryapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Light member view used inside BorrowDTO.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberSimpleDTO {

    private Long id;

    private String name;

    private MemberTypeDTO type;
}
