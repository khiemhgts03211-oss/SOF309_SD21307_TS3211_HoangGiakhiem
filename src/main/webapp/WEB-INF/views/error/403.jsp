<%@ page contentType="text/html;charset=UTF-8"%>

<!DOCTYPE html>
<html lang="vi">
<head>

<meta charset="UTF-8">

<title>403 - Forbidden</title>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
      rel="stylesheet">

<style>

body{
    background:#0f172a;
    color:white;
    height:100vh;
    display:flex;
    justify-content:center;
    align-items:center;
}

.error-card{
    max-width:600px;
    text-align:center;
    background:white;
    color:#333;
    padding:40px;
    border-radius:20px;
    box-shadow:0 20px 40px rgba(0,0,0,.25);
}

.error-code{
    font-size:90px;
    font-weight:bold;
    color:#dc3545;
}

</style>

</head>

<body>

<div class="error-card">

<div class="error-code">
403
</div>

<h2>Truy cập bị từ chối</h2>

<p class="text-muted">

Bạn không có quyền truy cập vào tài nguyên này.

</p>

<a
class="btn btn-primary mt-3"
href="${pageContext.request.contextPath}/login">

Quay về đăng nhập

</a>

</div>

</body>

</html>