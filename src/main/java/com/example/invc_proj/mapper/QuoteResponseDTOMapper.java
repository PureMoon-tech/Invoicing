package com.example.invc_proj.mapper;

import com.example.invc_proj.dto.QuoteResponseDTO;
import com.example.invc_proj.model.Quote;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

public class QuoteResponseDTOMapper {

        public static QuoteResponseDTO toDTO(Quote quote)
        {
            if(quote == null)
                return null;

            QuoteResponseDTO quoteResponseDTO = new QuoteResponseDTO();
            quoteResponseDTO.setQuote_id(quote.getQuote_id());
            quoteResponseDTO.setQuote_number(quote.getQuote_number());
            quoteResponseDTO.setClient_id(quote.getClient_id());
            quoteResponseDTO.setUser_id(quote.getUser_id());
            quoteResponseDTO.setTotal(quote.getTotal());
            quoteResponseDTO.setQuoteSrvcs(ServicesQuotedDTOMapper.toDTOList(quote.getQuoteSrvcs()));

            return quoteResponseDTO;
        }


        public static List<QuoteResponseDTO> toDTOList(List<Quote> quoteList)
        {
            if(quoteList == null)
                return Collections.emptyList();

            List<QuoteResponseDTO> quoteResponseDTOList = new ArrayList<>();
            return quoteList.stream()
                    .map(QuoteResponseDTOMapper::toDTO)
                    .collect(Collectors.toList());
        }

    }

