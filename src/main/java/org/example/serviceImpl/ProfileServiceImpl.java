package org.example.serviceImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.User;
import org.example.model.dto.ProfileResponse;
import org.example.model.dto.ProfileUpdateRequest;
import org.example.repo.UserRepository;
import org.example.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public ProfileResponse getUserProfile(String userId) {
        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));

        Map<String, Object> metadataMap = parseMetadata(user.getMetadata());

        return new ProfileResponse(
                user.getId().toString(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getCollege(),
                metadataMap,
                user.getGender(),
                "Profile retrieved successfully"
        );
    }

    @Override
    public ProfileResponse updateUserProfile(String userId, ProfileUpdateRequest profileUpdateRequest) {
        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (profileUpdateRequest.getName() != null && !profileUpdateRequest.getName().trim().isEmpty()) {
            user.setName(profileUpdateRequest.getName());
        }
        if (profileUpdateRequest.getPhone() != null && !profileUpdateRequest.getPhone().trim().isEmpty()) {
            user.setPhone(profileUpdateRequest.getPhone());
        }
        if (profileUpdateRequest.getCollege() != null) {
            user.setCollege(profileUpdateRequest.getCollege());
        }
        if (profileUpdateRequest.getMetadata() != null) {
            String metadataJson = convertMetadataToJson(profileUpdateRequest.getMetadata());
            user.setMetadata(metadataJson);
        }
        if (profileUpdateRequest.getGender() != null) {
            user.setGender(profileUpdateRequest.getGender());
        }

        User updatedUser = userRepository.save(user);

        Map<String, Object> metadataMap = parseMetadata(updatedUser.getMetadata());

        return new ProfileResponse(
                updatedUser.getId().toString(),
                updatedUser.getName(),
                updatedUser.getEmail(),
                updatedUser.getPhone(),
                updatedUser.getCollege(),
                metadataMap,
                updatedUser.getGender(),
                "Profile updated successfully"
        );
    }

    private Map<String, Object> parseMetadata(String metadataJson) {
        try {
            if (metadataJson == null || metadataJson.trim().isEmpty() || metadataJson.equals("{}")) {
                return new HashMap<>();
            }
            return objectMapper.readValue(metadataJson, new TypeReference<Map<String, Object>>() {});
        } catch (JsonProcessingException e) {
            return new HashMap<>();
        }
    }

    private String convertMetadataToJson(Map<String, Object> metadata) {
        try {
            if (metadata == null || metadata.isEmpty()) {
                return "{}";
            }
            return objectMapper.writeValueAsString(metadata);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }
} 