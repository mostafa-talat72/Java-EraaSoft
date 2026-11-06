package com.libraryapi.service;

import com.libraryapi.dto.BookDTO;
import com.libraryapi.dto.BookRequestDTO;
import com.libraryapi.model.Book;
import jakarta.validation.Valid;

import java.util.List;

public interface BookService {

    // @Valid on the contract, not on the implementation: Hibernate Validator
    // forbids an override from redefining the parameter constraints (HV000151).
    BookDTO createBook(@Valid BookRequestDTO requestDTO);

    BookDTO updateBook(Long id, @Valid BookRequestDTO requestDTO);

    List<BookDTO> getAllBooks();

    Book getMemberEntityById(Long id);

    BookDTO getBookById(Long id);

    void deleteBook(Long id);
}
