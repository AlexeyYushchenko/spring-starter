package com.yadev.spring.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageService {

    @Value("${app.image.bucket:C:\\Users\\13YAE\\IdeaProjects\\spring-starter\\images}")
    private final String bucket;

    @SneakyThrows
    public void upload(String imagePath, InputStream content) {
        var fullImagePath = Path.of(bucket, imagePath);
        try (content) {
            Files.createDirectories(fullImagePath.getParent()); //now we will have a directory for the file
            Files.write(fullImagePath, content.readAllBytes(), CREATE, TRUNCATE_EXISTING); //otherwise it can fail as write works only with existing dirs.
        }
    }

    @SneakyThrows
    public Optional<byte[]> download(String imagePath) {
        var fullImagePath = Path.of(bucket, imagePath);

        return Files.exists(fullImagePath)
                ? Optional.of(Files.readAllBytes(fullImagePath) )
                : Optional.empty();
    }
}
