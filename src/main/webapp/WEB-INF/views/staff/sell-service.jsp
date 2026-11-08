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

Bán dịch vụ theo vé

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

<form method="get"
      action="${pageContext.request.contextPath}/staff/sell-service"
      class="row g-3 mb-4">

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

<c:if test="${not empty ticket}">

<div class="row g-4">

<div class="col-md-5">

<table class="table table-borderless w-auto">

<tbody>

<tr>
<th width="140">Booking Code</th>
<td class="fw-bold">${ticket.bookingCode}</td>
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
<th>Total Price</th>
<td class="text-success fw-bold">
<fmt:formatNumber value="${ticket.totalPrice}" pattern="#,##0"/> đ
</td>
</tr>

</tbody>

</table>

<form method="post"
      action="${pageContext.request.contextPath}/staff/sell-service"
      class="row g-2">

    <input type="hidden" name="ticketId" value="${ticket.ticketId}">

    <div class="col-12">

        <label class="form-label">Dịch vụ</label>

        <select name="serviceId" class="form-select" required>

            <c:forEach var="s" items="${services}">

                <option value="${s.serviceId}">
                    ${s.serviceName} — <fmt:formatNumber value="${s.price}" pattern="#,##0"/> đ
                </option>

            </c:forEach>

        </select>

    </div>

    <div class="col-6">

        <label class="form-label">Số lượng</label>

        <input
            type="number"
            name="quantity"
            class="form-control"
            value="1"
            min="1"
            required>

    </div>

    <div class="col-6 d-flex align-items-end">

        <button class="btn btn-success w-100">

            <i class="bi bi-cart-plus"></i> Bán

        </button>

    </div>

</form>

</div>

<div class="col-md-7">

<h5 class="mb-3">Dịch vụ đã bán cho vé này</h5>

<table class="table table-bordered table-hover">

<thead class="table-dark">

<tr>

<th>Service</th>

<th>Quantity</th>

<th>Amount</th>

</tr>

</thead>

<tbody>

<c:forEach var="d" items="${details}">

<tr>

<td>${d.service.serviceName}</td>

<td>${d.quantity}</td>

<td><fmt:formatNumber value="${d.amount}" pattern="#,##0"/> đ</td>

</tr>

</c:forEach>

<c:if test="${empty details}">

<tr>

<td colspan="3" class="text-center text-muted">

Chưa bán dịch vụ nào cho vé này.

</td>

</tr>

</c:if>

</tbody>

</table>

</div>

</div>

</c:if>

</div>

</div>

</div>

</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>

</body>

</html>
