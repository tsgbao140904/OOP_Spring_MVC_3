package com.abc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.abc.entities.User;
import com.abc.entities.Place;
import com.abc.services.PlaceService;
import com.abc.services.UserService;

import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
public class AuthController {

    private UserService userService;

    @Autowired
    private PlaceService placeService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password, HttpSession session, Model model) {
        try {
            User user = userService.getUserByUserName(username);
            if (user != null && user.getPassword().equals(password)) {
                session.setAttribute("user", user);
                session.setAttribute("user_id", user.getId());
                return "redirect:/";
            } else {
                model.addAttribute("error", "Sai tên đăng nhập hoặc mật khẩu");
                return "login";
            }
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "login";
        }
    }

    
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("places", placeService.getAllPlaces());
        return "register";
    }

    @PostMapping(value = "/register", consumes = "multipart/form-data")
    public String registerUser(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("email") String email,
            @RequestParam("dateOfBirth") String dateOfBirth,
            @RequestParam("placeId") Long placeId,
            @RequestParam("avatarFile") MultipartFile avatarFile,
            Model model) {
        System.out.println("Received registration request: username=" + username + ", email=" + email);
        try {
            // Kiểm tra email hợp lệ
            if (!userService.isEmailValid(email)) {
                throw new IllegalArgumentException("Email không hợp lệ hoặc đã tồn tại");
            }

            // Kiểm tra tuổi
            LocalDate dob = LocalDate.parse(dateOfBirth, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            if (!userService.isAgeValid(dob)) {
                throw new IllegalArgumentException("Người dùng phải trên 15 tuổi");
            }

            // Tạo user mới
            User user = new User(username, password);
            user.setEmail(email);
            user.setDateOfBirth(dob);

            // Xử lý nơi ở
            Place place = new Place();
            place.setId(placeId);
            user.setPlace(place);

            // Xử lý upload avatar
            if (!avatarFile.isEmpty()) {
                System.out.println("Uploading avatar: " + avatarFile.getOriginalFilename());
                validateAvatar(avatarFile);
                String fileName = System.currentTimeMillis() + "_" + avatarFile.getOriginalFilename();
                Path path = Paths.get("src/main/webapp/uploads/" + fileName);
                Files.createDirectories(path.getParent());
                avatarFile.transferTo(path);
                user.setAvatar("/uploads/" + fileName);
            }

            if (userService.registerUser(user)) {
                return "redirect:/login";
            } else {
                model.addAttribute("error", "Đăng ký thất bại, có thể tên người dùng đã tồn tại");
                model.addAttribute("places", placeService.getAllPlaces());
                return "register";
            }
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("places", placeService.getAllPlaces());
            return "register";
        } catch (IOException e) {
            model.addAttribute("error", "Lỗi khi upload file avatar: " + e.getMessage());
            model.addAttribute("places", placeService.getAllPlaces());
            return "register";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    private void validateAvatar(MultipartFile file) throws IllegalArgumentException {
        // Kiểm tra định dạng
        String fileName = file.getOriginalFilename();
        if (fileName == null || !(fileName.endsWith(".jpg") || fileName.endsWith(".png"))) {
            throw new IllegalArgumentException("Chỉ chấp nhận file jpg hoặc png!");
        }

        // Kiểm tra dung lượng (200KB = 200 * 1024 bytes)
        if (file.getSize() > 200 * 1024) {
            throw new IllegalArgumentException("Dung lượng file không được vượt quá 200KB!");
        }
    }
}