package org.wildcodeschool.myblog.mapper;

import org.springframework.stereotype.Component;
import org.wildcodeschool.myblog.dto.ArticleAuthorDTO;
import org.wildcodeschool.myblog.dto.AuthorDTO;
import org.wildcodeschool.myblog.model.Author;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuthorMapper {
    public AuthorDTO convertToDTO(Author author){
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setId(author.getId());
        authorDTO.setLastname(author.getLastname());
        authorDTO.setFirstname(author.getFirstname());
        if (author.getArticleAuthors() != null) {
            authorDTO.setArticleAuthors(author.getArticleAuthors()
                .stream()
                .filter(articleAuthor -> articleAuthor.getArticle() != null)
                .map(articleAuthor -> {
                    ArticleAuthorDTO articleAuthorDTO = new ArticleAuthorDTO();
                    articleAuthorDTO.setArticleId(articleAuthor.getArticle().getId());
                    articleAuthorDTO.setAuthorId(articleAuthor.getAuthor().getId());
                    articleAuthorDTO.setContribution(articleAuthor.getContribution());
                    return articleAuthorDTO;
                })
                .collect(Collectors.toList()));;
        }
        return authorDTO;
    }
}
