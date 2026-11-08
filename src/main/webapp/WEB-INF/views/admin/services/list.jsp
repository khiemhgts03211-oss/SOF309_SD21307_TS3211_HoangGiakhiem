<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>

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

Quản Lí Dịch Vụ

</h3>

<a href="${pageContext.request.contextPath}/admin/services/add" class="btn btn-success">

<i class="bi bi-plus-circle"></i> Add Service

</a>

</div>

</div>

<div class="card-body">

<form method="get"
      action="${pageContext.request.contextPath}/admin/services"
      class="row mb-3">

    <div class="col-md-10">

        <input
            type="text"
            name="keyword"
            class="form-control"
            placeholder="Search service..."
            value="${keyword}">

    </div>

    <div class="col-md-2">

        <button class="btn btn-primary w-100">

            Search

        </button>

    </div>

</form>

<form method="get"
      action="${pageContext.request.contextPath}/admin/services"
      class="row mb-3">

    <div class="col-md-4">

        <select
            name="category"
            class="form-select">

            <option value="">All Category</option>

            <c:forEach var="c" items="${categories}">

                <option value="${c.categoryId}"
                    ${category==c.categoryId?'selected':''}>

                    ${c.categoryName}

                </option>

            </c:forEach>

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
      action="${pageContext.request.contextPath}/admin/services"
      class="row mb-3">

    <div class="col-md-4">

        <select name="sort"
                class="form-select">

            <option value="">-- Sắp xếp theo --</option>

            <option value="name">Service Name</option>

            <option value="price">Price</option>

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

<th>Category</th>

<th>Service Name</th>

<th>Price</th>

<th>Status</th>

<th>Created</th>

<th width="220">

Action

</th>

</tr>

</thead>

<tbody>

<c:forEach
var="s"
items="${services}">

<tr>

<td>${s.serviceId}</td>

<td>${s.categoryName}</td>

<td>${s.serviceName}</td>

<td><fmt:formatNumber value="${s.price}" pattern="#,##0"/> đ</td>

<td>

<span
class="badge
${s.status=='ACTIVE'
?'bg-success'
:'bg-danger'}">

${s.status}

</span>

</td>

<td>${s.createdAt}</td>

<td>

<a class="btn btn-info btn-sm" href="${pageContext.request.contextPath}/admin/services/detail?id=${s.serviceId}">Detail</a>

<a class="btn btn-warning btn-sm" href="${pageContext.request.contextPath}/admin/services/edit?id=${s.serviceId}">Edit</a>

<a class="btn btn-danger btn-sm" onclick="return confirm('Delete service?')" href="${pageContext.request.contextPath}/admin/services/delete?id=${s.serviceId}">Delete</a>

</td>

</tr>

</c:forEach>

<c:if test="${empty services}">

<tr>

<td colspan="7" class="text-center text-muted">

Không có dịch vụ nào.

</td>

</tr>

</c:if>

</tbody>

</table>

</div>

</div>

</div>

    </div>

</div>

</body>
</html>
