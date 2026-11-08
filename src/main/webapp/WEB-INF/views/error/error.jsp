<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="vi">
<head>

<meta charset="UTF-8">

<title>Lỗi hệ thống</title>

<link rel="stylesheet"
href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css">

<link rel="stylesheet"
href="${pageContext.request.contextPath}/assets/css/style.css">

<style>

.error-box{
    max-width:650px;
    margin:100px auto;
    text-align:center;
}

.error-code{
    font-size:90px;
    font-weight:bold;
    color:#dc3545;
}

.error-message{
    font-size:22px;
    margin-bottom:15px;
}

</style>

</head>

<body class="login-page">

<div class="container">

<div class="error-box card shadow p-5">

<div class="error-code">

${statusCode}

</div>

<h3 class="error-message">

${title}

</h3>

<p>

${message}

</p>

<a class="btn btn-primary"

href="${pageContext.request.contextPath}/">

Quay về Trang chủ

</a>

</div>

</div>

</body>

</html>