<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.abc.entities.User"%>
<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Danh Sách Người Dùng</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet">
</head>
<body>
	<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
		<div class="container-fluid">
			<a class="navbar-brand" href="<%= request.getContextPath() %>/">🏠
				Trang chủ</a>
			<div class="ms-auto">
				<a href="<%= request.getContextPath() %>/logout"
					class="btn btn-outline-light">Đăng xuất</a>
			</div>
		</div>
	</nav>

	<div class="container mt-4">
		<h2 class="text-center mb-4">Danh Sách Người Dùng</h2>

		<!-- Hiển thị thông báo lỗi nếu có -->
		<%
            String error = (String) request.getAttribute("error");
            if (error != null) {
        %>
		<div class="alert alert-danger" role="alert">
			<%= error %>
		</div>
		<%
            }
        %>

		<!-- Hiển thị danh sách người dùng -->
		<%
            List<User> users = (List<User>) request.getAttribute("users");
            if (users != null && !users.isEmpty()) {
        %>
		<table class="table table-bordered table-striped">
			<thead class="table-dark">
				<tr>
					<th>ID</th>
					<th>Avatar</th>
					<th>Tên người dùng</th>
					<th>Email</th>
					<th>Ngày sinh</th>
					<th>Nơi ở</th>
					<th>Hành động</th>
				</tr>
			</thead>
			<tbody>
				<%
                    for (User user : users) {
                %>
				<tr>
					<td><%= user.getId() %></td>
					<td>
						<%
        String avatarPath = user.getAvatar();
        String avatarUrl = avatarPath != null ? request.getContextPath() + avatarPath : request.getContextPath() + "/resources/images/avt.jpg";
        System.out.println("User ID: " + user.getId() + ", Avatar Path: " + avatarPath + ", Full URL: " + avatarUrl);
    %> <img src="<%= avatarUrl %>" alt="Avatar" class="rounded-circle"
						width="40" height="40">
					</td>
					<td><%= user.getUsername() %></td>
					<td><%= user.getEmail() != null ? user.getEmail() : "Chưa cập nhật" %></td>
					<td><%= user.getDateOfBirth() != null ? user.getDateOfBirth() : "Chưa cập nhật" %></td>
					<td><%= user.getPlace() != null ? user.getPlace().getName() : "Chưa cập nhật" %></td>
					<td><a
						href="<%= request.getContextPath() %>/users/edit/<%= user.getId() %>"
						class="btn btn-sm btn-primary">Chỉnh sửa</a></td>
				</tr>
				<%
                    }
                %>
			</tbody>
		</table>
		<%
            } else {
        %>
		<p class="text-center text-muted">Không có người dùng nào.</p>
		<%
            }
        %>
	</div>

	<footer class="bg-primary text-white text-center py-2 mt-4">
		<div class="container">
			<p>Công ty TNHH Mạng Xã Hội Việt © 2025</p>
			<p>Ngày phát hành: 15/02/2025</p>
			<p>Bản quyền © 2025. Mọi quyền được bảo lưu.</p>
		</div>
	</footer>

	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>