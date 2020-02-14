package com.client.service;

import com.client.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class UserRoleService {
    @Value("${rest.server}")
    private String restUrl;

    @Autowired
    private RestTemplate restTemplate;

    public List<UserRole> getAllUserRoles() {
        ResponseEntity<List<UserRole>> response = restTemplate.exchange(
                restUrl + "/restUserRoles/",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<UserRole>>() {
                });
        List<UserRole> userRoles = response.getBody();
        return userRoles;
    }
}