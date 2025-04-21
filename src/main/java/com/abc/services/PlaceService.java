package com.abc.services;

import com.abc.dao.PlaceDAO;
import com.abc.entities.Place;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaceService {

    @Autowired
    private PlaceDAO placeDAO;

    public List<Place> getAllPlaces() {
        return placeDAO.getAllPlaces();
    }
}