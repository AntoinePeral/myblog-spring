package org.wildcodeschool.myblog.dto;

import org.wildcodeschool.myblog.model.Article;
import org.wildcodeschool.myblog.model.Author;

public class ArticleAuthorDTO {
    private Long id;
    private Author author;
    private Article article;
    private String contribution;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public String getContribution() {
        return contribution;
    }

    public void setContribution(String contribution) {
        this.contribution = contribution;
    }
}
