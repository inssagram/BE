package com.be.inssagram.domain.chat;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {
    @GetMapping("/topic-view")
    public String topicView() {
        return "topic/index";
    }
}
