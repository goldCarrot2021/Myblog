package com.dmsql5303.shop.myblog.model;


import com.dmsql5303.shop.myblog.util.Timestamped;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Comment extends Timestamped {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long cid;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String username;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Article article;

    public Comment(String content, User user, Article article) {
        this.content = content;
        this.username = user.getUsername();
        this.user = user;
        this.article = article;
    }



    public void update(String content){
        this.content = content;
    }
}
