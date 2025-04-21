package com.abc.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.abc.entities.Post;
import com.abc.entities.User;
import com.abc.services.PlaceService;
import com.abc.services.PostService;
import com.abc.services.UserService;

import jakarta.servlet.http.HttpSession;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@Controller
public class UserController {

    private final UserService userService;
    private final PostService postService;

    @Autowired
    public UserController(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    @Autowired
    private PlaceService placeService;

    @GetMapping("/profile")
    public String profileUser(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        try {
            List<Post> posts = postService.getPostById(user.getId());
            model.addAttribute("user", user);
            model.addAttribute("posts", posts);
            model.addAttribute("followerCount", userService.getUserFollowersCount(user.getId()));
            model.addAttribute("followedCount", userService.getUserFollowedCount(user.getId()));
        } catch (Exception e) {
            model.addAttribute("error", "Không thể tải hồ sơ người dùng");
        }

        return "profile";
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        try {
            List<User> users = userService.findAll();
            model.addAttribute("users", users);
            return "user/list";
        } catch (Exception e) {
            model.addAttribute("error", "Không thể tải danh sách người dùng");
            return "user/list";
        }
    }

    @GetMapping("/users/add")
    public String showAddForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("places", placeService.getAllPlaces());
        model.addAttribute("error", "Chức năng thêm người dùng chưa được hỗ trợ qua giao diện web.");
        return "redirect:/users";
    }

    @PostMapping("/users/add")
    public String addUser(
            @ModelAttribute User user,
            @RequestParam(value = "avatarBase64", required = false) String avatarBase64,
            Model model) {
        try {
            // Kiểm tra email hợp lệ
            if (!userService.isEmailValid(user.getEmail())) {
                throw new Exception("Email không hợp lệ hoặc đã tồn tại!");
            }

            // Kiểm tra tuổi
            if (!userService.isAgeValid(user.getDateOfBirth())) {
                throw new Exception("Người dùng phải trên 15 tuổi!");
            }

            // Xử lý avatar từ Base64
            if (avatarBase64 != null && !avatarBase64.isEmpty()) {
                // Loại bỏ phần header của Base64 (ví dụ: "data:image/jpeg;base64,")
                String base64Data = avatarBase64.split(",")[1];
                byte[] decodedBytes = Base64.getDecoder().decode(base64Data);

                // Kiểm tra dung lượng
                if (decodedBytes.length > 200 * 1024) { // 200KB
                    throw new Exception("Dung lượng file không được vượt quá 200KB!");
                }

                // Kiểm tra định dạng (dựa vào header của Base64)
                if (!avatarBase64.startsWith("data:image/jpeg") && !avatarBase64.startsWith("data:image/png")) {
                    throw new Exception("Chỉ chấp nhận file jpg hoặc png!");
                }

                // Lưu file
                String fileName = System.currentTimeMillis() + (avatarBase64.startsWith("data:image/jpeg") ? ".jpg" : ".png");
                Path path = Paths.get("src/main/webapp/uploads/" + fileName);
                Files.createDirectories(path.getParent());
                try (FileOutputStream fos = new FileOutputStream(path.toFile())) {
                    fos.write(decodedBytes);
                }
                user.setAvatar("/uploads/" + fileName);
            }

            userService.saveUser(user);
            return "redirect:/users?success";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("places", placeService.getAllPlaces());
            return "redirect:/users";
        }
    }

    @GetMapping("/users/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        User user = userService.getUserById(id);
        if (user == null) {
            return "redirect:/users?error=UserNotFound";
        }
        model.addAttribute("user", user);
        model.addAttribute("places", placeService.getAllPlaces());
        return "user/edit"; // Trả về view user/edit.jsp
    }
    
    @PostMapping("/users/edit/{id}")
    public String editUser(
            @PathVariable("id") Long id,
            @ModelAttribute User user,
            @RequestParam(value = "avatarBase64", required = false) String avatarBase64,
            Model model) {
        try {
            User existingUser = userService.getUserById(id);
            if (existingUser == null) {
                return "redirect:/users?error=UserNotFound";
            }

            // Kiểm tra email hợp lệ
            if (!user.getEmail().equals(existingUser.getEmail()) && !userService.isEmailValid(user.getEmail())) {
                throw new Exception("Email không hợp lệ hoặc đã tồn tại!");
            }

            // Kiểm tra tuổi
            if (!userService.isAgeValid(user.getDateOfBirth())) {
                throw new Exception("Người dùng phải trên 15 tuổi!");
            }

            // Cập nhật thông tin
            existingUser.setUsername(user.getUsername());
            existingUser.setEmail(user.getEmail());
            existingUser.setDateOfBirth(user.getDateOfBirth());
            existingUser.setPlace(user.getPlace());

            // Xử lý avatar từ Base64 (nếu có)
            if (avatarBase64 != null && !avatarBase64.isEmpty()) {
                // Loại bỏ phần header của Base64 (ví dụ: "data:image/jpeg;base64,")
                String base64Data = avatarBase64.split(",")[1];
                byte[] decodedBytes = Base64.getDecoder().decode(base64Data);

                // Kiểm tra dung lượng
                if (decodedBytes.length > 200 * 1024) { // 200KB
                    throw new Exception("Dung lượng file không được vượt quá 200KB!");
                }

                // Kiểm tra định dạng
                if (!avatarBase64.startsWith("data:image/jpeg") && !avatarBase64.startsWith("data:image/png")) {
                    throw new Exception("Chỉ chấp nhận file jpg hoặc png!");
                }

                // Lưu file
                String fileName = System.currentTimeMillis() + (avatarBase64.startsWith("data:image/jpeg") ? ".jpg" : ".png");
                Path path = Paths.get("src/main/webapp/uploads/" + fileName);
                Files.createDirectories(path.getParent());
                try (FileOutputStream fos = new FileOutputStream(path.toFile())) {
                    fos.write(decodedBytes);
                }
                user.setAvatar("/uploads/" + fileName);
            }

            userService.updateUser(existingUser);
            return "redirect:/users?success";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("places", placeService.getAllPlaces());
            return "redirect:/users";
        }
    }
}