package com.dmsql5303.shop.myblog.service;

import com.dmsql5303.shop.myblog.model.Article;
import com.dmsql5303.shop.myblog.dto.ArticleRequestDto;
import com.dmsql5303.shop.myblog.model.Comment;
import com.dmsql5303.shop.myblog.model.User;
import com.dmsql5303.shop.myblog.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import javax.transaction.Transactional;
import java.util.Optional;


@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleService( ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }


    //페이징 처리
    public Page<Article> getArticleList(int page,int size,String sortBy, boolean isAsc){
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        return articleRepository.findAll(pageable);
    }


    // 게시글 작성
    public Article createArticle(ArticleRequestDto articleRequestDto, User user){

        Article article = new Article(articleRequestDto,user);

        // 게시글 제목 null 체크
        if(articleRequestDto.getTitle()== null || articleRequestDto.getTitle().isEmpty()) {
            throw new IllegalArgumentException("제목은 비워둘수 없습니다.");
        }

        articleRepository.save(article);
        return article;
    }

    // 특정 게시글 가져오기
    public Article getArticle(Long aid){
        return articleRepository.findById(aid).orElseThrow(()-> new NullPointerException("articleNull"));
    }


    // 게시글 업데이트
    @Transactional
    public Long updateArticle(ArticleRequestDto requestDto,Long aid){

        //title null check
        if(requestDto.getTitle()== null || requestDto.getTitle().isEmpty()) {
            throw new IllegalArgumentException("titleNull");
        }

        //aritcle 있는지 체크
        Article article = articleRepository.findById(aid)
                .orElseThrow(
                        ()-> new IllegalArgumentException("게시물이 존재하지않습니다.")
                );

        article.update(requestDto);
        return article.getAid();
    }


    public String deleteArticle(Long aid){
        Article article = articleRepository.findById(aid).orElseThrow(()->new NullPointerException("articleNull"));
        if(article != null){
            articleRepository.deleteById(aid);
            return "success";
        }
        return "fail";
    }
}
