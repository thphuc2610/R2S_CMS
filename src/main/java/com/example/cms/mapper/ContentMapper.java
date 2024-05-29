package com.example.cms.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import com.example.cms.dto.ContentDTO;
import com.example.cms.entity.Content;

@Mapper(componentModel = "spring")
@Component
public interface ContentMapper {
    @Mapping(target = "author.id", source = "author")
    Content toEntity(ContentDTO contentDTO);

    @Mapping(target = "author", source = "author.id")
    ContentDTO toDTO(Content content);
}
