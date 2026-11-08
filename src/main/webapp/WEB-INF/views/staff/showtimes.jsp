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

<div class="card shadow">

<div class="card-header bg-dark text-white">

<h3 class="mb-0">

Suất chiếu hôm nay

</h3>

</div>

<div class="card-body">

<div class="table-responsive">

<table class="table table-bordered table-hover">

<thead class="table-dark">

<tr>

<th>ID</th>

<th>Movie</th>

<th>Room</th>

<th>Start Time</th>

<th>End Time</th>

<th>Price</th>

<th>Status</th>

</tr>

</thead>

<tbody>

<c:forEach var="s" items="${showtimes}">

<tr>

<td>${s.showtimeId}</td>

<td>${s.movieTitle}</td>

<td>${s.roomName}</td>

<td>${s.startTime}</td>

<td>${s.endTime}</td>

<td><fmt:formatNumber value="${s.ticketPrice}" pattern="#,##0"/> đ</td>

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

<c:if test="${empty showtimes}">

<tr>

<td colspan="7" class="text-center text-muted">

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

</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>

</body>

</html>
