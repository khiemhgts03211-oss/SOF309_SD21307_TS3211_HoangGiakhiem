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

Edit Movie

</h3>

<a href="${pageContext.request.contextPath}/admin/movies" class="btn btn-secondary btn-sm">

<i class="bi bi-arrow-left"></i> Back

</a>

</div>

</div>

<div class="card-body">

<form method="post"
      enctype="multipart/form-data"
      action="${pageContext.request.contextPath}/admin/movies/edit"
      class="row g-3">

    <input type="hidden" name="movieId" value="${movie.movieId}">

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
            value="<fmt:formatDate value='${movie.releaseDate}' pattern='yyyy-MM-dd'/>"
            required>

    </div>

    <div class="col-md-6">

        <label class="form-label">Status</label>

        <select name="status" class="form-select">

            <option value="ACTIVE" ${movie.status=='ACTIVE'?'selected':''}>ACTIVE</option>
            <option value="INACTIVE" ${movie.status=='INACTIVE'?'selected':''}>INACTIVE</option>
            <option value="COMING_SOON" ${movie.status=='COMING_SOON'?'selected':''}>COMING_SOON</option>

        </select>

    </div>

    <div class="col-12">

        <label class="form-label">Description</label>

        <textarea
            name="description"
            class="form-control"
            rows="4">${movie.description}</textarea>

    </div>

    <div class="col-md-6">

        <label class="form-label d-block">Current Poster</label>

        <c:choose>

            <c:when test="${not empty movie.poster}">

                <img
                    src="${pageContext.request.contextPath}/${movie.poster}"
                    class="img-thumbnail"
                    style="max-height:220px"
                    alt="${movie.title}">

            </c:when>

            <c:otherwise>

                <span class="text-muted">Chưa có poster</span>

            </c:otherwise>

        </c:choose>

    </div>

    <div class="col-md-6">

        <label class="form-label">New Poster</label>

        <input
            type="file"
            name="poster"
            class="form-control"
            accept="image/*">

        <small class="text-muted">Bỏ trống nếu không muốn đổi poster.</small>

    </div>

    <div class="col-12 mt-4">

        <button type="submit" class="btn btn-warning">

            <i class="bi bi-save"></i> Update

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
