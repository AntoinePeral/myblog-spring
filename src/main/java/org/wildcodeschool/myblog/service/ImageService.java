package org.wildcodeschool.myblog.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.wildcodeschool.myblog.dto.ImageDTO;
import org.wildcodeschool.myblog.exception.ResourceNotFoundException;
import org.wildcodeschool.myblog.mapper.ImageMapper;
import org.wildcodeschool.myblog.model.Image;
import org.wildcodeschool.myblog.repository.ArticleRepository;
import org.wildcodeschool.myblog.repository.ImageRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImageService {
    private final ImageRepository imageRepository;
    private final ArticleRepository articleRepository;
    private final ImageMapper imageMapper;

    public ImageService(ImageRepository imageRepository, ArticleRepository articleRepository, ImageMapper imageMapper) {
        this.imageRepository = imageRepository;
        this.articleRepository = articleRepository;
        this.imageMapper = imageMapper;
    }

    public List<ImageDTO> getAllImages() {
        List<Image> images = imageRepository.findAll();
        if (images.isEmpty()) {
            throw new RuntimeException("Aucune images trouvée");
        }
        return images.stream().map(imageMapper::convertToDTO).collect(Collectors.toList());
    }


    public ImageDTO getImageById(Long id) {
        Image image = imageRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Aucune image avec l'id : " + id + " n'a été trouvé"));
        return imageMapper.convertToDTO(image);
    }

    public ImageDTO createImage(Image image) {
        Image savedImage = imageRepository.save(image);
        return imageMapper.convertToDTO(savedImage);
    }

    public ImageDTO updateImage( Long id,  Image imageDetails) {
        Image image = imageRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Aucune image avec l'id : " + id + " n'a été trouvé"));
        image.setUrl(imageDetails.getUrl());
        image.setArticles(imageDetails.getArticles());
        Image updatedImage = imageRepository.save(image);
        return imageMapper.convertToDTO(updatedImage);
    }

    public boolean deleteImage(@PathVariable Long id) {
        Image image = imageRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Aucune image avec l'id : " + id + " n'a été trouvé"));
        imageRepository.delete(image);
        return true;
    }
}
