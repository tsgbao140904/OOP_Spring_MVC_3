<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.abc.entities.*"%>
<%@ page import="java.util.List"%>
<%@ page session="true"%>
<%
com.abc.entities.User user = (com.abc.entities.User) session.getAttribute("user");
if (user == null)
    response.sendRedirect("login");
%>
<html lang="vi">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Mạng Xã Hội</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="d-flex flex-column min-vh-100 bg-light">
    <!-- Navbar -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary fixed-top">
        <div class="container">
            <a class="navbar-brand" href="<%= request.getContextPath() %>/">🏠 Trang chủ</a>

            <!-- Search Form -->
            <form class="d-flex ms-auto me-3" action="search" method="get">
                <input class="form-control me-2" type="search" name="query" placeholder="Tìm kiếm người dùng..." aria-label="Search">
                <button class="btn btn-light" type="submit">Tìm</button>
            </form>

            <div class="navbar-nav ms-auto d-flex align-items-center">
                <a href="profile" class="d-flex align-items-center text-white me-3">
                    <span class="me-2">👤 Hồ sơ của bạn</span>
                    <img src="<%= user.getAvatar() != null ? request.getContextPath() + user.getAvatar() : request.getContextPath() + "/resources/images/avt.jpg" %>"
                         alt="Avatar" class="rounded-circle" width="40">
                </a>
                <a href="<%= request.getContextPath() %>/logout" class="btn btn-outline-light">Đăng xuất</a>
            </div>
        </div>
    </nav>

    <div class="container mt-5 pt-5 flex-grow-1">
        <div class="row">
            <!-- Sidebar Left: Danh sách bạn bè -->
            <div class="col-md-3">
                <div class="card p-3 mb-3">
                    <p class="fw-bold">Danh sách người theo dõi</p>
                    <%
                    List<User> userfed = (List<User>) request.getAttribute("userfed");
                    for (User ufed : userfed) {
                    %>
                    <div class="d-flex align-items-center justify-content-between mb-2">
                        <!-- Avatar + Username -->
                        <div class="d-flex align-items-center">
                            <img src="<%= ufed.getAvatar() != null ? request.getContextPath() + ufed.getAvatar() : request.getContextPath() + "/resources/images/avt.jpg" %>"
                                 alt="Avatar" class="rounded-circle me-2" width="30">
                            <span class="text-truncate" style="max-width: 120px;"><%=ufed.getUsername()%></span>
                        </div>
                        <!-- Button -->
                        <button class="btn btn-sm btn-primary unfollow-btn"
                                data-following="<%=user.getId()%>"
                                data-followed="<%=ufed.getId()%>">Hủy theo dõi</button>
                    </div>
                    <%
                    }
                    %>
                </div>
            </div>

            <!-- Main Content: Bài đăng -->
            <div class="col-md-6 mt-0">
                <div class="card mb-3 text-center">
                    <div class="card-body">
                        <form action="post" method="post">
                            <p class="fw-bold mb-3">Đăng bài</p>
                            <input name="title" type="text" class="form-control mb-3" placeholder="Tiêu đề">
                            <textarea class="form-control mb-3" id="postBody" name="body" rows="5" placeholder="Viết gì đó..."></textarea>
                            <button type="submit" class="btn btn-danger w-100">Đăng</button>
                        </form>
                    </div>
                </div>
                <!-- Posts -->
                <%
                List<Post> posts = (List<Post>) request.getAttribute("posts");
                if (posts != null) {
                    for (Post post : posts) {
                %>
                <div class="card p-3 mb-3">
                    <div class="d-flex align-items-center justify-content-between mb-2">
                        <div class="d-flex align-items-center">
                            <img src="<%= post.getUser().getAvatar() != null ? request.getContextPath() + post.getUser().getAvatar() : request.getContextPath() + "/resources/images/avt.jpg" %>"
                                 alt="Avatar" class="rounded-circle me-2" width="30">
                            <b><%= post.getUser().getUsername() %></b>
                            <span class="text-muted ms-3"><%= post.getCreatedAt() %></span>
                        </div>
                        <div class="dropdown">
                            <button class="btn btn-light btn-sm" type="button"
                                    data-bs-toggle="dropdown" aria-expanded="false">⋮</button>
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

            <!-- Sidebar Right: Gợi ý kết bạn -->
            <div class="col-md-3">
                <div class="card p-3 mb-3">
                    <p class="fw-bold">Gợi ý theo dõi</p>
                    <%
                    List<User> suggestfollow = (List<User>) request.getAttribute("suggestfollow");
                    if (suggestfollow != null) {
                        for (User u : suggestfollow) {
                    %>
                    <div class="d-flex align-items-center justify-content-between mb-2">
                        <!-- Avatar + Username -->
                        <div class="d-flex align-items-center">
                            <img src="<%= u.getAvatar() != null ? request.getContextPath() + u.getAvatar() : request.getContextPath() + "/resources/images/avt.jpg" %>"
                                 alt="Avatar" class="rounded-circle me-2" width="30">
                            <span class="text-truncate" style="max-width: 120px;"><%= u.getUsername() %></span>
                        </div>
                        <!-- Button -->
                        <button class="btn btn-sm btn-primary follow-btn"
                                data-following="<%= user.getId()%>"
                                data-followed="<%= u.getId()%>">Theo dõi</button>
                    </div>
                    <%
                        }
                    }
                    %>
                </div>
            </div>
        </div>
    </div>

    <footer class="bg-primary text-white text-center py-3 mt-auto">
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

        document.querySelectorAll('.unfollow-btn').forEach(button => {
            button.addEventListener('click', function() {
                const followingUserId = this.getAttribute('data-following');
                const followedUserId = this.getAttribute('data-followed');

                fetch('/follow/remove', {
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