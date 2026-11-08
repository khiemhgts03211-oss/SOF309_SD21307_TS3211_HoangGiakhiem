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

Edit Service Category

</h3>

<a href="${pageContext.request.contextPath}/admin/service-categories" class="btn btn-secondary btn-sm">

<i class="bi bi-arrow-left"></i> Back

</a>

</div>

</div>

<div class="card-body">

<form method="post"
      action="${pageContext.request.contextPath}/admin/service-categories/edit"
      class="row g-3">

    <input type="hidden" name="categoryId" value="${category.categoryId}">

    <div class="col-md-6">

        <label class="form-label">Category Name</label>

        <input
            type="text"
            name="categoryName"
            class="form-control"
            value="${category.categoryName}"
            required>

    </div>

    <div class="col-md-6">

        <label class="form-label">Status</label>

        <select name="status" class="form-select">

            <option value="ACTIVE" ${category.status=='ACTIVE'?'selected':''}>ACTIVE</option>
            <option value="INACTIVE" ${category.status=='INACTIVE'?'selected':''}>INACTIVE</option>

        </select>

    </div>

    <div class="col-12 mt-4">

        <button type="submit" class="btn btn-warning">

            <i class="bi bi-save"></i> Update

        </button>

        <a href="${pageContext.request.contextPath}/admin/service-categories" class="btn btn-outline-secondary">

            Cancel

        </a>

    </div>

</form>

</div>

</div>

    </div>

</div>

</body>
</html>
