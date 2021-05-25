package com.dmsql5303.shop.myblog.controller;

import com.dmsql5303.shop.myblog.dto.SignRequestDto;
import com.dmsql5303.shop.myblog.security.UserDetailsImpl;
import com.dmsql5303.shop.myblog.service.UserService;
import com.dmsql5303.shop.myblog.util.ArletMessage;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/signup")
    public String register(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        //getUsername이 null이면 로그인 안한상태
        try{
            userDetails.getUsername();
        }catch (NullPointerException e) {
            return "signup";
        }
        //null이 아니면 접근불가
        return "loginFordidden";
    }


    @PostMapping("/users/api/signup")
    public String createUser(SignRequestDto reqeustDto, Model model) {

        try {
            userService.createUser(reqeustDto);

            // 예외 던질 시 다시 error메세지와 함께 회원가입 페이지로
        } catch (IllegalArgumentException e) {
            model.addAttribute("signError", e.getMessage());
            return "signup";
        }

        //아닐시 ArletMessage class를 통해 alert창 띄움
        model.addAttribute("data",new ArletMessage("회원가입이 완료되었습니다.","/users/login"));
        return "arletMessege";
    }


    // 로그인
    @GetMapping("/users/login")
    public String login(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        //getUsername이 null이면 로그인 안한상태
        try{
            userDetails.getUsername();
        }catch (NullPointerException e) {

            return "login";
        }
        //null이 아니면 접근불가
        return "loginFordidden";
    }


    @GetMapping("/users/login/error")
    public String loginError(Model model){
        model.addAttribute("loginError",true);
        return "login";
    }

    //카카오톡 로그인 구현
    @GetMapping("/users/kakao/callback")
    public String kakaoLogin(String code) {
        userService.kakaoLogin(code);
        return "main";
    }

}
