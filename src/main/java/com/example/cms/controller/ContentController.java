package com.example.cms.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.cms.constant.PaginateConstant;
import com.example.cms.dto.ContentDTO;
import com.example.cms.dto.MemberDTO;
import com.example.cms.exception.ContentNotFoundException;
import com.example.cms.service.ContentService;
import com.example.cms.service.MemberService;

@RestController
@RequestMapping("/api/vs1/content")
public class ContentController {
    private final ContentService contentService;
    private final MemberService memberService;

    public ContentController(ContentService contentService, MemberService memberService) {
        this.contentService = contentService;
        this.memberService = memberService;
    }

    @GetMapping("/paginate/{memberId}")
    public ResponseEntity<?> getAllContentPaginate(
            @PathVariable(value = "memberId") long memberId,
            @RequestParam(defaultValue = PaginateConstant.DEFAULT_PAGE_NUMBER) int no,
            @RequestParam(defaultValue = PaginateConstant.DEFAULT_PAGE_LIMIT) int limit,
            @RequestParam(required = false) String keyword) {
        try {
            MemberDTO existedMember = memberService.getMember();
            if (existedMember.getId() != memberId) {
                return new ResponseEntity<>("Nội dung này không phải do bạn tạo ra nên bạn không thể xem nó",
                        HttpStatus.NOT_ACCEPTABLE);
            }
            return ResponseEntity.ok(contentService.findAllContentPaginate(memberId, no, limit, keyword));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{memberId}/{id}")
    public ResponseEntity<?> getContentById(@PathVariable(value = "memberId") long memberId,
            @PathVariable(value = "id") long id) {
        try {
            MemberDTO existedMember = memberService.getMember();
            if (existedMember.getId() != memberId) {
                return new ResponseEntity<>("Nội dung này không phải do bạn tạo ra nên bạn không thể xem nó",
                        HttpStatus.NOT_ACCEPTABLE);
            }
            return ResponseEntity.ok(contentService.findContentById(memberId, id));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ContentDTO contentDTO) {
        try {
            return ResponseEntity.ok(contentService.create(contentDTO));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{memberId}/{id}")
    public ResponseEntity<?> update(@PathVariable(value = "memberId") long memberId,
            @PathVariable(value = "id") long id,
            @RequestBody ContentDTO contentDTO) {
        try {
            MemberDTO existedMember = memberService.getMember();
            if (existedMember.getId() != memberId) {
                return new ResponseEntity<>("Nội dung này không phải do bạn tạo ra nên bạn không thể cập nhật nó",
                        HttpStatus.NOT_ACCEPTABLE);
            }
            return ResponseEntity.ok(contentService.update(id, contentDTO));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{memberId}/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "memberId") long memberId,
            @PathVariable(value = "id") long id) {
        try {
            MemberDTO existedMember = memberService.getMember();
            if (existedMember.getId() != memberId) {
                return new ResponseEntity<>("Nội dung này không phải do bạn tạo ra nên bạn không thể xoá nó",
                        HttpStatus.NOT_ACCEPTABLE);
            }
            contentService.delete(id);
            return ResponseEntity.ok("Xoá nội dung thành công");
        } catch (ContentNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }
}
