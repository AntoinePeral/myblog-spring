package org.wildcodeschool.myblog.service;

import org.springframework.stereotype.Service;
import org.wildcodeschool.myblog.dto.ArticleCreateDTO;
import org.wildcodeschool.myblog.dto.ArticleDTO;
import org.wildcodeschool.myblog.exception.ResourceNotFoundException;
import org.wildcodeschool.myblog.mapper.ArticleMapper;
import org.wildcodeschool.myblog.model.*;
import org.wildcodeschool.myblog.repository.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final AuthorRepository authorRepository;
    private final ArticleAuthorRepository articleAuthorRepository;

    public ArticleService(
            ArticleRepository articleRepository,
            ArticleMapper articleMapper,
            CategoryRepository categoryRepository,
            ImageRepository imageRepository,
            AuthorRepository authorRepository,
            ArticleAuthorRepository articleAuthorRepository) {
        this.articleRepository = articleRepository;
        this.articleMapper = articleMapper;
        this.categoryRepository = categoryRepository;
        this.imageRepository = imageRepository;
        this.authorRepository = authorRepository;
        this.articleAuthorRepository = articleAuthorRepository;
    }

    public List<ArticleDTO> getAllArticles() {
        List<Article> articles = articleRepository.findAll();
        if(articles.isEmpty()) {
            throw new ResourceNotFoundException("Aucun article trouvé !");
        }
        return articles.stream().map(articleMapper::convertToDTO).collect(Collectors.toList());
    }

    public ArticleDTO getArticleById(Long id) {
        Article article = articleRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("L'article avec l'id " + id + " n'a pas été trouvé "));
        return articleMapper.convertToDTO(article);
    }

    public List<ArticleDTO> getArticlesByTitle(String searchTerms){
        List<Article> articles = articleRepository.findByTitle(searchTerms);
        if(articles.isEmpty()){
            throw new ResourceNotFoundException("Aucun article avec ce titre : " + searchTerms);
        }
        return  articles.stream().map(articleMapper::convertToDTO).collect(Collectors.toList());
    }

    public List<ArticleDTO> getArticlesByContent(String searchTerms){
        List<Article> articles = articleRepository.findByContent(searchTerms);
        if(articles.isEmpty()){
            throw new ResourceNotFoundException("Aucun article avec ce contenu : " + searchTerms);
        }
        return  articles.stream().map(articleMapper::convertToDTO).collect(Collectors.toList());
    }

    public List<ArticleDTO> getArticlesCreateAfter(String date){
        LocalDate searchNewDate  = LocalDate.parse(date);
        LocalDateTime newDate = searchNewDate.atStartOfDay();
        List<Article> articles = articleRepository.findByCreatedAtAfter(newDate);
        if(articles.isEmpty()){
            throw new ResourceNotFoundException("Aucun article avec trouvé après la date  : " + date);
        }
        return  articles.stream().map(articleMapper::convertToDTO).collect(Collectors.toList());
    }

    public List<ArticleDTO> getLastFiveArticles(){
        List<Article> articles = articleRepository.findFirst5ByOrderByCreatedAtDesc();
        if(articles.isEmpty()){
            throw new ResourceNotFoundException("Aucun article récent trouvé");
        }
        return articles.stream().map(articleMapper::convertToDTO).collect(Collectors.toList());
    }

    public ArticleDTO createArticle(ArticleCreateDTO articleCreateDTO) {
        Article article = articleMapper.convertToEntity(articleCreateDTO);
        article.setCreatedAt(LocalDateTime.now());
        article.setUpdatedAt(LocalDateTime.now());

        // Gestion de la catégorie
        if (article.getCategory() != null) {
            Category category = categoryRepository.findById(article.getCategory().getId()).orElse(null);
            if (category == null) {
                return null;
            }
            article.setCategory(category);
        }

        // Gestion des images
        if (article.getImages() != null && !article.getImages().isEmpty()) {
            List<Image> validImages = new ArrayList<>();
            for (Image image : article.getImages()) {
                if (image.getId() != null) {
                    Image existingImage = imageRepository.findById(image.getId()).orElse(null);
                    if (existingImage != null) {
                        validImages.add(existingImage);
                    } else {
                        return null;
                    }
                } else {
                    Image savedImage = imageRepository.save(image);
                    validImages.add(savedImage);
                }
            }
            article.setImages(validImages);
        }

        Article savedArticle = articleRepository.save(article);

        // Gestion des auteurs
        if (article.getArticleAuthors() != null) {
            for (ArticleAuthor articleAuthor : article.getArticleAuthors()) {
                Author author = articleAuthor.getAuthor();
                author = authorRepository.findById(author.getId()).orElse(null);
                if (author == null) {
                    return null;
                }

                articleAuthor.setAuthor(author);
                articleAuthor.setArticle(savedArticle);
                articleAuthor.setContribution(articleAuthor.getContribution());

                articleAuthorRepository.save(articleAuthor);
            }
        }

        return articleMapper.convertToDTO(savedArticle);
    }

    public ArticleDTO updateArticle(Long id, Article articleDetails) {
        Article article = articleRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Aucun article trouvé avec l'id : " + id));

        article.setTitle(articleDetails.getTitle());
        article.setContent(articleDetails.getContent());
        article.setUpdatedAt(LocalDateTime.now());

        // Mise à jour de la catégorie
        if (articleDetails.getCategory() != null) {
            Category category = categoryRepository.findById(articleDetails.getCategory().getId())
                    .orElseThrow(()-> new ResourceNotFoundException("La catégorie spécifiée n'existe pas "));
            article.setCategory(category);
        }

        // Mise à jour des images
        if (articleDetails.getImages() != null) {
            List<Image> validImages = new ArrayList<>();
            for (Image image : articleDetails.getImages()) {
                if (image.getId() != null) {
                    Image existingImage = imageRepository.findById(image.getId())
                            .orElseThrow(()-> new ResourceNotFoundException("L'image avec l'id : " + image.getId() + " n'existe pas"));
                        validImages.add(existingImage);
                } else {
                    Image savedImage = imageRepository.save(image);
                    validImages.add(savedImage);
                }
            }
            article.setImages(validImages);
        } else {
            article.getImages().clear();
        }

        // Mise à jour des auteurs
        if (articleDetails.getArticleAuthors() != null) {
            for (ArticleAuthor oldArticleAuthor : article.getArticleAuthors()) {
                articleAuthorRepository.delete(oldArticleAuthor);
            }

            List<ArticleAuthor> updatedArticleAuthors = new ArrayList<>();

            for (ArticleAuthor articleAuthorDetails : articleDetails.getArticleAuthors()) {
                Author author = articleAuthorDetails.getAuthor();
                author = authorRepository.findById(author.getId())
                        .orElseThrow(()-> new ResourceNotFoundException("L'auteur avec l'id : " + articleAuthorDetails.getAuthor().getId() + " n'existe pas"));

                ArticleAuthor newArticleAuthor = new ArticleAuthor();
                newArticleAuthor.setAuthor(author);
                newArticleAuthor.setArticle(article);
                newArticleAuthor.setContribution(articleAuthorDetails.getContribution());

                updatedArticleAuthors.add(newArticleAuthor);
            }

            for (ArticleAuthor articleAuthor : updatedArticleAuthors) {
                articleAuthorRepository.save(articleAuthor);
            }

            article.setArticleAuthors(updatedArticleAuthors);
        }

        Article updatedArticle = articleRepository.save(article);
        return articleMapper.convertToDTO(updatedArticle);
    }

    public boolean deleteArticle(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Impossible de supprimé l'article avec l'id : " + id ));

        articleAuthorRepository.deleteAll(article.getArticleAuthors());
        articleRepository.delete(article);
        return true;
    }
}