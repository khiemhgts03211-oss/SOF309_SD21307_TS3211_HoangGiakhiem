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

Check-in vé

</h3>

</div>

<div class="card-body">

<c:if test="${not empty error}">

<div class="alert alert-danger">

${error}

</div>

</c:if>

<c:if test="${not empty success}">

<div class="alert alert-success">

${success}

</div>

</c:if>

<form method="post"
      action="${pageContext.request.contextPath}/staff/checkin"
      class="row g-3 mb-4">

    <div class="col-md-8">

        <input
            type="text"
            name="bookingCode"
            class="form-control form-control-lg"
            placeholder="Quét hoặc nhập mã đặt vé..."
            value="${bookingCode}"
            required>

    </div>

    <div class="col-md-4">

        <button class="btn btn-success btn-lg w-100">

            <i class="bi bi-qr-code-scan"></i> Check-in

        </button>

    </div>

</form>

<c:if test="${not empty ticket}">

<table class="table table-borderless w-auto">

<tbody>

<tr>
<th width="180">Booking Code</th>
<td class="fw-bold fs-5">${ticket.bookingCode}</td>
</tr>

<tr>
<th>Customer</th>
<td>${ticket.customerName}</td>
</tr>

<tr>
<th>Movie</th>
<td>${ticket.movieTitle}</td>
</tr>

<tr>
<th>Room</th>
<td>${ticket.roomName}</td>
</tr>

<tr>
<th>Start Time</th>
<td><fmt:formatDate value="${ticket.startTime}" pattern="HH:mm dd/MM/yyyy"/></td>
</tr>

<tr>
<th>People Count</th>
<td>${ticket.peopleCount}</td>
</tr>

<tr>
<th>Status</th>
<td>

<span
class="badge
${ticket.status=='PAID'
?'bg-success'
:(ticket.status=='BOOKED'
?'bg-warning text-dark'
:(ticket.status=='USED'
?'bg-secondary'
:'bg-danger'))}">

${ticket.status}

</span>

</td>
</tr>

</tbody>

</table>

</c:if>

</div>

</div>

</div>

</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>

</body>

</html>
