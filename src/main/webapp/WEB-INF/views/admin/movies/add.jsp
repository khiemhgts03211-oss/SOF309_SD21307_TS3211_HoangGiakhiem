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

Add Movie

</h3>

<a href="${pageContext.request.contextPath}/admin/movies" class="btn btn-secondary btn-sm">

<i class="bi bi-arrow-left"></i> Back

</a>

</div>

</div>

<div class="card-body">

<c:if test="${not empty error}">

<div class="alert alert-danger">

${error}

</div>

</c:if>

<form method="post"
      enctype="multipart/form-data"
      action="${pageContext.request.contextPath}/admin/movies/add"
      class="row g-3">

    <div class="col-md-6">

        <label class="form-label">Title</label>

        <input
            type="text"
            name="title"
            class="form-control"
            value="${movie.title}"
            required>

    </div>

    <div class="col-md-6">

        <label class="form-label">Genre</label>

        <input
            type="text"
            name="genre"
            class="form-control"
            value="${movie.genre}">

    </div>

    <div class="col-md-6">

        <label class="form-label">Duration (phút)</label>

        <input
            type="number"
            name="duration"
            class="form-control"
            value="${movie.duration}"
            required>

    </div>

    <div class="col-md-6">

        <label class="form-label">Release Date</label>

        <input
            type="date"
            name="releaseDate"
            class="form-control"
            required>

    </div>

    <div class="col-md-6">

        <label class="form-label">Status</label>

        <select name="status" class="form-select">

            <option value="ACTIVE">ACTIVE</option>
            <option value="INACTIVE">INACTIVE</option>
            <option value="COMING_SOON">COMING_SOON</option>

        </select>

    </div>

    <div class="col-md-6">

        <label class="form-label">Poster</label>

        <input
            type="file"
            name="poster"
            class="form-control"
            accept="image/*">

    </div>

    <div class="col-12">

        <label class="form-label">Description</label>

        <textarea
            name="description"
            class="form-control"
            rows="4">${movie.description}</textarea>

    </div>

    <div class="col-12 mt-4">

        <button type="submit" class="btn btn-success">

            <i class="bi bi-save"></i> Save

        </button>

        <a href="${pageContext.request.contextPath}/admin/movies" class="btn btn-outline-secondary">

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
