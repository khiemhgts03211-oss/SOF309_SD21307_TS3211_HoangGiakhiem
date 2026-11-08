<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<link rel="stylesheet"
href="${pageContext.request.contextPath}/assets/css/admin.css">

<style>
@media print {
    .no-print { display: none !important; }
    .sidebar, nav.navbar { display: none !important; }
    .receipt { box-shadow: none !important; border: 1px solid #000; }
}
.receipt { max-width: 420px; margin: 0 auto; }
.receipt hr { border-top: 1px dashed #999; }
</style>

<body>

<jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

<div class="d-flex">

<jsp:include page="/WEB-INF/views/common/sidebar-staff.jsp"/>

<div class="flex-grow-1 p-4">

<div class="card shadow no-print">

<div class="card-header bg-dark text-white">

<h3 class="mb-0">

In vé

</h3>

</div>

<div class="card-body">

<c:if test="${not empty error}">

<div class="alert alert-danger">

${error}

</div>

</c:if>

<form method="get"
      action="${pageContext.request.contextPath}/staff/print-ticket"
      class="row g-3">

    <div class="col-md-8">

        <input
            type="text"
            name="bookingCode"
            class="form-control form-control-lg"
            placeholder="Nhập mã đặt vé..."
            value="${ticket.bookingCode}"
            required>

    </div>

    <div class="col-md-4">

        <button class="btn btn-primary btn-lg w-100">

            <i class="bi bi-search"></i> Tìm vé

        </button>

    </div>

</form>

</div>

</div>

<c:if test="${not empty ticket}">

<div class="card shadow receipt mt-4">

<div class="card-body">

<div class="text-center mb-3">

<h4 class="fw-bold mb-0">CineStar</h4>

<small class="text-muted">Hoá đơn vé xem phim</small>

</div>

<hr>

<table class="table table-borderless table-sm mb-0">

<tbody>

<tr>
<th width="140">Mã vé</th>
<td class="fw-bold">${ticket.bookingCode}</td>
</tr>

<tr>
<th>Khách hàng</th>
<td>${ticket.customerName}</td>
</tr>

<tr>
<th>Phim</th>
<td>${ticket.movieTitle}</td>
</tr>

<tr>
<th>Phòng</th>
<td>${ticket.roomName}</td>
</tr>

<tr>
<th>Suất chiếu</th>
<td><fmt:formatDate value="${ticket.startTime}" pattern="HH:mm dd/MM/yyyy"/></td>
</tr>

<tr>
<th>Số vé</th>
<td>${ticket.peopleCount}</td>
</tr>

</tbody>

</table>

<hr>

<c:if test="${not empty details}">

<table class="table table-sm mb-0">

<thead>

<tr>
<th>Dịch vụ</th>
<th class="text-center">SL</th>
<th class="text-end">Thành tiền</th>
</tr>

</thead>

<tbody>

<c:forEach var="d" items="${details}">

<tr>
<td>${d.service.serviceName}</td>
<td class="text-center">${d.quantity}</td>
<td class="text-end"><fmt:formatNumber value="${d.amount}" pattern="#,##0"/> đ</td>
</tr>

</c:forEach>

</tbody>

</table>

<hr>

</c:if>

<div class="d-flex justify-content-between fs-5 fw-bold">

<span>Tổng cộng</span>

<span class="text-success"><fmt:formatNumber value="${ticket.totalPrice}" pattern="#,##0"/> đ</span>

</div>

<p class="text-center text-muted mt-3 mb-0" style="font-size:.85rem">

Cảm ơn quý khách. Chúc xem phim vui vẻ!

</p>

</div>

</div>

<div class="text-center mt-3 no-print">

<button onclick="window.print()" class="btn btn-success btn-lg">

<i class="bi bi-printer"></i> In hoá đơn

</button>

</div>

</c:if>

</div>

</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>

</body>

</html>
