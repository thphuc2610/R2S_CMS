package com.example.cms.service.impl;

import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.cms.dto.MemberDTO;
import com.example.cms.dto.RegisterDTO;
import com.example.cms.entity.Member;
import com.example.cms.exception.MemberNotFoundException;
import com.example.cms.mapper.MemberMapper;
import com.example.cms.repository.MemberRepository;
import com.example.cms.service.MemberService;

@Service
public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MemberMapper memberMapper;

    public MemberServiceImpl(MemberRepository memberRepository, BCryptPasswordEncoder bCryptPasswordEncoder,
            MemberMapper memberMapper) {
        this.memberRepository = memberRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.memberMapper = memberMapper;
    }

    /**
     * Tạo mới người dùng.
     *
     * @param RegisterDTO Đối tượng RegisterDTO chứa thông tin người dùng cần tạo
     *                    mới
     * @return Đối tượng UserDTO đã được tạo mới
     * @throws Exception Nếu xảy ra lỗi trong quá trình tạo mới người dùng
     */
    @Transactional
    @Override
    public MemberDTO create(RegisterDTO registerDTO) throws Exception {
        // Kiểm tra tên người dùng đã tồn tại chưa
        Optional<Member> existedMember = memberRepository.findByUsername(registerDTO.getUsername());
        if (existedMember.isPresent()) {
            throw new Exception("Tên đăng nhập đã đăng ký trước đó!");
        }

        // Kiểm tra email người dùng đã tồn tại chưa
        Optional<Member> existedEmail = memberRepository.findByEmail(registerDTO.getEmail());
        if (existedEmail.isPresent()) {
            throw new Exception("Email người dùng đã đăng ký trước đó!");
        }

        // Tạo người dùng mới
        registerDTO.setPassword(bCryptPasswordEncoder.encode(registerDTO.getPassword()));

        Member member = memberMapper.toEntity(registerDTO);
        member = memberRepository.save(member);
        return memberMapper.toMemberDTO(member);
    }

    @Transactional(readOnly = true)
    @Override
    public MemberDTO findById(long id) throws MemberNotFoundException {
        return memberMapper.toMemberDTO(memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("Người dùng này không tồn tại!")));
    }

    @Transactional(readOnly = true)
    @Override
    public MemberDTO getMember() throws MemberNotFoundException {
        String username = getUsernameOfCurrentLoginMember();
        return memberMapper.toMemberDTO(memberRepository.findByUsername(username)
                .orElseThrow(() -> new MemberNotFoundException("Người dùng không tồn tại!")));
    }

    @Override
    public String getUsernameOfCurrentLoginMember() throws MemberNotFoundException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (username.equals("anonymousMember")) {
            throw new MemberNotFoundException("Vui lòng đăng nhập!");
        }
        return username;
    }

}
