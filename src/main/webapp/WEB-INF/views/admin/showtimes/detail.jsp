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

Showtime Detail

</h3>

<a href="${pageContext.request.contextPath}/admin/showtimes" class="btn btn-secondary btn-sm">

<i class="bi bi-arrow-left"></i> Back

</a>

</div>

</div>

<div class="card-body">

<table class="table table-borderless w-auto">

<tbody>

<tr>
<th width="180">Showtime ID</th>
<td>${showtime.showtimeId}</td>
</tr>

<tr>
<th>Movie</th>
<td class="fw-bold fs-5">${showtime.movieTitle}</td>
</tr>

<tr>
<th>Room</th>
<td>${showtime.roomName}</td>
</tr>

<tr>
<th>Show Date</th>
<td>${showtime.showDate}</td>
</tr>

<tr>
<th>Start Time</th>
<td>${showtime.startTime}</td>
</tr>

<tr>
<th>End Time</th>
<td>${showtime.endTime}</td>
</tr>

<tr>
<th>Ticket Price</th>
<td><fmt:formatNumber value="${showtime.ticketPrice}" pattern="#,##0"/> đ</td>
</tr>

<tr>
<th>Status</th>
<td>

<span
class="badge
${showtime.status=='OPEN'
?'bg-success'
:(showtime.status=='CLOSED'
?'bg-secondary'
:'bg-danger')}">

${showtime.status}

</span>

</td>
</tr>

<tr>
<th>Created At</th>
<td>${showtime.createdAt}</td>
</tr>

</tbody>

</table>

<a class="btn btn-warning" href="${pageContext.request.contextPath}/admin/showtimes/edit?id=${showtime.showtimeId}">

<i class="bi bi-pencil-square"></i> Edit

</a>

</div>

</div>

    </div>

</div>

</body>
</html>
