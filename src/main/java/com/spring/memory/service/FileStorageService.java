package com.spring.memory.service;


import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    class UploadResponse {
        private final String url;
        private final String publicId;

        public UploadResponse(String url, String publicId) {
            this.url = url;
            this.publicId = publicId;
        }
        public String getUrl() {
            return url;
        }
        public String getPublicId() {
            return publicId;
        }
    }

    UploadResponse upload(MultipartFile file);

    /**
     * Delete the file by public id (provider-specific id).
     * Returns true if deleted/acknowledged.
     */
    boolean delete(String publicId);
}
