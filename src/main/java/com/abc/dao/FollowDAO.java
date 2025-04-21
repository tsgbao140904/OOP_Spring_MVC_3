package com.abc.dao;

import com.abc.entities.Follow;
import com.abc.entities.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class FollowDAO {

    @Autowired
    private SessionFactory sessionFactory;

    public List<User> getFollowerUser(Long id) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "SELECT u FROM User u " +
                        "JOIN Follow f ON u.id = f.followingUserId " +
                        "WHERE f.followedUserId = :id";
            Query<User> query = session.createQuery(hql, User.class);
            query.setParameter("id", id);
            return query.list();
        }
    }

    public List<User> getFollowedUsers(Long id) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "SELECT u FROM User u " +
                        "JOIN Follow f ON u.id = f.followedUserId " +
                        "WHERE f.followingUserId = :id";
            Query<User> query = session.createQuery(hql, User.class);
            query.setParameter("id", id);
            return query.list();
        }
    }

    public void followUser(Long followingUserId, Long followedUserId) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Follow follow = new Follow(followingUserId, followedUserId, LocalDateTime.now());
            session.save(follow);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void unfollowUser(Long followingUserId, Long followedUserId) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query query = session.createQuery("DELETE FROM Follow WHERE followingUserId = :followingUserId AND followedUserId = :followedUserId");
            query.setParameter("followingUserId", followingUserId);
            query.setParameter("followedUserId", followedUserId);
            query.executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<User> getSuggestedFollows(Long userId) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "SELECT u FROM User u " +
                        "LEFT JOIN Follow f ON u.id = f.followedUserId AND f.followingUserId = :userId " +
                        "WHERE f.followedUserId IS NULL AND u.id <> :userId";
            Query<User> query = session.createQuery(hql, User.class);
            query.setParameter("userId", userId);
            return query.list();
        }
    }
}