package com.abc.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.abc.dao.FollowDAO;
import com.abc.entities.User;

@Service
public class FollowServiceImpl implements FollowService {

    @Autowired
    private FollowDAO followDAO;

    @Override
    public List<User> getUserFollower(Long id) {
        return followDAO.getFollowerUser(id);
    }

    @Override
    public List<User> getUserFollowed(Long id) {
        return followDAO.getFollowedUsers(id);
    }

    @Override
    public List<User> getSuggestFollow(Long id) {
        return followDAO.getSuggestedFollows(id);
    }

    @Override
    public void followUser(Long followingUserId, Long followedUserId) {
        followDAO.followUser(followingUserId, followedUserId);
    }

    @Override
    public void unfollowUser(Long followingUserId, Long followedUserId) {
        followDAO.unfollowUser(followingUserId, followedUserId);
    }
}