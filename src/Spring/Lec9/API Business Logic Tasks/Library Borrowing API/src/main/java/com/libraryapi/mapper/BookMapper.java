package com.libraryapi.mapper;

import com.libraryapi.dto.BookDTO;
import com.libraryapi.dto.BookRequestDTO;
import com.libraryapi.dto.BookSimpleDTO;
import com.libraryapi.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookDTO convertFromBookToBookDto(Book book);

    BookSimpleDTO convertFromBookToBookSimpleDto(Book book);

    List<BookDTO> convertFromBookListToBookDtoList(List<Book> books);

    // availableCopies starts equal to totalCopies (@PrePersist), so it is not
    // copied from the request.
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "availableCopies", ignore = true)
    Book convertFromBookRequestDtoToBook(BookRequestDTO requestDTO);

    // availableCopies is recalculated by the service so the borrowed copies
    // stay out of the shelf after the update.
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "availableCopies", ignore = true)
    void updateBookFromBookRequestDto(BookRequestDTO requestDTO, @MappingTarget Book book);
}
