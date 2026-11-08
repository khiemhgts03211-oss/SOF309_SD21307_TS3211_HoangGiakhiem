<%@ page contentType="text/html;charset=UTF-8"%>

<!DOCTYPE html>
<html lang="vi">
<head>

<meta charset="UTF-8">

<title>404 - Không tìm thấy trang</title>

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
    padding:40px;
    text-align:center;
    max-width:650px;
    box-shadow:0 20px 50px rgba(0,0,0,.3);
}

.error-code{
    font-size:100px;
    font-weight:bold;
    color:#0d6efd;
}

.error-card img{
    width:140px;
    margin-bottom:20px;
}

</style>

</head>

<body>

<div class="error-card">

<img
src="${pageContext.request.contextPath}/assets/images/logo.png"
alt="Logo">

<div class="error-code">

404

</div>

<h2>Không tìm thấy trang</h2>

<p class="text-muted">

Trang bạn yêu cầu không tồn tại hoặc đã bị di chuyển.

</p>

<div class="mt-4">

<a
class="btn btn-primary"
href="${pageContext.request.contextPath}/">

Trang chủ

</a>

<a
class="btn btn-outline-secondary ms-2"
href="javascript:history.back()">

Quay lại

</a>

</div>

</div>

</body>

</html>