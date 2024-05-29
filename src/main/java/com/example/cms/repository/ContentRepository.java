package com.example.cms.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.cms.entity.Content;

public interface ContentRepository extends JpaRepository<Content, Long> {
    @Query("SELECT c FROM Content c WHERE author.id = :memberId")
    Page<Content> findAll(Long memberId, Pageable pageable);

    @Query("SELECT c FROM Content c WHERE author.id = :memberId AND c.id = :id")
    Content findContentById(Long memberId, Long id);

    @Query("SELECT c FROM Content c WHERE author.id = :memberId AND (c.sort LIKE %:keyword% OR c.brief LIKE %:keyword% OR c.content LIKE %:keyword%)")
    Page<Content> findContentByKeyword(Long memberId, @Param("keyword") String keyword,
            Pageable pageable);

}