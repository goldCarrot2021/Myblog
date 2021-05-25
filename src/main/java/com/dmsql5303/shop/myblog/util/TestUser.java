package com.dmsql5303.shop.myblog.util;


import com.dmsql5303.shop.myblog.model.Article;
import com.dmsql5303.shop.myblog.dto.ArticleRequestDto;
import com.dmsql5303.shop.myblog.model.User;
import com.dmsql5303.shop.myblog.repository.ArticleRepository;
import com.dmsql5303.shop.myblog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

//@Component
public class TestUser implements ApplicationRunner {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ArticleRepository articleRepository;


    User user;

    /*
    * 테스트를 위한 데이터 저장
    *
    * */

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 테스트 User 생성
        createUser();
        for(int i =0; i<20; i++){
            createArticle();
        }

    }

    public void createUser(){
        String username = "test1";
        String password = "1234";
        String encodePassword = passwordEncoder.encode(password);
        Long kakaoId = 1L;
        String email = "test@naver.com";

        user = new User(username,encodePassword,email,kakaoId);

        userRepository.save(user);

    }

    public void createArticle(){
        String title ="테스트게시글입니다.";
        String content ="테스트 내용입니다.";
        ArticleRequestDto dto = new ArticleRequestDto(title,content);

        Article article = new Article(dto,user);

        articleRepository.save(article);

    }
}
