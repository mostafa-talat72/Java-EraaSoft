package com.libraryapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Full member view; the nested type carries the loan period and the book cap.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {

    private Long id;

    private String name;

    private String email;

    private MemberTypeDTO type;
}
