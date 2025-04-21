package com.abc.services;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abc.dao.UserDAO;
import com.abc.entities.User;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private FollowService followService;

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
    );

    @Override
    public User getUserByUserName(String userName) {
        if (userName == null || userName.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên người dùng không được rỗng");
        }
        return userDAO.getUserByUserName(userName);
    }

    @Override
    public boolean registerUser(User user) {
        if (user == null || user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên người dùng không được rỗng");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Mật khẩu không được rỗng");
        }
        return userDAO.registerUser(user);
    }

    @Override
    public boolean isEmailValid(String email) {
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            return false;
        }
        return userDAO.getUserByEmail(email) == null;
    }

    @Override
    public boolean isAgeValid(LocalDate dateOfBirth) {
        if (dateOfBirth == null) {
            return false;
        }
        return Period.between(dateOfBirth, LocalDate.now()).getYears() >= 15;
    }

    @Override
    public void saveUser(User user) {
        userDAO.save(user);
    }

    @Override
    public void updateUser(User user) {
        userDAO.update(user);
    }

    @Override
    public User getUserById(Long id) {
        return userDAO.getUserById(id);
    }

    @Override
    public List<User> searchUsersByUsername(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new IllegalArgumentException("Từ khóa tìm kiếm không được rỗng");
        }
        return userDAO.searchUsersByUsername(keyword);
    }

    @Override
    public List<User> findAll() {
        return userDAO.findAll();
    }

    @Override
    public Long getUserFollowersCount(Long userId) {
        List<User> followers = followService.getUserFollower(userId);
        return (long) followers.size();
    }

    @Override
    public Long getUserFollowedCount(Long userId) {
        List<User> followed = followService.getUserFollowed(userId);
        return (long) followed.size();
    }
}