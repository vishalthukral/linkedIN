package com.linkedin_clone_application.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class CloudinaryService {
    //     injecting cloudinary dependency that will configure in CloudinaryConfiguration
//     this help us to interact with cloudinary to add, update and delete images
    @Autowired
    private Cloudinary cloudinary;

    public CloudinaryService(@Value("${cloudinary.cloud-name}") String cloudName,
                             @Value("${cloudinary.api-key}") String apiKey,
                             @Value("${cloudinary.api-secret}") String apiSecret) {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret));
    }
    //     Upload Image
//     it will upload the image on cloudinary and get the url
    public String uploadImage(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null; // No file uploaded
        }
        //get.bytes() means cloudinary internally save image into bytes form
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        return uploadResult.get("url").toString();
    }
    //for posts
    public String uploadFile(MultipartFile file) {
        try {
            Map<String, Object> options = new HashMap<>();

            // Check if it's a document (PDF, TXT, etc.)
            if (file.getContentType().equals("application/pdf") ||
                    file.getContentType().equals("text/plain") ||
                    file.getContentType().startsWith("application/")) {
                options.put("resource_type", "raw"); // For documents
            } else if (file.getContentType().startsWith("video/")) {
                options.put("resource_type", "video"); // For videos
            } else {
                options.put("resource_type", "image"); // Default to images
            }

            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), options);
            return uploadResult.get("secure_url").toString(); // Return Cloudinary URL
        } catch (Exception e) {
            throw new RuntimeException("Upload failed: " + e.getMessage());
        }
    }

    // Update Image
    // As cloudinary didn't overwrite, it will check is old image exists or not, it will delete old image first
    public String updateImage(String oldImageUrl, MultipartFile newFile) throws IOException {
        if (oldImageUrl != null) {
            deleteImage(oldImageUrl); // Delete the old image first
        }
        return uploadImage(newFile); // Upload new image
    }
    // Delete Image
    // get the id from the providedURL, and delete it and return true
    public boolean deleteImage(String imageUrl) throws IOException {
        if (imageUrl == null || imageUrl.isEmpty()) {
            return false;
        }
        String publicId = extractPublicId(imageUrl);
        Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        return "ok".equals(result.get("result")); // Check if deletion was successful
    }
    // Helper method to extract Cloudinary public ID from image URL
    private String extractPublicId(String imageUrl) {
        String[] parts = imageUrl.split("/");
        return parts[parts.length - 1].split("\\.")[0]; // Extract the image ID before file extension
    }
}