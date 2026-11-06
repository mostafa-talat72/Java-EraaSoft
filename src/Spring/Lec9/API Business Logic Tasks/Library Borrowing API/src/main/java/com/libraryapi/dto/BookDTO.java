package com.libraryapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Full book view: totalCopies is the shelf size, availableCopies what is free now.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {

    private Long id;

    private String title;

    private String author;

    private String isbn;

    private Integer totalCopies;

    private Integer availableCopies;
}
