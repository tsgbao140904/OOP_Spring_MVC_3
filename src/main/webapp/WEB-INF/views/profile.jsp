<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.abc.entities.*" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Trang Cá Nhân</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
        <div class="container-fluid">
            <a class="navbar-brand" href="<%= request.getContextPath() %>/">🏠 Trang chủ</a>
            <div class="ms-auto">
                <a href="<%= request.getContextPath() %>/logout" class="btn btn-outline-light">Đăng xuất</a>
            </div>
        </div>
    </nav>

    <div class="container mt-2">
        <div class="row justify-content-center">
            <div class="col-md-3">
                <div class="card p-3 mb-3">
                    <p class="fw-bold">Danh sách người theo dõi</p>
                    <ul class="list-unstyled">
                        <%
                        List<User> followers = (List<User>) request.getAttribute("followers");
                        if (followers != null && !followers.isEmpty()) {
                            for (User follower : followers) {
                        %>
                        <li class="mb-2">
                            <img src="<%= follower.getAvatar() != null ? request.getContextPath() + follower.getAvatar() : request.getContextPath() + "/resources/images/avt1.jpg" %>"
                                 alt="Avatar" class="rounded-circle me-2" width="30">
                            <%= follower.getUsername() %>
                        </li>
                        <%
                            }
                        } else {
                        %>
                        <p class="text-muted">Chưa có người theo dõi.</p>
                        <%
                        }
                        %>
                    </ul>
                </div>
            </div>
            <div class="col-md-6 mt-0">
                <div class="card mb-3 text-center">
                    <% User user = (User) request.getAttribute("user"); %>
                    <div class="card-body">
                        <img src="<%= user.getAvatar() != null ? request.getContextPath() + user.getAvatar() : request.getContextPath() + "/resources/images/avt.jpg" %>"
                             alt="Avatar" class="rounded-circle mb-3" width="150" height="150">
                        <h4 class="card-title"><%= user.getUsername() %></h4>
                        <p><strong>Email:</strong> <%= user.getEmail() != null ? user.getEmail() : "Chưa cập nhật" %></p>
                        <p><strong>Ngày sinh:</strong> <%= user.getDateOfBirth() != null ? user.getDateOfBirth() : "Chưa cập nhật" %></p>
                        <p><strong>Nơi ở:</strong> <%= user.getPlace() != null ? user.getPlace().getName() : "Chưa cập nhật" %></p>
                        <p><strong>Đang theo dõi:</strong> <%= request.getAttribute("followedCount") != null ? request.getAttribute("followedCount") : 0 %> | 
                           <strong>Người theo dõi:</strong> <%= request.getAttribute("followerCount") != null ? request.getAttribute("followerCount") : 0 %></p>
                        <a href="<%= request.getContextPath() %>/users/edit/<%= user.getId() %>" class="btn btn-primary mb-3">Chỉnh sửa hồ sơ</a>
                        <form class="d-flex gap-2 align-items-center" action="/post" method="post">
                            <select class="form-select w-25" name="status">
                                <option value="public" selected>Công khai</option>
                                <option value="followers">Người theo dõi</option>
                                <option value="private">Chỉ mình tôi</option>
                            </select>
                            <input type="text" class="form-control" name="title" placeholder="Tiêu đề">
                            <textarea class="form-control mt-2" name="body" placeholder="Bạn đang nghĩ gì?" rows="3"></textarea>
                            <button class="btn btn-danger mt-2">Đăng</button>
                        </form>
                    </div>
                </div>

                <% 
                List<Post> posts = (List<Post>) request.getAttribute("posts");
                if (posts != null) {
                    for (Post post : posts) {
                %>
                <div class="card p-3 mb-3">
                    <div class="d-flex align-items-center justify-content-between mb-2">
                        <div class="d-flex align-items-center">
                            <img src="<%= user.getAvatar() != null ? request.getContextPath() + user.getAvatar() : request.getContextPath() + "/resources/images/avt.jpg" %>"
                                 alt="Avatar" class="rounded-circle me-2" width="30">
                            <b><%= user.getUsername() %></b>
                            <span class="text-muted ms-3"><%= post.getCreatedAt() %></span>
                        </div>
                        <div class="dropdown">
                            <button class="btn btn-light btn-sm" type="button" data-bs-toggle="dropdown" aria-expanded="false">⋮</button>
                            <ul class="dropdown-menu dropdown-menu-end">
                                <li><a class="dropdown-item" href="#">Chỉnh sửa trạng thái</a></li>
                                <li><a class="dropdown-item" href="#">Xóa bài</a></li>
                            </ul>
                        </div>
                    </div>
                    <p><strong>Trạng thái:</strong> <%= post.getStatus() %></p>
                    <p><strong>Tiêu đề:</strong> <%= post.getTitle() %></p>
                    <p><%= post.getBody() %></p>
                </div>
                <% 
                    }
                }
                %>
            </div>
            <div class="col-md-3">
                <div class="card p-3 mb-3">
                    <p class="fw-bold">Gợi ý theo dõi</p>
                    <%
                    List<User> suggestFollow = (List<User>) request.getAttribute("suggestFollow");
                    if (suggestFollow != null) {
                        for (User u : suggestFollow) {
                    %>
                    <p>
                        <img src="<%= u.getAvatar() != null ? request.getContextPath() + u.getAvatar() : request.getContextPath() + "/resources/images/avt.jpg" %>"
                             alt="Avatar" class="rounded-circle me-2" width="30">
                        <%= u.getUsername() %>
                        <button class="btn btn-sm btn-primary follow-btn"
                                data-following="<%= user.getId()%>"
                                data-followed="<%= u.getId()%>">Theo dõi</button>
                    </p>
                    <%
                        }
                    }
                    %>
                </div>
            </div>
        </div>
    </div>

    <footer class="bg-primary text-white text-center py-2 mt-4">
        <div class="container">
            <p>Công ty TNHH Mạng Xã Hội Việt © 2025</p>
            <p>Ngày phát hành: 15/02/2025</p>
            <p>Bản quyền © 2025. Mọi quyền được bảo lưu.</p>
        </div>
    </footer>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        document.querySelectorAll('.follow-btn').forEach(button => {
            button.addEventListener('click', function() {
                const followingUserId = this.getAttribute('data-following');
                const followedUserId = this.getAttribute('data-followed');

                fetch('/follow/add', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                    body: new URLSearchParams({ followingUserId, followedUserId })
                }).then(response => response.text())
                  .then(data => {
                      location.reload();
                  });
            });
        });
    </script>
</body>
</html>