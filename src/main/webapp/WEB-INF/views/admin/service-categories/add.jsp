<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<!DOCTYPE html>

<html>

<head>

<meta charset="UTF-8">

<title>Add Service Category</title>

<link rel="stylesheet"
href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css">

</head>

<body>

<div class="container mt-4">

<div class="card">

<div class="card-header bg-success text-white">

<h3>Add Service Category</h3>

</div>

<div class="card-body">

<c:if test="${not empty error}">

<div class="alert alert-danger">

${error}

</div>

</c:if>

<form method="post">

<div class="mb-3">

<label>Category Name</label>

<input
type="text"
name="categoryName"
class="form-control"
required>

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

Save

</button>

<a
class="btn btn-secondary"
href="${pageContext.request.contextPath}/admin/service-categories">

Back

</a>

</form>

</div>

</div>

</div>

</body>

</html>>