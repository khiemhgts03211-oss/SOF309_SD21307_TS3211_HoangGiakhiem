<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>403 - Access Denied</title>

<link
    href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
    rel="stylesheet">

<style>

body{
    background:#f8f9fa;
}

.error-container{

    max-width:600px;

    margin:100px auto;

    background:white;

    padding:40px;

    border-radius:10px;

    text-align:center;

    box-shadow:0 0 15px rgba(0,0,0,.1);

}

.error-code{

    font-size:72px;

    font-weight:bold;

    color:#dc3545;

}

.error-title{

    font-size:30px;

    margin-bottom:20px;

}

.error-message{

    color:#6c757d;

    margin-bottom:30px;

}

</style>

</head>

<body>

<div class="error-container">

    <div class="error-code">
        403
    </div>

    <div class="error-title">
        Access Denied
    </div>

    <div class="error-message">

        Bạn không có quyền truy cập chức năng này.

    </div>

    <a
        href="${pageContext.request.contextPath}/"
        class="btn btn-primary">

        Trang chủ

    </a>

    <a
        href="${pageContext.request.contextPath}/login"
        class="btn btn-outline-secondary ms-2">

        Đăng nhập

    </a>

</div>

</body>
</html>