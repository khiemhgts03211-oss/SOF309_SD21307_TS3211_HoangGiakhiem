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

Movie Detail

</h3>

<a href="${pageContext.request.contextPath}/admin/movies" class="btn btn-secondary btn-sm">

<i class="bi bi-arrow-left"></i> Back

</a>

</div>

</div>

<div class="card-body">

<div class="row g-4">

<div class="col-md-4">

<c:choose>

<c:when test="${not empty movie.poster}">

<img
src="${pageContext.request.contextPath}/${movie.poster}"
class="img-fluid rounded shadow-sm"
alt="${movie.title}">

</c:when>

<c:otherwise>

<img
src="${pageContext.request.contextPath}/assets/images/logo.png"
class="img-fluid rounded shadow-sm"
alt="No poster">

</c:otherwise>

</c:choose>

</div>

<div class="col-md-8">

<table class="table table-borderless">

<tbody>

<tr>
<th width="180">Movie ID</th>
<td>${movie.movieId}</td>
</tr>

<tr>
<th>Title</th>
<td class="fw-bold fs-5">${movie.title}</td>
</tr>

<tr>
<th>Genre</th>
<td>${movie.genre}</td>
</tr>

<tr>
<th>Duration</th>
<td>${movie.duration} phút</td>
</tr>

<tr>
<th>Release Date</th>
<td>${movie.releaseDate}</td>
</tr>

<tr>
<th>Status</th>
<td>

<span
class="badge
${movie.status=='ACTIVE'
?'bg-success'
:(movie.status=='COMING_SOON'
?'bg-warning text-dark'
:'bg-secondary')}">

${movie.status}

</span>

</td>
</tr>

<tr>
<th>Description</th>
<td>${movie.description}</td>
</tr>

<tr>
<th>Created At</th>
<td>${movie.createdAt}</td>
</tr>

</tbody>

</table>

<a class="btn btn-warning" href="${pageContext.request.contextPath}/admin/movies/edit?id=${movie.movieId}">

<i class="bi bi-pencil-square"></i> Edit

</a>

</div>

</div>

</div>

</div>

    </div>

</div>

</body>
</html>
