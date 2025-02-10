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
            List<ArticleAuthorDTO> articleAuthorDTOs = author.getArticleAuthors().stream()
                    .map(articleAuthor -> {
                        ArticleAuthorDTO dto = new ArticleAuthorDTO();
                        dto.setId(articleAuthor.getId());
                        dto.setContribution(articleAuthor.getContribution());
                        return dto;
                    })
                    .collect(Collectors.toList());
            authorDTO.setArticleAuthors(articleAuthorDTOs);
        }
        return authorDTO;
    }
}
