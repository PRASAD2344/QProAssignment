package com.qpro.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentDTO {
    private String text;
    private String by;
    private String userHandle;
    private int ageOfProfileInYears;
}
