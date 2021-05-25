package com.dmsql5303.shop.myblog.controller;

import com.dmsql5303.shop.myblog.service.ArticleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final ArticleService articleService;

    public HomeController(ArticleService articleService){
        this.articleService =articleService;
    }

    @GetMapping("/main")
    public String home(Model model){
        return "main";
    }

    @GetMapping("/")
    public String login(Model model){
        return "login";
    }


    @GetMapping("/users/longForbidden")
    public String loginErro(){
        return "loginFordidden";
    }

}
