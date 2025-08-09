package org.example.service;

import org.example.model.dto.ProfileResponse;
import org.example.model.dto.ProfileUpdateRequest;

public interface ProfileService {
    
    ProfileResponse getUserProfile(String userId);
    
    ProfileResponse updateUserProfile(String userId, ProfileUpdateRequest profileUpdateRequest);
} 