package ru.gknsv.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gknsv.model.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, String> {
    Image findImageById(String id);
}
