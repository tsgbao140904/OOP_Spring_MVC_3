package com.abc.dao;

import com.abc.entities.Post;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class PostDAO {

    @Autowired
    private SessionFactory sessionFactory;

    public List<Post> getALLPost(Long id) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "SELECT DISTINCT p FROM Post p " +
                        "JOIN p.user u " +
                        "LEFT JOIN Follow f ON p.userId = f.followedUserId " +
                        "WHERE f.followingUserId = :id OR p.userId = :id " +
                        "ORDER BY p.createdAt DESC";
            Query<Post> query = session.createQuery(hql, Post.class);
            query.setParameter("id", id);
            return query.list();
        }
    }

    public List<Post> getPostById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Query<Post> query = session.createQuery("FROM Post WHERE userId = :id ORDER BY createdAt DESC", Post.class);
            query.setParameter("id", id);
            return query.list();
        }
    }

    public boolean createdPost(Post post) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            post.setCreatedAt(LocalDateTime.now());
            session.save(post);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}