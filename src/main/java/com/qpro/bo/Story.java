package com.qpro.bo;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class Story implements Serializable{
    private String title;
    private String url;
    private long score;
    private long time;
    private String by;
}
