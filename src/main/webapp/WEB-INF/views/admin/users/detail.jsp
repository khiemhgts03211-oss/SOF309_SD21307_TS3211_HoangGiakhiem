<%@ page contentType="text/html;charset=UTF-8"%>

<!DOCTYPE html>
<html>
<head>

<meta charset="UTF-8">

<meta name="viewport" content="width=device-width, initial-scale=1">

<title>User Detail</title>

<link rel="stylesheet"
href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css">

</head>

<body class="bg-light">

<div class="container mt-5">

<div class="card">

<div class="card-header bg-primary text-white">

<h3>User Detail</h3>

</div>

<div class="card-body">

<table class="table table-bordered">

<tr>
    <th>ID</th>
    <td>${user.userId}</td>
</tr>

<tr>
    <th>Username</th>
    <td>${user.username}</td>
</tr>

<tr>
    <th>Full Name</th>
    <td>${user.fullName}</td>
</tr>

<tr>
    <th>Email</th>
    <td>${user.email}</td>
</tr>

<tr>
    <th>Phone</th>
    <td>${user.phone}</td>
</tr>

<tr>
    <th>Role</th>
    <td>${user.role}</td>
</tr>

<tr>
    <th>Status</th>
    <td>${user.status}</td>
</tr>

<tr>
    <th>Created At</th>
    <td>${user.createdAt}</td>
</tr>

</table>

<a href="${pageContext.request.contextPath}/admin/users"
class="btn btn-secondary">

Back

</a>

<a href="${pageContext.request.contextPath}/admin/users/edit?id=${user.userId}"
class="btn btn-warning">

Edit

</a>

</div>

</div>

</div>

</body>
</html>