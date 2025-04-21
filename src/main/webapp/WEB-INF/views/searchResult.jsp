<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.abc.entities.User" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Kết Quả Tìm Kiếm</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <div class="container mt-5">
        <h2 class="mb-4">Kết quả tìm kiếm cho: "${keyword}"</h2>
        <%
        List<User> results = (List<User>) request.getAttribute("results");
        if (results != null && !results.isEmpty()) {
            for (User u : results) {
        %>
        <div class="card p-3 mb-3">
            <div class="d-flex align-items-center">
                <img src="<%= u.getAvatar() != null ? request.getContextPath() + u.getAvatar() : request.getContextPath() + "/resources/images/avt.jpg" %>"
                     alt="Avatar" class="rounded-circle me-2" width="40">
                <p class="mb-0"><strong><%= u.getUsername() %></strong></p>
            </div>
        </div>
        <%
            }
        } else {
        %>
        <div class="text-center mt-5">
            <img src="<%= request.getContextPath() %>/resources/images/not-found.jpg"
                 alt="Không tìm thấy"
                 class="img-fluid"
                 style="max-width: 300px;">
            <p class="text-muted mt-3">Không tìm thấy người dùng nào.</p>
        </div>
        <%
        }
        %>
        <a href="${pageContext.request.contextPath}/" class="btn btn-primary mt-3">🏠 Về trang chủ</a>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>