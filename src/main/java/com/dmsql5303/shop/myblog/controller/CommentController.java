package com.dmsql5303.shop.myblog.controller;

import com.dmsql5303.shop.myblog.dto.CommentRequestDto;
import com.dmsql5303.shop.myblog.model.Comment;
import com.dmsql5303.shop.myblog.security.UserDetailsImpl;
import com.dmsql5303.shop.myblog.service.CommentService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // 사용자가 로그인했는지 여부 체크
    @GetMapping("/comments/users")
    public String getUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        String result ="";


        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        // 로그인 안했다면
        if(username.equals("anonymousUser") || username == null) {
            result = "anonymousUser";

            //로그인했다면
        }else if(!userDetails.getUser().getUsername().isEmpty()){
            result = userDetails.getUsername();
        }

        return result;
    }


    // 댓글 작성
    @PostMapping("/comments/{aid}")
    public String createComment(@RequestBody CommentRequestDto commentRequestDto, @PathVariable Long aid,
                                @AuthenticationPrincipal UserDetailsImpl userDetails) {

        String result = userDetails.getUsername();

        // null 체크
        if(result.equals("anonymousUser") || result == null) {
            return "fail";
        }else{
            commentService.addComment(commentRequestDto, userDetails.getUser(), aid);
            return "success";
        }
    }

    //댓글 리스트 가져오기
    @GetMapping("/comments/{aid}")
    public List<Comment> getComment(@PathVariable  Long aid){
        return commentService.getComment(aid);
    }


    //댓글 수정
    @PutMapping("/comments/{cid}")
    public Long updateComment(@RequestBody CommentRequestDto commentRequestDto,@PathVariable Long cid){
        return commentService.updateComment(commentRequestDto,cid);
    }

    // 댓글 삭제
    @DeleteMapping("/comments/{cid}")
    public String deleteUpdate(@PathVariable Long cid){
        return commentService.DeleteComment(cid);
    }

}

