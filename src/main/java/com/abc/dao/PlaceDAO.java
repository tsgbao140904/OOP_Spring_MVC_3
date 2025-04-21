package com.abc.dao;

import com.abc.entities.Place;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PlaceDAO {

    @Autowired
    private SessionFactory sessionFactory;

    public List<Place> getAllPlaces() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Place", Place.class).list();
        }
    }
}