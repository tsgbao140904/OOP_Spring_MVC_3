package com.abc.services;

import java.util.List;
import com.abc.entities.User;

public interface UserService {
    User getUserByUserName(String userName);
    boolean registerUser(User user);
    List<User> searchUsersByUsername(String keyword);
    boolean isEmailValid(String email);
    boolean isAgeValid(java.time.LocalDate dateOfBirth);
    void saveUser(User user);
    void updateUser(User user);
    User getUserById(Long id);
    List<User> findAll();
    Long getUserFollowersCount(Long userId);
    Long getUserFollowedCount(Long userId);
}