package com.abc.controllers;

import com.abc.entities.User;
import com.abc.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SearchController {

    @Autowired
    private UserService userService;

    @GetMapping("/search")
    public String searchUser(@RequestParam("query") String query, Model model) {
        try {
            List<User> results = userService.searchUsersByUsername(query);
            model.addAttribute("results", results);
            model.addAttribute("keyword", query);
            if (results == null || results.isEmpty()) {
                model.addAttribute("notFound", true);
            }
        } catch (Exception e) {
            model.addAttribute("error", "Không thể tìm kiếm người dùng");
        }
        return "searchResult";
    }
}