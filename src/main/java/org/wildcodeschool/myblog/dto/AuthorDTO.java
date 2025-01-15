package org.wildcodeschool.myblog.dto;

import org.wildcodeschool.myblog.model.Article;

import java.util.List;

public class AuthorDTO {
    private Long id;
    private String firstname;
    private String lastname;
    private List<ArticleDTO> articleList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public List<ArticleDTO> getArticleList() {
        return articleList;
    }

    public void setArticleList(List<ArticleDTO> articleList) {
        this.articleList = articleList;
    }
}
