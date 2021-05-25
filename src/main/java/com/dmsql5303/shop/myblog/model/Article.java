package com.dmsql5303.shop.myblog.model;

import com.dmsql5303.shop.myblog.dto.ArticleRequestDto;
import com.dmsql5303.shop.myblog.util.Timestamped;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Article extends Timestamped {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long aid;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private String username;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    public Article(ArticleRequestDto requestDto, User user){
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.user = user;
        this.username= user.getUsername();
    }

    public void update(ArticleRequestDto requestDto){
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
    }
}
