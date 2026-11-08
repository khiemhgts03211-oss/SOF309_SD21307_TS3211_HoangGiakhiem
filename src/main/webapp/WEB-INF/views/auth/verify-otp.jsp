<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<!DOCTYPE html>
<html lang="vi">
<head>

<meta charset="UTF-8">

<title>Xác thực OTP</title>

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

Xác thực OTP

</h3>

<p class="text-center text-muted">

Nhập mã OTP gồm 6 chữ số đã gửi đến Email.

</p>

<c:if test="${not empty error}">

<div class="alert alert-danger">

${error}

</div>

</c:if>

<form
method="post"
action="${pageContext.request.contextPath}/verify-otp">

<div class="mb-3">

<input
type="text"
name="otp"
maxlength="6"
class="form-control text-center fs-4"
placeholder="000000"
required>

</div>

<button
class="btn btn-primary w-100">

Xác nhận

</button>

</form>

<div class="text-center mt-3">

<a href="${pageContext.request.contextPath}/forgot-password">

Gửi lại OTP

</a>

</div>

</div>

</div>

</div>

</div>

</div>

</body>

</html>