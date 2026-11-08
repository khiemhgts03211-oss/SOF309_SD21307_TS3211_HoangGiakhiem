<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<!DOCTYPE html>
<html lang="vi">

<head>

<meta charset="UTF-8">

<title>Đặt lại mật khẩu</title>

<link rel="stylesheet"
href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css">

</head>

<body class="login-page">

<div class="container">

<div class="row justify-content-center">

<div class="col-md-5">

<div class="card shadow mt-5">

<div class="card-body">

<h3 class="text-center">

Đặt lại mật khẩu

</h3>

<c:if test="${not empty error}">
<div class="alert alert-danger">

${error}

</div>
</c:if>

<form
method="post"
action="${pageContext.request.contextPath}/reset-password">

<div class="mb-3">

<label>Mật khẩu mới</label>

<input
type="password"
name="password"
class="form-control"
required>

</div>

<div class="mb-3">

<label>Xác nhận mật khẩu</label>

<input
type="password"
name="confirmPassword"
class="form-control"
required>

</div>

<button
class="btn btn-success w-100">

Đổi mật khẩu

</button>

</form>

</div>

</div>

</div>

</div>

</div>

</body>

</html>