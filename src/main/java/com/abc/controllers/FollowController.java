package com.abc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.abc.services.FollowService;

@Controller
@RequestMapping("/follow")
public class FollowController {

    private FollowService followService;

    @Autowired
    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    @PostMapping("/add")
    @ResponseBody
    public String followUser(@RequestParam("followingUserId") Long followingUserId, @RequestParam("followedUserId") Long followedUserId) {
        try {
            followService.followUser(followingUserId, followedUserId);
            return "Followed successfully!";
        } catch (IllegalArgumentException e) {
            return "Error: " + e.getMessage();
        } catch (Exception e) {
            return "Error: Không thể theo dõi người dùng";
        }
    }

    @PostMapping("/remove")
    @ResponseBody
    public String unfollowUser(@RequestParam("followingUserId") Long followingUserId, @RequestParam("followedUserId") Long followedUserId) {
        try {
            followService.unfollowUser(followingUserId, followedUserId);
            return "Unfollowed successfully!";
        } catch (IllegalArgumentException e) {
            return "Error: " + e.getMessage();
        } catch (Exception e) {
            return "Error: Không thể bỏ theo dõi người dùng";
        }
    }
}