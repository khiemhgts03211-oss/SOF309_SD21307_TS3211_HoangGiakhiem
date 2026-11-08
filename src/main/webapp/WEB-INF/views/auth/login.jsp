<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Đăng nhập</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Quicksand:wght@400;500;600;700&display=swap" rel="stylesheet">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/login.css" type="text/css">
</head>
<body>

    <div class="login-card">

        <img src="${pageContext.request.contextPath}/assets/images/logo.png" alt="Logo" class="login-logo">

        <h2>Đăng Nhập</h2>
        <p class="subtitle">Movie Ticket Management</p>

        <c:if test="${not empty error}">
            <div class="alert alert-error">${error}</div>
        </c:if>

        <c:if test="${not empty success}">
            <div class="alert alert-success">${success}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/login" method="POST">

            <div class="form-group">
                <input type="text" name="username" value="${username}" placeholder="Tên đăng nhập" required>
            </div>

            <div class="form-group">
                <input type="password" name="password" placeholder="Mật khẩu" required>
            </div>

            <div class="form-options">
                <label class="remember">
                    <input type="checkbox" name="remember" ${remember ? "checked" : ""}>
                    Ghi nhớ đăng nhập
                </label>
                <a href="${pageContext.request.contextPath}/forgot-password">Quên mật khẩu?</a>
            </div>

            <button type="submit">Đăng Nhập</button>

        </form>

        <div class="switch-link">
            Chưa có tài khoản? <a href="${pageContext.request.contextPath}/register">Đăng ký ngay</a>
        </div>

    </div>

</body>
</html>