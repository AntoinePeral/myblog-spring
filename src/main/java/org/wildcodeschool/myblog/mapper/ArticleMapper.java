package org.wildcodeschool.myblog.mapper;

import org.springframework.stereotype.Component;
import org.wildcodeschool.myblog.dto.*;
import org.wildcodeschool.myblog.model.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ArticleMapper {
    public ArticleDTO convertToDTO(Article article) {
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setId(article.getId());
        articleDTO.setTitle(article.getTitle());
        articleDTO.setContent(article.getContent());
        articleDTO.setUpdatedAt(article.getUpdatedAt());
        if (article.getCategory() != null) {
            articleDTO.setCategoryName(article.getCategory().getName());
        }
        if (article.getImages() != null) {
            articleDTO.setImageUrls(article.getImages().stream().map(Image::getUrl).collect(Collectors.toList()));
        }

        if (article.getArticleAuthors() != null) {
            articleDTO.setArticleAuthorDTOs(article.getArticleAuthors().stream()
                    .filter(articleAuthor -> articleAuthor.getAuthor() != null)
                    .map(articleAuthor -> {
                        ArticleAuthorDTO articleAuthorDTO = new ArticleAuthorDTO();
                        articleAuthorDTO.setArticleId(articleAuthor.getArticle().getId());
                        articleAuthorDTO.setAuthorId(articleAuthor.getAuthor().getId());
                        articleAuthorDTO.setContribution(articleAuthor.getContribution());
                        return articleAuthorDTO;
                    })
                    .collect(Collectors.toList()));
        }

        return articleDTO;
    }

    public Article convertToEntity(ArticleCreateDTO articleCreateDTO){
        Article article = new Article();
        article.setTitle(articleCreateDTO.getTitle());
        article.setContent(articleCreateDTO.getContent());
        article.setCreatedAt(LocalDateTime.now());
        article.setUpdatedAt(LocalDateTime.now());

        if(articleCreateDTO.getCategoryId() != null){
            Category category = new Category();
            category.setId(articleCreateDTO.getCategoryId());
            article.setCategory(category);
        }

        if(articleCreateDTO.getImages() != null){
            article.setImages( articleCreateDTO.getImages()
                    .stream()
                    .map( image ->{
                        Image newImage = new Image();
                        newImage.setUrl(image.getUrl());
                        return newImage;
                    })
                    .collect(Collectors.toList())
            );
        }

        if(articleCreateDTO.getAuthors() != null){
            article.setArticleAuthors(articleCreateDTO.getAuthors()
                    .stream()
                    .map(articleAuthorDTO -> {
                        ArticleAuthor articleAuthor = new ArticleAuthor();
                        Author author = new Author();
                        author.setId(articleAuthorDTO.getAuthorId());
                        articleAuthor.setAuthor(author);
                        articleAuthor.setContribution(articleAuthorDTO.getContribution());
                        return articleAuthor;
                    })
                    .collect(Collectors.toList())
            );
        }

        return article;
    }
}
