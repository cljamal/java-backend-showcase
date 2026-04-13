package com.sultanov.present_project.core.interfaces;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    String store(MultipartFile file, String path);
    Resource load(String path);
    void delete(String path);
    String url(String path);
}
