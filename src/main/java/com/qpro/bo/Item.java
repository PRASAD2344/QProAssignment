package com.qpro.bo;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class Item implements Serializable{
    private Long id;
    private String title;
    private String url;
    private Long score;
    private Long time;
    private String by;
    private List<Long> kids;
    private String type;
    private String text;
    public int numberOfKids(){
        return this.kids == null ? 0 : this.kids.size();
    }
    public boolean isStory(){ return this.type.equals("story"); }
}
