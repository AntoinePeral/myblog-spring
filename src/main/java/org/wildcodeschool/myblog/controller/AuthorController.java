package org.wildcodeschool.myblog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wildcodeschool.myblog.dto.ArticleAuthorDTO;
import org.wildcodeschool.myblog.dto.AuthorDTO;
import org.wildcodeschool.myblog.model.*;
import org.wildcodeschool.myblog.repository.ArticleAuthorRepository;
import org.wildcodeschool.myblog.repository.ArticleRepository;
import org.wildcodeschool.myblog.repository.AuthorRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/authors")
public class AuthorController {
    private final AuthorRepository authorRepository;
    private final ArticleAuthorRepository articleAuthorRepository;
    private final ArticleRepository articleRepository;

    public AuthorController(
            ArticleAuthorRepository articleAuthorRepository,
            AuthorRepository authorRepository,
            ArticleRepository articleRepository){
        this.articleAuthorRepository = articleAuthorRepository;
        this.authorRepository = authorRepository;
        this.articleRepository = articleRepository;
    }

    @GetMapping
    public ResponseEntity<List<AuthorDTO>> getAllAuthors(){
        List<Author> authors =  authorRepository.findAll();
        if(authors.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        List<AuthorDTO> authorsDTO = authors.stream().map(this::convertToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(authorsDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDTO> getAuthorById(@PathVariable Long id){
        Author author = authorRepository.findById(id).orElse(null);
        if(author == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertToDTO(author));
    }

    @PostMapping
    public ResponseEntity<AuthorDTO> createAuthor(@RequestBody Author author){

//        if(author.getArticleAuthors() != null){
//            for(ArticleAuthor articleAuthor : author.getArticleAuthors()){
//                Article article = articleAuthor.getArticle();
//                article = articleRepository.findById(article.getId()).orElse(null);
//
//                if(article == null){
//                    return ResponseEntity.badRequest().body(null);
//                }
//
//                articleAuthor.setArticle(article);
//                articleAuthor.setAuthor(author);
//                articleAuthor.setContribution(articleAuthor.getContribution());
//
//                articleAuthorRepository.save(articleAuthor);
//            }
//        }

        Author savedAuthor = authorRepository.save(author);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(savedAuthor));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorDTO> updateAuthor(@PathVariable Long id, @RequestBody Author authorDetails){
        Author author = authorRepository.findById(id).orElse(null);

        if (author == null){
            return ResponseEntity.notFound().build();
        }

        author.setFirstname(authorDetails.getFirstname());
        author.setLastname(authorDetails.getLastname());

//        if(authorDetails.getArticleAuthors() != null){
//            // Supprimer manuellement les anciens ArticleAuthor
//            for (ArticleAuthor oldArticleAuthor : authorDetails.getArticleAuthors()) {
//                articleAuthorRepository.delete(oldArticleAuthor);
//            }
//
//            List<ArticleAuthor> updatedArticleAuthors  = new ArrayList<>();
//
//            for(ArticleAuthor articleAuthor : authorDetails.getArticleAuthors()){
//                Article article = articleAuthor.getArticle();
//                article = articleRepository.findById(article.getId()).orElse(null);
//
//                if(article == null){
//                    return ResponseEntity.badRequest().build();
//                }
//
//                ArticleAuthor newArticleAuthor = new ArticleAuthor();
//                newArticleAuthor.setArticle(article);
//                newArticleAuthor.setAuthor(author);
//                newArticleAuthor.setContribution(articleAuthor.getContribution());
//
//                updatedArticleAuthors.add(newArticleAuthor);
//            }
//
//            for (ArticleAuthor articleAuthor : updatedArticleAuthors) {
//                articleAuthorRepository.save(articleAuthor);
//            }
//
//            author.setArticleAuthors(updatedArticleAuthors);
//        }
        Author updatedAuthor = authorRepository.save(author);
        return ResponseEntity.ok(convertToDTO(updatedAuthor));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id){
        Author author = authorRepository.findById(id).orElse(null);
        if(author == null){
            return ResponseEntity.notFound().build();
        }

        if(author.getArticleAuthors() != null){
            for(ArticleAuthor articleAuthor : author.getArticleAuthors()){
                articleAuthorRepository.delete(articleAuthor);
            }
        }

        authorRepository.delete(author);
        return ResponseEntity.noContent().build();
    }


    private AuthorDTO convertToDTO(Author author){
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
