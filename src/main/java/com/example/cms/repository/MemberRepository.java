package com.example.cms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cms.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long>{
    public boolean existsByUsername(String username);

    Optional<Member> findByUsername(String username);

    Optional<Member> findByEmail(String email);
}
