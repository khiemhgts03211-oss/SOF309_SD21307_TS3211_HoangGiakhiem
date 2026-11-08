<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<body>

<jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin.css">

<div class="d-flex">

    <jsp:include page="/WEB-INF/views/common/sidebar.jsp"/>

    <div class="flex-grow-1 p-4">

<div class="card shadow">

<div class="card-header bg-dark text-white">

<div class="d-flex
justify-content-between
align-items-center
flex-wrap">

<h3 class="mb-2 mb-md-0">

Quản Lí Người Dùng

</h3>

<a href="${pageContext.request.contextPath}/admin/users/add" class="btn btn-success">

<i class="bi bi-person-plus"></i> Add User

</a>

</div>

</div>

<div class="card-body">

<form method="get"
      action="${pageContext.request.contextPath}/admin/users"
      class="row mb-3">

    <div class="col-md-10">

        <input
            type="text"
            name="keyword"
            class="form-control"
            placeholder="Search username, họ tên hoặc email..."
            value="${keyword}">

    </div>

    <div class="col-md-2">

        <button class="btn btn-primary w-100">

            Search

        </button>

    </div>

</form>

<form method="get"
      action="${pageContext.request.contextPath}/admin/users"
      class="row mb-3">

    <div class="col-md-4">

        <select
            name="role"
            class="form-select">

            <option value="">All Role</option>

            <option value="ADMIN"
                ${role=='ADMIN'?'selected':''}>

                ADMIN

            </option>

            <option value="STAFF"
                ${role=='STAFF'?'selected':''}>

                STAFF

            </option>

            <option value="CUSTOMER"
                ${role=='CUSTOMER'?'selected':''}>

                CUSTOMER

            </option>

        </select>

    </div>

    <div class="col-md-4">

        <select
            name="status"
            class="form-select">

            <option value="">All Status</option>

            <option value="ACTIVE"
                ${status=='ACTIVE'?'selected':''}>

                ACTIVE

            </option>

            <option value="INACTIVE"
                ${status=='INACTIVE'?'selected':''}>

                INACTIVE

            </option>

            <option value="LOCKED"
                ${status=='LOCKED'?'selected':''}>

                LOCKED

            </option>

        </select>

    </div>

    <div class="col-md-2">

        <button
            class="btn btn-success w-100">

            Filter

        </button>

    </div>

</form>

<form method="get"
      action="${pageContext.request.contextPath}/admin/users"
      class="row mb-3">

    <div class="col-md-4">

        <select name="sortBy"
                class="form-select">

            <option value="username">Username</option>

            <option value="full_name">Full Name</option>

            <option value="created_at">Created Date</option>

        </select>

    </div>

    <div class="col-md-4">

        <select name="order"
                class="form-select">

            <option value="ASC">Ascending</option>

            <option value="DESC">Descending</option>

        </select>

    </div>

    <div class="col-md-2">

        <button class="btn btn-warning w-100">

            Sort

        </button>

    </div>

</form>

<div class="table-responsive">

<table class="table
table-bordered
table-hover">

<thead class="table-dark">

<tr>

<th>ID</th>

<th>Username</th>

<th>Full Name</th>

<th>Email</th>

<th>Phone</th>

<th>Role</th>

<th>Status</th>

<th>Created</th>

<th width="220">

Action

</th>

</tr>

</thead>

<tbody>

<c:forEach
var="u"
items="${users}">

<tr>

<td>${u.userId}</td>

<td>${u.username}</td>

<td>${u.fullName}</td>

<td>${u.email}</td>

<td>${u.phone}</td>

<td>

<span
class="badge
${u.role=='ADMIN'
?'bg-primary'
:(u.role=='STAFF'
?'bg-warning text-dark'
:'bg-secondary')}">

${u.role}

</span>

</td>

<td>

<span
class="badge
${u.status=='ACTIVE'
?'bg-success'
:'bg-danger'}">

${u.status}

</span>

</td>

<td>${u.createdAt}</td>

<td>

<a class="btn btn-info btn-sm" href="${pageContext.request.contextPath}/admin/users/detail?id=${u.userId}">Detail</a>

<a class="btn btn-warning btn-sm" href="${pageContext.request.contextPath}/admin/users/edit?id=${u.userId}">Edit</a>

<a class="btn btn-danger btn-sm" onclick="return confirm('Delete user?')" href="${pageContext.request.contextPath}/admin/users/delete?id=${u.userId}">Delete</a>

</td>

</tr>

</c:forEach>

</tbody>

</table>

</div>

<nav>

<ul class="pagination justify-content-center">

<c:forEach
begin="1"
end="${totalPage}"
var="i">

<li class="page-item
${i==currentPage?'active':''}">

<a class="page-link" href="${pageContext.request.contextPath}/admin/users?page=${i}">${i}</a>

</li>

</c:forEach>

</ul>

</nav>

</div>

</div>

</div>

    </div>

</div>

</body>
</html>
