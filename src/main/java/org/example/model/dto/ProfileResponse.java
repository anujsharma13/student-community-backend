package org.example.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponse {
    private String id;
    private String name;
    private String email;
    private String phone;
    private String college;
    private Map<String, Object> metadata;
    private String gender;
    private String message;
    
    public ProfileResponse(String id, String name, String email, String phone, String college, Map<String, Object> metadata, String gender) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.college = college;
        this.metadata = metadata;
        this.gender = gender;
    }
} 