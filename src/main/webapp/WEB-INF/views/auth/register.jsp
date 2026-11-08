<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<!DOCTYPE html>
<html lang="vi">

<head>

<meta charset="UTF-8">

<title>Đăng ký</title>

<meta name="viewport"
      content="width=device-width, initial-scale=1">

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
      rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Quicksand:wght@400;500;600;700&display=swap" rel="stylesheet">
<link rel="stylesheet"
href="${pageContext.request.contextPath}/assets/css/auth.css">
</head>

<body class="login-page">

<div class="login-overlay">

<div class="login-card">

<img
src="${pageContext.request.contextPath}/assets/images/logo.png"
class="login-logo"
alt="Logo">

<h2>Đăng ký tài khoản</h2>

<p class="text-muted">

Movie Ticket Management

</p>

<c:if test="${not empty error}">
<div class="alert alert-danger">

${error}

</div>
</c:if>

<form
method="post"
action="${pageContext.request.contextPath}/register"
class="needs-validation"
novalidate>

<div class="mb-3">

<label>Họ và tên</label>

<input
type="text"
name="fullName"
class="form-control"
required>

</div>

<div class="mb-3">

<label>Tên đăng nhập</label>

<input
type="text"
name="username"
class="form-control"
required
minlength="4"
maxlength="20"
pattern="[A-Za-z0-9_]+">

<div class="invalid-feedback">

Tên đăng nhập từ 4 đến 20 ký tự.

</div>

</div>

<div class="mb-3">

<label>Email</label>

<input
type="email"
name="email"
class="form-control"
required>

<div class="invalid-feedback">

Email không đúng định dạng.

</div>

</div>

<div class="mb-3">

<label>Số điện thoại</label>

<input
type="tel"
name="phone"
class="form-control"
pattern="(0|84)(3|5|7|8|9)[0-9]{8}">

<div class="invalid-feedback">

Số điện thoại không hợp lệ.

</div>

</div>

<div class="mb-3">

<label>Mật khẩu</label>

<input
type="password"
name="password"
class="form-control"
required
minlength="8">

<div class="invalid-feedback">

Mật khẩu tối thiểu 8 ký tự.

</div>

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
class="btn btn-primary w-100">

Đăng ký

</button>

</form>

<hr>

<div class="text-center">

Đã có tài khoản?

<a href="${pageContext.request.contextPath}/login">

Đăng nhập

</a>

</div>

</div>

</div>

<script>

(() => {

'use strict';

const forms =
document.querySelectorAll('.needs-validation');

Array.from(forms).forEach(form => {

form.addEventListener('submit', event => {

if (!form.checkValidity()) {

event.preventDefault();

event.stopPropagation();

}

form.classList.add('was-validated');

}, false);

});

})();

</script>

</body>

</html>