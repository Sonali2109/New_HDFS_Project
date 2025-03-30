package com.example.hdfs.repository;

import com.example.hdfs.dto.User;
import java.util.HashMap;
import java.util.Map;

public class UserManager {
    private Map<String, User> users = new HashMap<>();

    public User getUser(String username) throws Exception {
        if (!users.containsKey(username)) {
            users.put(username, new User(username));
        }
        return users.get(username);
    }

    public boolean authenticate(String username, String password) {
        // Implement your authentication logic here
        return "admin".equals(username) && "password123".equals(password);
    }
}

