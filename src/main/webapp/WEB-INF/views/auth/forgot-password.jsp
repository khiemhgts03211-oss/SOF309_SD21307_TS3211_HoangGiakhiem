<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c"
           uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="vi">

<head>

<meta charset="UTF-8">

<title>Quên mật khẩu</title>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
      rel="stylesheet">

<link rel="stylesheet"
href="${pageContext.request.contextPath}/assets/css/auth.css">

<link
href="${pageContext.request.contextPath}/assets/css/style.css"
rel="stylesheet">

</head>

<body class="login-page">

<div class="container">

<div class="row justify-content-center">

<div class="col-md-5">

<div class="card shadow-lg border-0 mt-5">

<div class="card-body p-4">

<h3 class="text-center mb-4">

Quên mật khẩu

</h3>

<c:if test="${not empty error}">
<div class="alert alert-danger">

${error}

</div>
</c:if>

<c:if test="${not empty success}">
<div class="alert alert-success">

${success}

</div>
</c:if>

<form
method="post"
action="${pageContext.request.contextPath}/forgot-password">

<div class="mb-3">

<label class="form-label">

Email hoặc Username

</label>

<input
type="text"
name="account"
class="form-control"
placeholder="Nhập Email hoặc Username"
required>

</div>

<button
class="btn btn-primary w-100">

Tiếp tục

</button>

</form>

<div class="text-center mt-3">

<a href="${pageContext.request.contextPath}/login">

← Quay lại Đăng nhập

</a>

</div>

</div>

</div>

</div>

</div>

</div>

</body>

</html>