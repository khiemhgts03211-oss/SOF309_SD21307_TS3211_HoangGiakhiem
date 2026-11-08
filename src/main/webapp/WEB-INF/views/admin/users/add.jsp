<%@ page contentType="text/html;charset=UTF-8"%>

<!DOCTYPE html>
<html>
<head>

<meta charset="UTF-8">

<meta name="viewport" content="width=device-width, initial-scale=1">

<title>Thêm User</title>

<link rel="stylesheet"
href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css">

</head>

<body>

<div class="container mt-5">

<h2>Thêm User</h2>

<form method="post"
      action="${pageContext.request.contextPath}/admin/users">

    <div class="mb-3">

        <label>Username</label>

        <input
            type="text"
            name="username"
            class="form-control"
            required>

    </div>

    <div class="mb-3">

        <label>Password</label>

        <input
            type="password"
            name="password"
            class="form-control"
            required>

    </div>

    <div class="mb-3">

        <label>Họ tên</label>

        <input
            type="text"
            name="fullName"
            class="form-control"
            required>

    </div>

    <div class="mb-3">

        <label>Email</label>

        <input
            type="email"
            name="email"
            class="form-control"
            required>

    </div>

    <div class="mb-3">

        <label>Số điện thoại</label>

        <input
            type="text"
            name="phone"
            class="form-control">

    </div>

    <div class="mb-3">

        <label>Role</label>

        <select
            name="role"
            class="form-select">

            <option value="ADMIN">ADMIN</option>
            <option value="STAFF">STAFF</option>
            <option value="CUSTOMER">CUSTOMER</option>

        </select>

    </div>

    <div class="mb-3">

        <label>Status</label>

        <select
            name="status"
            class="form-select">

            <option value="ACTIVE">ACTIVE</option>
            <option value="INACTIVE">INACTIVE</option>

        </select>

    </div>

    <button
        class="btn btn-success">

        Lưu

    </button>

    <a href="${pageContext.request.contextPath}/admin/users"
       class="btn btn-secondary">

        Quay lại

    </a>

</form>

</div>

</body>
</html>