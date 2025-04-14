package org.wildcodeschool.myblog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wildcodeschool.myblog.model.Image;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findByUrl(String url);
}
