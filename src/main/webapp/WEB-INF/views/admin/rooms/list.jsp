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

Quản Lí Phòng Chiếu

</h3>

<a href="${pageContext.request.contextPath}/admin/rooms/add" class="btn btn-success">

<i class="bi bi-plus-circle"></i> Add Room

</a>

</div>

</div>

<div class="card-body">

<form method="get"
      action="${pageContext.request.contextPath}/admin/rooms"
      class="row mb-3">

    <div class="col-md-10">

        <input
            type="text"
            name="keyword"
            class="form-control"
            placeholder="Search Room Name hoặc Capacity..."
            value="${keyword}">

    </div>

    <div class="col-md-2">

        <button class="btn btn-primary w-100">

            Search

        </button>

    </div>

</form>

<form method="get"
      action="${pageContext.request.contextPath}/admin/rooms"
      class="row mb-3">

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

        </select>

    </div>

    <div class="col-md-2">

        <button
            class="btn btn-success w-100">

            Filter

        </button>

    </div>

</form>

<c:if test="${param.error=='used'}">

<div class="alert alert-danger">

Không thể xóa phòng vì đã có suất chiếu.

</div>

</c:if>

<form method="get"
      action="${pageContext.request.contextPath}/admin/rooms"
      class="row mb-3">

    <div class="col-md-4">

        <select name="sortBy"
                class="form-select">

            <option value="room_name">Room Name</option>

            <option value="capacity">Capacity</option>

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

<th>Room Name</th>

<th>Capacity</th>

<th>Status</th>

<th>Created</th>

<th width="220">

Action

</th>

</tr>

</thead>

<tbody>

<c:forEach
var="r"
items="${rooms}">

<tr>

<td>${r.roomId}</td>

<td>${r.roomName}</td>

<td>${r.capacity}</td>

<td>

<span
class="badge
${r.status=='ACTIVE'
?'bg-success'
:'bg-danger'}">

${r.status}

</span>

</td>

<td>${r.createdAt}</td>

<td>

<a class="btn btn-info btn-sm" href="${pageContext.request.contextPath}/admin/rooms/detail?id=${r.roomId}">Detail</a>

<a class="btn btn-warning btn-sm" href="${pageContext.request.contextPath}/admin/rooms/edit?id=${r.roomId}">Edit</a>

<a class="btn btn-danger btn-sm" onclick="return confirm('Delete room?')" href="${pageContext.request.contextPath}/admin/rooms/delete?id=${r.roomId}">Delete</a>

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

<a class="page-link" href="${pageContext.request.contextPath}/admin/rooms?page=${i}">${i}</a>

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
