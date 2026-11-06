package com.libraryapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Full borrow-status view, nested inside BorrowDTO.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BorrowStatusDTO {

    private Long id;

    private String name;
}
