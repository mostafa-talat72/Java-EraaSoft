package com.productdiscountapi.service;

import com.productdiscountapi.dto.DiscountCodeDTO;
import com.productdiscountapi.dto.DiscountCodeRequestDTO;
import com.productdiscountapi.model.DiscountCode;

import java.util.List;

public interface DiscountCodeService {

    List<DiscountCodeDTO> getAllDiscountCodes();

    DiscountCode getDiscountCodeEntityById(Long id);

    DiscountCode getDiscountCodeEntityByCode(String code);

    DiscountCodeDTO getDiscountCodeById(Long id);

    DiscountCodeDTO getDiscountCodeByCode(String code);

    DiscountCodeDTO createDiscountCode(DiscountCodeRequestDTO requestDTO);

    DiscountCodeDTO updateDiscountCode(Long id, DiscountCodeRequestDTO requestDTO);

    void deleteDiscountCode(Long id);
}
