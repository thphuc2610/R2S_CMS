package com.example.cms.service;

import com.example.cms.dto.ContentDTO;
import com.example.cms.dto.PaginationDTO;
import com.example.cms.exception.ContentNotFoundException;

public interface ContentService {
    PaginationDTO findAllContentPaginate(long memberId, int no, int limit, String keyword)
    throws ContentNotFoundException;

    ContentDTO findContentById(long memberId, long id) throws ContentNotFoundException;

    ContentDTO create(ContentDTO contentDTO);

    ContentDTO update(long id, ContentDTO contentDTO) throws ContentNotFoundException;

    String delete(long id) throws ContentNotFoundException;
}
