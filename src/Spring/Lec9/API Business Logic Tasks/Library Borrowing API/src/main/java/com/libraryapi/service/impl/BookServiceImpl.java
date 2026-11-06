package com.libraryapi.service.impl;

import com.libraryapi.dto.BookDTO;
import com.libraryapi.dto.BookRequestDTO;
import com.libraryapi.exception.BadRequestException;
import com.libraryapi.exception.NotFoundException;
import com.libraryapi.helper.BorrowHelper;
import com.libraryapi.mapper.BookMapper;
import com.libraryapi.model.Book;
import com.libraryapi.repo.BookRepo;
import com.libraryapi.repo.BorrowRepo;
import com.libraryapi.service.BookService;
import com.libraryapi.service.BorrowService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepo bookRepo;

    private final BookMapper bookMapper;

    private final BorrowHelper borrowHelper;

    @Transactional(readOnly = true)
    @Override
    public List<BookDTO> getAllBooks() {
        List<Book> books = bookRepo.findAllByOrderByIdAsc();

        // Same convention as the previous task: an empty list is reported as 404.
        if (books.isEmpty()) {
            throw new NotFoundException("No books found in the system");
        }

        return bookMapper.convertFromBookListToBookDtoList(books);
    }

    @Override
    public Book getMemberEntityById(Long id) {
        return findBookOrThrow(id);
    }

    @Transactional(readOnly = true)
    @Override
    public BookDTO getBookById(Long id) {
        return bookMapper.convertFromBookToBookDto(findBookOrThrow(id));
    }

    @Override
    public BookDTO createBook(BookRequestDTO requestDTO) {
        // Pre-check the unique business key so a duplicate becomes a clear 400
        // instead of a raw DB unique-constraint 500.
        if (bookRepo.existsByIsbn(requestDTO.getIsbn())) {
            throw new BadRequestException("Book with isbn '" + requestDTO.getIsbn() + "' already exists");
        }

        Book book = bookMapper.convertFromBookRequestDtoToBook(requestDTO);

        // availableCopies is filled by @PrePersist from totalCopies.
        return bookMapper.convertFromBookToBookDto(bookRepo.save(book));
    }

    @Override
    public BookDTO updateBook(Long id, BookRequestDTO requestDTO) {
        Book book = findBookOrThrow(id);

        if (bookRepo.existsByIsbnAndIdNot(requestDTO.getIsbn(), id)) {
            throw new BadRequestException("Book with isbn '" + requestDTO.getIsbn() + "' already exists");
        }

        // Copies that are currently borrowed must stay borrowed: shrinking the
        // shelf below them would silently create negative availability.
        int borrowed = book.getTotalCopies() - book.getAvailableCopies();

        if (requestDTO.getTotalCopies() < borrowed) {
            throw new BadRequestException(
                    "Total copies cannot be less than the " + borrowed + " copies currently borrowed");
        }

        // Managed entity: dirty checking flushes title/author/isbn/totalCopies.
        bookMapper.updateBookFromBookRequestDto(requestDTO, book);

        book.setAvailableCopies(requestDTO.getTotalCopies() - borrowed);

        return bookMapper.convertFromBookToBookDto(book);
    }

    @Override
    public void deleteBook(Long id) {
        Book book = findBookOrThrow(id);

        // Refuse the delete while borrow rows still reference this book,
        // otherwise the FK constraint fails and the client gets a 500.
        if (borrowHelper.hasBorrowHistoryForBook(id)) {
            throw new BadRequestException(
                    "Book with id " + id + " has borrow history and cannot be deleted");
        }

        bookRepo.delete(book);
    }

    // Shared helper for safe entity retrieval with consistent 404 messaging.
    private Book findBookOrThrow(Long id) {
        return bookRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Book with id " + id + " not found"));
    }
}
