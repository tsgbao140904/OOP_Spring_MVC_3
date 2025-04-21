package com.abc.services;

import java.util.List;
import com.abc.entities.User;

public interface FollowService {
    List<User> getUserFollower(Long id);
    List<User> getUserFollowed(Long id);
    List<User> getSuggestFollow(Long id);
    void followUser(Long followingUserId, Long followedUserId);
    void unfollowUser(Long followingUserId, Long followedUserId);
}