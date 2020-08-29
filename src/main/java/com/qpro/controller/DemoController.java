package com.qpro.controller;

import com.qpro.bo.Item;
import com.qpro.bo.User;
import com.qpro.dto.CommentDTO;
import com.qpro.dto.StoryDTO;
import com.qpro.service.DemoService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Years;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class DemoController {

    @Autowired
    DemoService demoService;

    @Autowired
    ModelMapper modelMapper;

    @GetMapping("/best-stories")
    public List<StoryDTO> bestStories(HttpServletRequest request) {
        return demoService.bestStories(request.getRemoteAddr())
                .stream()
                .map(item -> modelMapper.map(item, StoryDTO.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/past-stories")
    public List<StoryDTO> pastStories(HttpServletRequest request) {
        return demoService.pastStories(request.getRemoteAddr())
                .stream()
                .map(item -> modelMapper.map(item, StoryDTO.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/comments/{storyId}")
    public List<CommentDTO> comments(@PathVariable long storyId) {
        List<Item> comments = demoService.comments(storyId);
        return comments.stream()
                .map(comment -> {
                    CommentDTO commentDTO = new CommentDTO();
                    commentDTO.setText(comment.getText());
                    commentDTO.setBy(comment.getBy());
                    return commentDTO;
                })
                .map(commentDTO -> {
                    User user = demoService.getUser(commentDTO.getBy());
                    commentDTO.setUserHandle("https://news.ycombinator.com/user?id="+commentDTO.getBy());
                    DateTime currentUTCDateTime = new DateTime(DateTimeZone.UTC);
                    //We will get created in epoch i.e., seconds past 1970. So multiplying by 1000 to convert into milliseconds
                    DateTime presentedDateTime = new DateTime(user.getCreated()*1000,DateTimeZone.UTC);
                    commentDTO.setAgeOfProfileInYears(Years.yearsBetween(presentedDateTime,currentUTCDateTime).getYears());
                    return commentDTO;
                })
                .collect(Collectors.toList());
    }
}
