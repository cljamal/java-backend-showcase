package com.sultanov.present_project.features.profile.actions;

import com.sultanov.present_project.core.exceptions.ResourceNotFoundException;
import com.sultanov.present_project.core.interfaces.StorageService;
import com.sultanov.present_project.features.profile.dto.AvatarResource;
import com.sultanov.present_project.features.users.models.User;
import com.sultanov.present_project.features.users.models.UserMedia;
import com.sultanov.present_project.features.users.repositories.UserMediaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ValidationException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileAvatarActions {

    private static final String COLLECTION = "avatars";
    private static final String CACHE_KEY_PREFIX = "avatar:default:";
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final Set<String> ALLOWED_MIME_TYPES = Set.of(
            "image/jpeg", "image/png", "image/webp", "image/gif"
    );

    private final UserMediaRepository userMediaRepository;
    private final StorageService storageService;
    private final RedisTemplate<String, String> redisTemplate;

    public List<AvatarResource> getCollection(User user) {
        return userMediaRepository
                .findByModelIdAndCollectionName(user.getId(), COLLECTION)
                .stream()
                .map(this::toResource)
                .toList();
    }

    @Transactional
    public AvatarResource upload(User user, MultipartFile file) {
        if (file.getSize() > MAX_FILE_SIZE)
            throw new ValidationException("File too large");

        if (!ALLOWED_MIME_TYPES.contains(file.getContentType()))
            throw new ValidationException("Invalid file type");

        String ext = getExtension(file.getOriginalFilename());
        String fileName = UUID.randomUUID() + "." + ext;
        String path = buildPath(user.getId(), COLLECTION, fileName);

        storageService.store(file, path);

        // Снимаем дефолт с предыдущей аватарки
        userMediaRepository.findByModelIdAndCollectionName(user.getId(), COLLECTION)
                .forEach(m -> m.getCustomProperties().put("is_default", false));

        UserMedia media = new UserMedia();
        media.setModelId(user.getId());
        media.setCollectionName(COLLECTION);
        media.setName(file.getOriginalFilename());
        media.setFileName(fileName);
        media.setMimeType(file.getContentType());
        media.setDisk("local");
        media.setSize(file.getSize());
        media.setCustomProperties(new java.util.HashMap<>(Map.of("is_default", true)));

        userMediaRepository.save(media);

        // Обновляем кэш
        redisTemplate.opsForValue().set(CACHE_KEY_PREFIX + user.getId(), storageService.url(path));

        return toResource(media);
    }

    @Transactional
    public AvatarResource setDefault(User user, UUID id) {
        UserMedia target = userMediaRepository.findById(id)
                .filter(m -> m.getModelId().equals(user.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("Avatar not found"));

        List<UserMedia> all = userMediaRepository.findByModelIdAndCollectionName(user.getId(), COLLECTION);
        all.forEach(m -> m.getCustomProperties().put("is_default", false));
        userMediaRepository.saveAll(all);

        target.getCustomProperties().put("is_default", true);
        userMediaRepository.save(target);

        redisTemplate.opsForValue().set(CACHE_KEY_PREFIX + user.getId(), storageService.url(buildPath(target)));

        return toResource(target);
    }

    @Transactional
    public void unsetDefault(User user) {
        List<UserMedia> all = userMediaRepository.findByModelIdAndCollectionName(user.getId(), COLLECTION);
        all.forEach(m -> m.getCustomProperties().put("is_default", false));
        userMediaRepository.saveAll(all);

        redisTemplate.delete(CACHE_KEY_PREFIX + user.getId());
    }

    @Transactional
    public boolean delete(User user, UUID id) {
        UserMedia media = userMediaRepository.findById(id)
                .filter(m -> m.getModelId().equals(user.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("Avatar not found"));

        boolean wasDefault = Boolean.TRUE.equals(media.getCustomProperties().get("is_default"));

        storageService.delete(buildPath(media));
        userMediaRepository.delete(media);

        if (wasDefault) {
            redisTemplate.delete(CACHE_KEY_PREFIX + user.getId());
        }

        return wasDefault;
    }

    public String getDefaultUrl(User user) {
        String cached = redisTemplate.opsForValue().get(CACHE_KEY_PREFIX + user.getId());
        if (cached != null) return cached;

        return userMediaRepository.findByModelIdAndCollectionName(user.getId(), COLLECTION)
                .stream()
                .filter(m -> Boolean.TRUE.equals(m.getCustomProperties().get("is_default")))
                .findFirst()
                .map(m -> {
                    String url = storageService.url(buildPath(m));
                    redisTemplate.opsForValue().set(CACHE_KEY_PREFIX + user.getId(), url);
                    return url;
                })
                .orElse(null);
    }

    private AvatarResource toResource(UserMedia media) {
        return new AvatarResource(
                media.getId(),
                storageService.url(buildPath(media)),
                media.getFileName(),
                media.getMimeType(),
                media.getSize(),
                Boolean.TRUE.equals(media.getCustomProperties().get("is_default"))
        );
    }

    private String buildPath(UserMedia media) {
        return buildPath(media.getModelId(), media.getCollectionName(), media.getFileName());
    }

    private String buildPath(Long modelId, String collection, String fileName) {
        return "users/" + modelId + "/" + collection + "/" + fileName;
    }

    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) return "bin";
        return filename.substring(filename.lastIndexOf('.') + 1);
    }
}