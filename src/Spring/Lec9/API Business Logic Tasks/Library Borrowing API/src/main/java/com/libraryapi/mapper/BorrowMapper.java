package com.libraryapi.mapper;

import com.libraryapi.dto.BorrowDTO;
import com.libraryapi.model.Borrow;
import org.mapstruct.Mapper;

import java.util.List;

// BookMapper / MemberMapper provide the nested view DTOs, and
// BorrowStatusMapper provides the nested status view.
@Mapper(componentModel = "spring", uses = {BookMapper.class, MemberMapper.class, BorrowStatusMapper.class})
public interface BorrowMapper {

    BorrowDTO convertFromBorrowToBorrowDto(Borrow borrow);

    List<BorrowDTO> convertFromBorrowListToBorrowDtoList(List<Borrow> borrows);
}
