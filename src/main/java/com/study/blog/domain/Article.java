package com.study.blog.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import org.springframework.boot.autoconfigure.web.WebProperties;

@Entity
public class Article {
    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id",updatable = false)
    private Long id;

    @Getter
    @Column(name="title",nullable = false)
    private String title;

    @Getter
    @Column(name="content", nullable = false)
    private String content;
    @Builder
    public Article(String title, String content){
        this.title=title;
        this.content=content;
    }
    protected Article(){}

    public void update(String title,String content)
    {
        this.title=title;
        this.content=content;
    }
}
