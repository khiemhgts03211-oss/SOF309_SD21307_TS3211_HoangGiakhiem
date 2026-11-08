<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<c:set var="pageTitle" value="Danh sách thanh toán" scope="request"/>

<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<link rel="stylesheet"
      href="${pageContext.request.contextPath}/assets/css/admin.css">

    <style>

        .page-title {
            font-weight: 700;
        }

        .payment-card,
        .summary-card {
            border: none;
            border-radius: 12px;
            overflow: hidden;
        }

        .summary-label {
            color: #6c757d;
            margin-bottom: 6px;
        }

        .summary-number {
            font-size: 27px;
            font-weight: 700;
            margin-bottom: 0;
        }

        .table th {
            white-space: nowrap;
            vertical-align: middle;
        }

        .table td {
            vertical-align: middle;
        }

        .transaction-code {
            color: #0d6efd;
            font-weight: 700;
        }

        .booking-code {
            color: #6f42c1;
            font-weight: 700;
        }

        .customer-name {
            font-weight: 600;
        }

        .customer-email {
            color: #6c757d;
            font-size: 13px;
        }

        .money-value {
            color: #198754;
            font-weight: 700;
            white-space: nowrap;
        }

        .empty-data {
            padding: 50px 15px !important;
        }

        .action-column {
            min-width: 380px;
        }

        .action-column form {
            margin: 0;
        }

    </style>

<body>

<jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

<div class="d-flex">

<jsp:include page="/WEB-INF/views/common/sidebar-staff.jsp"/>

<div class="flex-grow-1 p-4">

    <div class="d-flex flex-wrap
                justify-content-between
                align-items-center
                gap-3 mb-4">

        <div>

            <h2 class="page-title mb-1">
                Danh sách thanh toán
            </h2>

            <p class="text-muted mb-0">
                Tìm kiếm, lọc và xử lý giao dịch thanh toán
            </p>

        </div>

        <div class="d-flex gap-2">

            <a
                href="${pageContext.request.contextPath}/staff/payments/today"
                class="btn btn-outline-primary">

                Thanh toán hôm nay

            </a>

            <a
                href="${pageContext.request.contextPath}/staff/dashboard"
                class="btn btn-outline-secondary">

                Quay lại Dashboard

            </a>

        </div>

    </div>

    <%-- THÔNG BÁO THÀNH CÔNG --%>

    <c:if test="${param.success eq 'paid'}">

        <div class="alert alert-success alert-dismissible fade show">

            Xác nhận thanh toán thành công.

            <button type="button"
                    class="btn-close"
                    data-bs-dismiss="alert">
            </button>

        </div>

    </c:if>

    <c:if test="${param.success eq 'cancelled'}">

        <div class="alert alert-success alert-dismissible fade show">

            Hủy giao dịch thanh toán thành công.

            <button type="button"
                    class="btn-close"
                    data-bs-dismiss="alert">
            </button>

        </div>

    </c:if>

    <c:if test="${param.success eq 'failed'}">

        <div class="alert alert-success alert-dismissible fade show">

            Đã cập nhật giao dịch sang trạng thái FAILED.

            <button type="button"
                    class="btn-close"
                    data-bs-dismiss="alert">
            </button>

        </div>

    </c:if>

    <%-- THÔNG BÁO LỖI --%>

    <c:if test="${not empty errorMessage}">

        <div class="alert alert-danger alert-dismissible fade show">

            <c:out value="${errorMessage}"/>

            <button type="button"
                    class="btn-close"
                    data-bs-dismiss="alert">
            </button>

        </div>

    </c:if>

    <c:if test="${not empty warningMessage}">

        <div class="alert alert-warning alert-dismissible fade show">

            <c:out value="${warningMessage}"/>

            <button type="button"
                    class="btn-close"
                    data-bs-dismiss="alert">
            </button>

        </div>

    </c:if>

    <c:if test="${param.error eq 'invalid_id'}">
        <div class="alert alert-warning">
            Mã thanh toán không hợp lệ.
        </div>
    </c:if>

    <c:if test="${param.error eq 'not_found'}">
        <div class="alert alert-warning">
            Không tìm thấy giao dịch thanh toán.
        </div>
    </c:if>

    <c:if test="${param.error eq 'invalid_status'}">
        <div class="alert alert-warning">
            Trạng thái yêu cầu không hợp lệ.
        </div>
    </c:if>

    <c:if test="${param.error eq 'already_paid'}">
        <div class="alert alert-info">
            Giao dịch này đã được thanh toán.
        </div>
    </c:if>

    <c:if test="${param.error eq 'already_cancelled'}">
        <div class="alert alert-info">
            Giao dịch này đã bị hủy.
        </div>
    </c:if>

    <c:if test="${param.error eq 'already_failed'}">
        <div class="alert alert-info">
            Giao dịch này đã ở trạng thái FAILED.
        </div>
    </c:if>

    <c:if test="${param.error eq 'cannot_confirm'}">
        <div class="alert alert-warning">
            Chỉ giao dịch PENDING mới được xác nhận.
        </div>
    </c:if>

    <c:if test="${param.error eq 'cannot_cancel'}">
        <div class="alert alert-warning">
            Chỉ giao dịch PENDING mới được hủy.
        </div>
    </c:if>

    <c:if test="${param.error eq 'cannot_fail'}">
        <div class="alert alert-warning">
            Chỉ giao dịch PENDING mới được đánh dấu thất bại.
        </div>
    </c:if>

    <c:if test="${param.error eq 'ticket_not_found'}">
        <div class="alert alert-danger">
            Không tìm thấy vé liên kết.
        </div>
    </c:if>

    <c:if test="${param.error eq 'ticket_cancelled'}">
        <div class="alert alert-danger">
            Không thể xác nhận vì vé đã bị hủy.
        </div>
    </c:if>

    <c:if test="${param.error eq 'invalid_amount'}">
        <div class="alert alert-danger">
            Số tiền thanh toán không hợp lệ.
        </div>
    </c:if>

    <c:if test="${param.error eq 'insufficient_amount'}">
        <div class="alert alert-danger">
            Số tiền thanh toán nhỏ hơn tổng tiền vé.
        </div>
    </c:if>

    <c:if test="${param.error eq 'ticket_update_failed'}">
        <div class="alert alert-warning">
            Payment đã PAID nhưng trạng thái vé chưa cập nhật.
        </div>
    </c:if>

    <c:if test="${param.error eq 'update_failed'}">
        <div class="alert alert-danger">
            Cập nhật trạng thái thanh toán thất bại.
        </div>
    </c:if>

    <c:if test="${param.error eq 'server_error'}">
        <div class="alert alert-danger">
            Đã xảy ra lỗi hệ thống.
        </div>
    </c:if>

    <%-- THỐNG KÊ --%>

    <div class="row g-3 mb-4">

        <div class="col-xl col-md-4 col-sm-6">
            <div class="card shadow-sm summary-card">
                <div class="card-body">
                    <div class="summary-label">
                        Tổng giao dịch
                    </div>
                    <p class="summary-number text-primary">
                        ${empty totalPayments ? 0 : totalPayments}
                    </p>
                </div>
            </div>
        </div>

        <div class="col-xl col-md-4 col-sm-6">
            <div class="card shadow-sm summary-card">
                <div class="card-body">
                    <div class="summary-label">PAID</div>
                    <p class="summary-number text-success">
                        ${empty paidCount ? 0 : paidCount}
                    </p>
                </div>
            </div>
        </div>

        <div class="col-xl col-md-4 col-sm-6">
            <div class="card shadow-sm summary-card">
                <div class="card-body">
                    <div class="summary-label">PENDING</div>
                    <p class="summary-number text-warning">
                        ${empty pendingCount ? 0 : pendingCount}
                    </p>
                </div>
            </div>
        </div>

        <div class="col-xl col-md-4 col-sm-6">
            <div class="card shadow-sm summary-card">
                <div class="card-body">
                    <div class="summary-label">FAILED</div>
                    <p class="summary-number text-danger">
                        ${empty failedCount ? 0 : failedCount}
                    </p>
                </div>
            </div>
        </div>

        <div class="col-xl col-md-4 col-sm-6">
            <div class="card shadow-sm summary-card">
                <div class="card-body">
                    <div class="summary-label">CANCELLED</div>
                    <p class="summary-number text-secondary">
                        ${empty cancelledCount ? 0 : cancelledCount}
                    </p>
                </div>
            </div>
        </div>

    </div>

    <%-- TÌM KIẾM VÀ LỌC --%>

    <div class="card shadow-sm payment-card mb-4">

        <div class="card-header bg-white">
            <strong>Tìm kiếm và lọc thanh toán</strong>
        </div>

        <div class="card-body">

            <form
                action="${pageContext.request.contextPath}/staff/payments"
                method="get">

                <div class="row g-3 align-items-end">

                    <div class="col-lg-5">

                        <label class="form-label">
                            Từ khóa
                        </label>

                        <input
                            type="text"
                            name="keyword"
                            class="form-control"
                            maxlength="150"
                            value="<c:out value='${keyword}'/>"
                            placeholder="Mã giao dịch, mã vé, khách hàng, email, phim...">

                    </div>

                    <div class="col-lg-2">

                        <label class="form-label">
                            Trạng thái
                        </label>

                        <select
                            name="status"
                            class="form-select">

                            <option value="">
                                Tất cả
                            </option>

                            <option value="PENDING"
                                ${selectedStatus eq 'PENDING'
                                    ? 'selected' : ''}>
                                PENDING
                            </option>

                            <option value="PAID"
                                ${selectedStatus eq 'PAID'
                                    ? 'selected' : ''}>
                                PAID
                            </option>

                            <option value="FAILED"
                                ${selectedStatus eq 'FAILED'
                                    ? 'selected' : ''}>
                                FAILED
                            </option>

                            <option value="CANCELLED"
                                ${selectedStatus eq 'CANCELLED'
                                    ? 'selected' : ''}>
                                CANCELLED
                            </option>

                        </select>

                    </div>

                    <div class="col-lg-2">

                        <label class="form-label">
                            Phương thức
                        </label>

                        <select
                            name="method"
                            class="form-select">

                            <option value="">
                                Tất cả
                            </option>

                            <option value="CASH"
                                ${selectedMethod eq 'CASH'
                                    ? 'selected' : ''}>
                                CASH
                            </option>

                            <option value="BANKING"
                                ${selectedMethod eq 'BANKING'
                                    ? 'selected' : ''}>
                                BANKING
                            </option>

                            <option value="MOMO"
                                ${selectedMethod eq 'MOMO'
                                    ? 'selected' : ''}>
                                MOMO
                            </option>

                            <option value="VNPAY"
                                ${selectedMethod eq 'VNPAY'
                                    ? 'selected' : ''}>
                                VNPAY
                            </option>

                        </select>

                    </div>

                    <div class="col-lg-3">

                        <div class="d-flex gap-2">

                            <button
                                type="submit"
                                class="btn btn-primary flex-grow-1">

                                Tìm kiếm

                            </button>

                            <a
                                href="${pageContext.request.contextPath}/staff/payments"
                                class="btn btn-outline-secondary">

                                Làm mới

                            </a>

                        </div>

                    </div>

                </div>

            </form>

        </div>

    </div>

    <%-- BẢNG --%>

    <div class="card shadow-sm payment-card">

        <div class="card-header bg-white d-flex
                    justify-content-between align-items-center">

            <strong>Danh sách giao dịch</strong>

            <span class="badge bg-primary">
                ${empty payments ? 0 : payments.size()}
                giao dịch
            </span>

        </div>

        <div class="card-body p-0">

            <div class="table-responsive">

                <table class="table table-bordered
                              table-hover align-middle mb-0">

                    <thead class="table-dark">

                    <tr>
                        <th>ID</th>
                        <th>Mã giao dịch</th>
                        <th>Mã đặt vé</th>
                        <th>Khách hàng</th>
                        <th>Phim</th>
                        <th>Phương thức</th>
                        <th class="text-end">Số tiền</th>
                        <th>Trạng thái</th>
                        <th>Thời gian</th>
                        <th class="text-center">Thao tác</th>
                    </tr>

                    </thead>

                    <tbody>

                    <c:choose>

                        <c:when test="${empty payments}">

                            <tr>
                                <td colspan="10"
                                    class="text-center text-muted empty-data">

                                    Không có giao dịch phù hợp.

                                </td>
                            </tr>

                        </c:when>

                        <c:otherwise>

                            <c:forEach
                                items="${payments}"
                                var="payment">

                                <tr>

                                    <td>${payment.paymentId}</td>

                                    <td class="transaction-code">
                                        <c:out value="${payment.transactionCode}"/>
                                    </td>

                                    <td>

                                        <c:choose>

                                            <c:when test="${not empty payment.bookingCode}">
                                                <span class="booking-code">
                                                    <c:out value="${payment.bookingCode}"/>
                                                </span>
                                            </c:when>

                                            <c:otherwise>
                                                Ticket #${payment.ticketId}
                                            </c:otherwise>

                                        </c:choose>

                                    </td>

                                    <td>

                                        <div class="customer-name">
                                            <c:out value="${payment.customerName}"/>
                                        </div>

                                        <div class="customer-email">
                                            <c:out value="${payment.customerEmail}"/>
                                        </div>

                                    </td>

                                    <td>
                                        <c:out value="${payment.movieTitle}"/>
                                    </td>

                                    <td>
                                        <span class="badge bg-secondary">
                                            <c:out value="${payment.paymentMethod}"/>
                                        </span>
                                    </td>

                                    <td class="text-end money-value">

                                        <fmt:formatNumber
                                            value="${empty payment.amount
                                                ? 0 : payment.amount}"
                                            type="number"
                                            groupingUsed="true"/>

                                        VNĐ

                                    </td>

                                    <td>

                                        <c:choose>

                                            <c:when test="${payment.status eq 'PAID'}">
                                                <span class="badge bg-success">
                                                    PAID
                                                </span>
                                            </c:when>

                                            <c:when test="${payment.status eq 'PENDING'}">
                                                <span class="badge bg-warning text-dark">
                                                    PENDING
                                                </span>
                                            </c:when>

                                            <c:when test="${payment.status eq 'FAILED'}">
                                                <span class="badge bg-danger">
                                                    FAILED
                                                </span>
                                            </c:when>

                                            <c:otherwise>
                                                <span class="badge bg-secondary">
                                                    <c:out value="${payment.status}"/>
                                                </span>
                                            </c:otherwise>

                                        </c:choose>

                                    </td>

                                    <td>

                                        <c:choose>

                                            <c:when test="${not empty payment.paidAt}">

                                                <fmt:formatDate
                                                    value="${payment.paidAt}"
                                                    pattern="dd/MM/yyyy HH:mm:ss"/>

                                            </c:when>

                                            <c:otherwise>
                                                Chưa thanh toán
                                            </c:otherwise>

                                        </c:choose>

                                    </td>

                                    <td class="action-column">

                                        <div class="d-flex
                                                    justify-content-center
                                                    flex-wrap gap-1">

                                            <a
                                                href="${pageContext.request.contextPath}/staff/payments/detail?id=${payment.paymentId}"
                                                class="btn btn-sm btn-outline-primary">

                                                Chi tiết

                                            </a>

                                            <c:if test="${payment.status eq 'PENDING'}">

                                                <form
                                                    action="${pageContext.request.contextPath}/staff/payments/status"
                                                    method="post">

                                                    <input type="hidden"
                                                           name="paymentId"
                                                           value="${payment.paymentId}">

                                                    <input type="hidden"
                                                           name="status"
                                                           value="PAID">

                                                    <input type="hidden"
                                                           name="returnPage"
                                                           value="list">

                                                    <input type="hidden"
                                                           name="keyword"
                                                           value="<c:out value='${keyword}'/>">

                                                    <input type="hidden"
                                                           name="selectedStatus"
                                                           value="<c:out value='${selectedStatus}'/>">

                                                    <input type="hidden"
                                                           name="selectedMethod"
                                                           value="<c:out value='${selectedMethod}'/>">

                                                    <button
                                                        type="submit"
                                                        class="btn btn-sm btn-outline-success"
                                                        onclick="return confirm('Xác nhận giao dịch đã thanh toán?');">

                                                        Xác nhận

                                                    </button>

                                                </form>

                                                <form
                                                    action="${pageContext.request.contextPath}/staff/payments/status"
                                                    method="post">

                                                    <input type="hidden"
                                                           name="paymentId"
                                                           value="${payment.paymentId}">

                                                    <input type="hidden"
                                                           name="status"
                                                           value="CANCELLED">

                                                    <input type="hidden"
                                                           name="returnPage"
                                                           value="list">

                                                    <input type="hidden"
                                                           name="keyword"
                                                           value="<c:out value='${keyword}'/>">

                                                    <input type="hidden"
                                                           name="selectedStatus"
                                                           value="<c:out value='${selectedStatus}'/>">

                                                    <input type="hidden"
                                                           name="selectedMethod"
                                                           value="<c:out value='${selectedMethod}'/>">

                                                    <button
                                                        type="submit"
                                                        class="btn btn-sm btn-outline-danger"
                                                        onclick="return confirm('Bạn có chắc muốn hủy giao dịch này?');">

                                                        Hủy

                                                    </button>

                                                </form>

                                                <form
                                                    action="${pageContext.request.contextPath}/staff/payments/status"
                                                    method="post">

                                                    <input type="hidden"
                                                           name="paymentId"
                                                           value="${payment.paymentId}">

                                                    <input type="hidden"
                                                           name="status"
                                                           value="FAILED">

                                                    <input type="hidden"
                                                           name="returnPage"
                                                           value="list">

                                                    <input type="hidden"
                                                           name="keyword"
                                                           value="<c:out value='${keyword}'/>">

                                                    <input type="hidden"
                                                           name="selectedStatus"
                                                           value="<c:out value='${selectedStatus}'/>">

                                                    <input type="hidden"
                                                           name="selectedMethod"
                                                           value="<c:out value='${selectedMethod}'/>">

                                                    <button
                                                        type="submit"
                                                        class="btn btn-sm btn-outline-warning"
                                                        onclick="return confirm('Đánh dấu giao dịch này là thất bại?');">

                                                        Thất bại

                                                    </button>

                                                </form>

                                            </c:if>

                                        </div>

                                    </td>

                                </tr>

                            </c:forEach>

                        </c:otherwise>

                    </c:choose>

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