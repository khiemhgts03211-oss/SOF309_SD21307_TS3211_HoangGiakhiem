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

Service Category Detail

</h3>

<a href="${pageContext.request.contextPath}/admin/service-categories" class="btn btn-secondary btn-sm">

<i class="bi bi-arrow-left"></i> Back

</a>

</div>

</div>

<div class="card-body">

<table class="table table-borderless w-auto">

<tbody>

<tr>
<th width="180">Category ID</th>
<td>${category.categoryId}</td>
</tr>

<tr>
<th>Name</th>
<td class="fw-bold fs-5">${category.categoryName}</td>
</tr>

<tr>
<th>Status</th>
<td>

<span
class="badge
${category.status=='ACTIVE'
?'bg-success'
:'bg-danger'}">

${category.status}

</span>

</td>
</tr>

<tr>
<th>Created At</th>
<td>${category.createdAt}</td>
</tr>

</tbody>

</table>

<a class="btn btn-warning" href="${pageContext.request.contextPath}/admin/service-categories/edit?id=${category.categoryId}">

<i class="bi bi-pencil-square"></i> Edit

</a>

</div>

</div>

    </div>

</div>

</body>
</html>
