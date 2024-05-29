package com.example.cms.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class MemberDTO extends RegisterDTO{
    private long id;
}
