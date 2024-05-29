package com.example.cms.service;

import com.example.cms.dto.MemberDTO;
import com.example.cms.dto.RegisterDTO;
import com.example.cms.exception.MemberNotFoundException;

public interface MemberService {

    public MemberDTO create(RegisterDTO registerDTO) throws Exception;

    MemberDTO getMember() throws MemberNotFoundException;

    MemberDTO findById(long id) throws MemberNotFoundException;

    String getUsernameOfCurrentLoginMember() throws MemberNotFoundException;
}
