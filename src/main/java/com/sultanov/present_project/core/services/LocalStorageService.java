package com.sultanov.present_project.core.services;

import com.sultanov.present_project.core.interfaces.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class LocalStorageService implements StorageService {

    @Value("${storage.root}")
    private String storageRoot;

    @Value("${app.url}")
    private String appUrl;

    @Override
    public String store(MultipartFile file, String path) {
        try {
            Path target = Paths.get(storageRoot).resolve(path);
            Files.createDirectories(target.getParent());
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            return path;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file: " + path, e);
        }
    }

    @Override
    public Resource load(String path) {
        try {
            Path file = Paths.get(storageRoot).resolve(path);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            }
            throw new RuntimeException("File not found: " + path);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load file: " + path, e);
        }
    }

    @Override
    public void delete(String path) {
        try {
            Path file = Paths.get(storageRoot).resolve(path);
            Files.deleteIfExists(file);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file: " + path, e);
        }
    }

    @Override
    public String url(String path) {
        return appUrl + "/storage/" + path;
    }
}
