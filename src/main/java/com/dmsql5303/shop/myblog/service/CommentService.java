package com.dmsql5303.shop.myblog.service;

import com.dmsql5303.shop.myblog.dto.CommentRequestDto;
import com.dmsql5303.shop.myblog.model.Article;
import com.dmsql5303.shop.myblog.model.Comment;
import com.dmsql5303.shop.myblog.model.User;
import com.dmsql5303.shop.myblog.repository.CommentRepository;
import com.dmsql5303.shop.myblog.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final ArticleService articleService;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository,ArticleService articleService,UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.articleService = articleService;
        this.userRepository = userRepository;
    }

    // 댓글작성
    public Comment addComment(CommentRequestDto requestDto, User user,Long aid){
        // JoinColumn을 위한 article
        Article article = articleService.getArticle(aid);

        Comment comment = new Comment(requestDto.getContent(),user,article);
        return commentRepository.save(comment);
    }

    //댓글 목록
    public List<Comment> getComment(Long aid){
        Article article = articleService.getArticle(aid);
        return commentRepository.findAllByArticleOrderByCreatedAtDesc(article);
    }

    //댓글 수정
    @Transactional
    public Long updateComment(CommentRequestDto commentRequestDto,Long cid){

        Comment comment = commentRepository.findById(cid).orElseThrow(()-> new NullPointerException("commentNull"));
       comment.update(commentRequestDto.getContent());

       return comment.getCid();
    }

    //댓글 삭제
    public String DeleteComment(Long cid){
        commentRepository.deleteById(cid);
        return "success";
    }
}
