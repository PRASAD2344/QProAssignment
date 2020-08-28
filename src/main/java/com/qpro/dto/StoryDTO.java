package com.qpro.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class StoryDTO implements Serializable {
    private String title;
    private String url;
    private Long score;
    private Long submittedAt;
    private String submittedBy;
}
