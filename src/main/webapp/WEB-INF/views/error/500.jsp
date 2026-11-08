<%@ page contentType="text/html;charset=UTF-8"%>

<!DOCTYPE html>
<html lang="vi">

<head>

<meta charset="UTF-8">

<title>500 - Internal Server Error</title>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
      rel="stylesheet">

<style>

body{
    background:#0f172a;
    color:#fff;
    height:100vh;
    display:flex;
    justify-content:center;
    align-items:center;
    font-family:Arial,sans-serif;
}

.error-card{
    background:#fff;
    color:#333;
    border-radius:20px;
    padding:45px;
    max-width:700px;
    text-align:center;
    box-shadow:0 20px 40px rgba(0,0,0,.25);
}

.error-code{
    font-size:100px;
    color:#dc3545;
    font-weight:bold;
}

</style>

</head>

<body>

<div class="error-card">

<img
src="${pageContext.request.contextPath}/assets/images/logo.png"
width="120">

<div class="error-code">

500

</div>

<h2>Đã xảy ra lỗi hệ thống</h2>

<p class="text-muted">

Máy chủ gặp lỗi khi xử lý yêu cầu của bạn.

<br>

Vui lòng thử lại sau hoặc liên hệ quản trị viên.

</p>

<a
href="${pageContext.request.contextPath}/"
class="btn btn-danger mt-3">

Trang chủ

</a>

</div>

</body>

</html>