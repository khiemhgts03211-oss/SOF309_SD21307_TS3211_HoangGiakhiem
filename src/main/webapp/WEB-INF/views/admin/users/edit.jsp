<%@ page contentType="text/html;charset=UTF-8"%>

<!DOCTYPE html>
<html>
<head>

<meta charset="UTF-8">

<meta name="viewport" content="width=device-width, initial-scale=1">

<title>Edit User</title>

<link rel="stylesheet"
href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css">

</head>

<body>

<div class="container mt-5">

<h2>Edit User</h2>

<form method="post"
      action="${pageContext.request.contextPath}/admin/users">

<input type="hidden"
       name="userId"
       value="${user.userId}">

<div class="mb-3">

<label>Username</label>

<input
class="form-control"
name="username"
value="${user.username}"
required>

</div>

<div class="mb-3">

<label>Password mới</label>

<input
type="password"
class="form-control"
name="password">

<small>
Để trống nếu không đổi mật khẩu
</small>

</div>

<div class="mb-3">

<label>Họ tên</label>

<input
class="form-control"
name="fullName"
value="${user.fullName}"
required>

</div>

<div class="mb-3">

<label>Email</label>

<input
class="form-control"
name="email"
value="${user.email}">

</div>

<div class="mb-3">

<label>Phone</label>

<input
class="form-control"
name="phone"
value="${user.phone}">

</div>

<div class="mb-3">

<label>Role</label>

<select
name="role"
class="form-select">

<option value="ADMIN"
${user.role=='ADMIN'?'selected':''}>

ADMIN

</option>

<option value="STAFF"
${user.role=='STAFF'?'selected':''}>

STAFF

</option>

<option value="CUSTOMER"
${user.role=='CUSTOMER'?'selected':''}>

CUSTOMER

</option>

</select>

</div>

<div class="mb-3">

<label>Status</label>

<select
name="status"
class="form-select">

<option value="ACTIVE"
${user.status=='ACTIVE'?'selected':''}>

ACTIVE

</option>

<option value="INACTIVE"
${user.status=='INACTIVE'?'selected':''}>

INACTIVE

</option>

</select>

</div>

<button class="btn btn-success">

Cập nhật

</button>

<a href="${pageContext.request.contextPath}/admin/users"
class="btn btn-secondary">

Quay lại

</a>

</form>

</div>

</body>
</html>