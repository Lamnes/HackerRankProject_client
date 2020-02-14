package com.client.service;

import com.client.exception.ServiceException;
import com.client.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class UserService {
    @Value("${rest.server}")
    private String restUrl;

    @Autowired
    private RestTemplate restTemplate;

    public List<User> getAllUsers() {
        ResponseEntity<List<User>> response = restTemplate.exchange(
                restUrl + "/restUserList/",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<User>>() {
                });
        List<User> users = response.getBody();
        return users;
    }

    public void removeUser(long id) {
        User foundUser = getUserById(id);
        if (foundUser == null) {
            throw new ServiceException("User to remove NOT found");
        }

        String fooResourceUrl
                = restUrl + "/restUserRemove";
        ResponseEntity<String> response = restTemplate.getForEntity(fooResourceUrl + "?id=" + id, String.class);
    }

    public User getUserById(long id) {
        String fooResourceUrl = restUrl + "/restUser/";
        ResponseEntity<User> response = restTemplate.getForEntity(fooResourceUrl + "?id=" + id, User.class);
        User user = response.getBody();
        return user;
    }

    public User getUserByName(String name) {
        String fooResourceUrl = restUrl + "/restUser/";
        ResponseEntity<User> response = restTemplate.getForEntity(fooResourceUrl + "?name=" + name, User.class);
        User user = response.getBody();
        return user;
    }

    public void updateUser(User user) {
        String fooResourceUrl = restUrl + "/restUserSave/";
        HttpEntity<User> request = new HttpEntity<>(user);
        user = restTemplate.postForObject(fooResourceUrl, request, User.class);
    }

    public void createUser(User user) {
        String fooResourceUrl = restUrl + "/restUserSave/";
        HttpEntity<User> request = new HttpEntity<>(user);
        user = restTemplate.postForObject(fooResourceUrl, request, User.class);
    }
}