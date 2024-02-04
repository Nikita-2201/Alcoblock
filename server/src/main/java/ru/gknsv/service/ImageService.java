package ru.gknsv.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.gknsv.model.Image;
import ru.gknsv.repository.ImageRepository;

@Service
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;

    public Image getImageForIcon(String imageId) {
        return imageRepository.findImageById(imageId);
    }
}
