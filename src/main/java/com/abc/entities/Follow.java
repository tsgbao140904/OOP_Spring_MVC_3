package com.abc.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "follows")
public class Follow {

    @Id
    @Column(name = "following_user_id")
    private Long followingUserId;

    @Id
    @Column(name = "followed_user_id")
    private Long followedUserId;

    @ManyToOne
    @JoinColumn(name = "following_user_id", insertable = false, updatable = false)
    private User followingUser;

    @ManyToOne
    @JoinColumn(name = "followed_user_id", insertable = false, updatable = false)
    private User followedUser;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Constructor mặc định
    public Follow() {
    }

    // Constructor đầy đủ
    public Follow(Long followingUserId, Long followedUserId, LocalDateTime createdAt) {
        this.followingUserId = followingUserId;
        this.followedUserId = followedUserId;
        this.createdAt = createdAt;
    }

    // Getter và Setter
    public Long getFollowingUserId() {
        return followingUserId;
    }

    public void setFollowingUserId(Long followingUserId) {
        this.followingUserId = followingUserId;
    }

    public User getFollowingUser() {
        return followingUser;
    }

    public void setFollowingUser(User followingUser) {
        this.followingUser = followingUser;
    }

    public Long getFollowedUserId() {
        return followedUserId;
    }

    public void setFollowedUserId(Long followedUserId) {
        this.followedUserId = followedUserId;
    }

    public User getFollowedUser() {
        return followedUser;
    }

    public void setFollowedUser(User followedUser) {
        this.followedUser = followedUser;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}