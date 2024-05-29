package com.example.cms.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import com.example.cms.dto.MemberDTO;
import com.example.cms.dto.RegisterDTO;
import com.example.cms.entity.Member;

@Mapper(componentModel = "spring")
@Component
public interface MemberMapper {
    Member toEntity(RegisterDTO registerDTO);
    
    RegisterDTO toDTO(Member member);

    Member toEntity(MemberDTO memberDTO);

    MemberDTO toMemberDTO(Member member);
}
