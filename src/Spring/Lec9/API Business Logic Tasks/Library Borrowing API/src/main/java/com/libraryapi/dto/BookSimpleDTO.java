package com.libraryapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Light book view used inside BorrowDTO.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookSimpleDTO {

    private Long id;

    private String title;

    private String isbn;
}
