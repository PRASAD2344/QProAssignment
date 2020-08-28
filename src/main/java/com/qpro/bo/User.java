package com.qpro.bo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class User {
    private String about;
    private Long created;
    private String id;
    private Long karma;
    private List<Long> submitted;
}
