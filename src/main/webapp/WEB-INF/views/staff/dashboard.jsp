<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<link rel="stylesheet"
href="${pageContext.request.contextPath}/assets/css/admin.css">

<body>

<jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

<div class="d-flex">

<jsp:include page="/WEB-INF/views/common/sidebar-staff.jsp"/>

<div class="flex-grow-1 p-4">

<h2 class="mb-4">
Trang Chủ Của Staff
</h2>

<div class="row g-4">

<div class="col-md-3">

<div class="card border-primary shadow">

<div class="card-body text-center">

<h5>Today's Tickets</h5>

<h2>${todayTickets}</h2>

</div>

</div>

</div>

<div class="col-md-3">

<div class="card border-success shadow">

<div class="card-body text-center">

<h5>Today's Revenue</h5>

<h2><fmt:formatNumber value="${todayRevenue}" pattern="#,##0"/> đ</h2>

</div>

</div>

</div>

<div class="col-md-3">

<div class="card border-warning shadow">

<div class="card-body text-center">

<h5>Today's Showtimes</h5>

<h2>${showtimeCount}</h2>

</div>

</div>

</div>

<div class="col-md-3">

<div class="card border-danger shadow">

<div class="card-body text-center">

<h5>Pending Payments</h5>

<h2>${pendingPayments}</h2>

</div>

</div>

</div>

</div>

<hr class="my-4">

<div class="card shadow">

<div class="card-header bg-dark text-white">

Today's Showtimes

</div>

<div class="card-body">

<table class="table table-bordered table-hover">

<thead class="table-dark">

<tr>

<th>ID</th>

<th>Movie</th>

<th>Room</th>

<th>Start Time</th>

<th>Status</th>

</tr>

</thead>

<tbody>

<c:forEach items="${todayShowtimes}" var="s">

<tr>

<td>${s.showtimeId}</td>

<td>${s.movieTitle}</td>

<td>${s.roomName}</td>

<td>${s.startTime}</td>

<td>

<span
class="badge
${s.status=='OPEN'
?'bg-success'
:(s.status=='CLOSED'
?'bg-secondary'
:'bg-danger')}">

${s.status}

</span>

</td>

</tr>

</c:forEach>

<c:if test="${empty todayShowtimes}">

<tr>

<td colspan="5" class="text-center text-muted">

Hôm nay không có suất chiếu nào.

</td>

</tr>

</c:if>

</tbody>

</table>

</div>

</div>

</div>

</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>

</body>

</html>
