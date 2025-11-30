package com.spring.memory.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.spring.memory.exception.CloudinaryUploadImageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
public class CloudinaryService implements FileStorageService {
    private final Cloudinary cloudinary;

    public CloudinaryService(
            @Value("${cloudinary.cloud_name:}") String cloudName,
            @Value("${cloudinary.api_key:}") String apiKey,
            @Value("${cloudinary.api_secret:}") String apiSecret) {

        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret));
    }

    @Override
    public FileStorageService.UploadResponse upload(MultipartFile file) {
        try {
            Map<String, Object> result = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap("folder", "memory"));

            String secure = (String) result.get("secure_url");
            String publicId = (String) result.get("public_id");

            return new FileStorageService.UploadResponse(secure, publicId);

        } catch (Exception e) {
            String msg = e.getMessage() != null ? e.getMessage() : "unknown error";
            throw new CloudinaryUploadImageException("Cloudinary upload failed: " + msg);
        }
    }

    @Override
    public boolean delete(String publicId) {
        if (publicId == null || publicId.isBlank())
            return false;
        try {
            Map<String, Object> result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            return "ok".equals(result.get("result"));
        } catch (Exception e) {
            String msg = e.getMessage() != null ? e.getMessage() : "unknown error";
            throw new RuntimeException("Cloudinary delete failed: " + msg);
        }
    }
}
