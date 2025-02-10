package org.wildcodeschool.myblog.service;

import org.springframework.stereotype.Service;
import org.wildcodeschool.myblog.dto.AuthorDTO;
import org.wildcodeschool.myblog.mapper.AuthorMapper;
import org.wildcodeschool.myblog.model.ArticleAuthor;
import org.wildcodeschool.myblog.model.Author;
import org.wildcodeschool.myblog.repository.ArticleAuthorRepository;
import org.wildcodeschool.myblog.repository.ArticleRepository;
import org.wildcodeschool.myblog.repository.AuthorRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final ArticleAuthorRepository articleAuthorRepository;
    private final ArticleRepository articleRepository;
    private final AuthorMapper authorMapper;

    public AuthorService(
            ArticleAuthorRepository articleAuthorRepository,
            AuthorRepository authorRepository,
            ArticleRepository articleRepository,
            AuthorMapper authorMapper){
        this.articleAuthorRepository = articleAuthorRepository;
        this.authorRepository = authorRepository;
        this.articleRepository = articleRepository;
        this.authorMapper = authorMapper;
    }

    public List<AuthorDTO> getAllAuthors(){
        List<Author> authors =  authorRepository.findAll();
        if(authors.isEmpty()){
            return null;
        }

        return authors.stream().map(authorMapper::convertToDTO).collect(Collectors.toList());
    }

    public AuthorDTO getAuthorById(Long id){
        Author author = authorRepository.findById(id).orElse(null);
        if(author == null){
            return null;
        }
        return authorMapper.convertToDTO(author);
    }

    public AuthorDTO createAuthor(Author author){
        Author savedAuthor = authorRepository.save(author);
        return authorMapper.convertToDTO(savedAuthor);
    }

    public AuthorDTO updateAuthor( Long id, Author authorDetails){
        Author author = authorRepository.findById(id).orElse(null);

        if (author == null){
            return null;
        }

        author.setFirstname(authorDetails.getFirstname());
        author.setLastname(authorDetails.getLastname());

        Author updatedAuthor = authorRepository.save(author);
        return authorMapper.convertToDTO(updatedAuthor);
    }

    public boolean deleteAuthor(Long id){
        Author author = authorRepository.findById(id).orElse(null);
        if(author == null){
            return false;
        }
        if(author.getArticleAuthors() != null){
            for(ArticleAuthor articleAuthor : author.getArticleAuthors()){
                articleAuthorRepository.delete(articleAuthor);
            }
        }
        authorRepository.delete(author);
        return true;
    }
}
