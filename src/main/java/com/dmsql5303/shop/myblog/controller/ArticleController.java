package com.dmsql5303.shop.myblog.controller;

import com.dmsql5303.shop.myblog.model.Article;
import com.dmsql5303.shop.myblog.dto.ArticleRequestDto;
import com.dmsql5303.shop.myblog.model.Comment;
import com.dmsql5303.shop.myblog.security.UserDetailsImpl;
import com.dmsql5303.shop.myblog.service.ArticleService;
import com.dmsql5303.shop.myblog.service.CommentService;
import com.dmsql5303.shop.myblog.util.ArletMessage;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
public class ArticleController {

    private final ArticleService articleService;
    private final CommentService commentService;

    public ArticleController(ArticleService articleService,CommentService commentService) {
        this.articleService = articleService;
        this.commentService = commentService;
    }


    //게시글 작성 페이지
    @GetMapping("/articles/update")
    public String getArticleWrite(Model model){

        // 사용자 체크
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        // 로그인이 안되있으면
        if(username.equals("anonymousUser") || username == null) {
            model.addAttribute("data",new ArletMessage("로그인하세요.","/users/login"));
            return "arletMessege";
        }
        //로그인 되있으면
        return "articleWrite";
    }



    //게시글 가져오기
    @GetMapping("/articles")
    @ResponseBody
    public Page<Article> getArticleList(@RequestParam("page") int page ,
                                        @RequestParam("size") int size,
                                        @RequestParam("sortBy") String sortBy,
                                        @RequestParam("isAsc") boolean isAsc){
        page = page -1;
        return articleService.getArticleList(page,size,sortBy,isAsc);
    }



    // 게시글 작성
    @PostMapping("/articles")
    public String createArticle(Model model,
                                ArticleRequestDto requestDto,
                                @AuthenticationPrincipal UserDetailsImpl userDetails) {

            //title값이 null인지 체크해서 받은 오류 catch
            try {
                articleService.createArticle(requestDto, userDetails.getUser());

            }catch (IllegalArgumentException e){
                model.addAttribute("error",e.getMessage());
                return "articleWrite";
            }
            //오류없을 시 성공 메세지 전달
            model.addAttribute("data",new ArletMessage("게시글이 등록되었습니다.","/main"));

            // 메세지를 띄워줄 html
            return "arletMessege";
    }


    // 특정 게시글 조회
    @RequestMapping("/articles/{aid}")
    public String getArticle(Model model, @PathVariable("aid") Long aid){

        //article 가져오기
       Article article = articleService.getArticle(aid);
        model.addAttribute("articleOne",article);


        // 로그인한 유저 정보  없을시 anonymousUser 출력
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        //로그인한 유저와 글을 쓴 유저가 같은지 체크해서 수정|삭제 버튼 표시
        if(article.getUser().getUsername().equals(username)){
            model.addAttribute("userCheck",true);
        }


        return "articleView";
    }


    //수정 페이지 가져오기
    @RequestMapping("/articles/update/{aid}")
    public String getArticleModfiy(Model model, @PathVariable("aid") Long aid){

        // 사용자 정보 가져오기
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        // 사용자 정보가 anonymousUser => 로그인이 안했다면
        if(username.equals("anonymousUser") || username == null) {
            model.addAttribute("data",new ArletMessage("로그인하세요.","/users/login"));
            return "arletMessege";
        }

        //로그인 되있을시
        Article article = articleService.getArticle(aid);
        model.addAttribute("articleOne",article);

        return "articleModify";
    }


    // 수정 기능 구현
    @PutMapping("/articles/{aid}")
    @ResponseBody
    public String updateArticle(@RequestBody ArticleRequestDto requestDto,@PathVariable Long aid) {
        String message="";



        try {
            articleService.updateArticle(requestDto, aid);

        // error 던져서 ajax에 전달.
        }catch (IllegalArgumentException e){
            message=e.getMessage();
            return message;
        }
        // error없을시
        message = aid.toString();

        return message;
    }

    // 게시글 삭제
    @DeleteMapping("/articles/{aid}")
    @ResponseBody
    public String deleteArticle(@PathVariable Long aid){

        //게시글의 댓글 불러옴.
        List<Comment> commentList = commentService.getComment(aid);

        if(commentList == null){

            return articleService.deleteArticle(aid);

        }else{
            //for문을 통해 해당 게시글의 댓글 삭제
            for(int i=0; i<commentList.size(); i++){
                Long cid = commentList.get(i).getCid();
                System.out.println(cid);
                commentService.DeleteComment(cid);
            }
            return articleService.deleteArticle(aid);
        }

    }
}
