package org.wildcodeschool.myblog.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ArticleUpdateDTO {
    private Long id;
    private String title;
    private String content;
    private boolean published;
    private String categoryName;
    private List<ImageDTO> images; // remplace imageIds + imageUrls
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ArticleAuthorDTO> articleAuthorDTOs;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean getPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public String getCategoryName() {
        return categoryName;
    }


    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }


    public List<ImageDTO> getImages() {
        return images;
    }

    public void setImages(List<ImageDTO> images) {
        this.images = images;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<ArticleAuthorDTO> getArticleAuthorDTOs() {
        return articleAuthorDTOs;
    }

    public void setArticleAuthorDTOs(List<ArticleAuthorDTO> articleAuthorDTOs) {
        this.articleAuthorDTOs = articleAuthorDTOs;
    }

    @Override
    public String toString() {
        System.out.println("articleAuthorDTOs:" + articleAuthorDTOs );
        return "";
    }
}
