<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<!DOCTYPE html>
<html lang="vi">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1">

    <title>Thanh toán hôm nay</title>

    <link
        href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
        rel="stylesheet">

    <style>

        body {
            background-color: #f4f6f9;
        }

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

</head>

<body>

<div class="container-fluid py-4 px-3 px-md-4">

    <%-- HEADER --%>

    <div class="d-flex flex-wrap
                justify-content-between
                align-items-center
                gap-3 mb-4">

        <div>

            <h2 class="page-title mb-1">
                Thanh toán hôm nay
            </h2>

            <p class="text-muted mb-0">

                Danh sách giao dịch trong ngày

                <strong>
                    <fmt:formatDate
                        value="<%= new java.util.Date() %>"
                        pattern="dd/MM/yyyy"/>
                </strong>

            </p>

        </div>

        <div class="d-flex flex-wrap gap-2">

            <a
                href="${pageContext.request.contextPath}/staff/payments"
                class="btn btn-outline-primary">

                Tất cả thanh toán

            </a>

            <a
                href="${pageContext.request.contextPath}/staff/dashboard"
                class="btn btn-outline-secondary">

                Quay lại Dashboard

            </a>

        </div>

    </div>

    <%-- THÔNG BÁO --%>

    <c:if test="${not empty errorMessage}">

        <div class="alert alert-danger alert-dismissible fade show">

            <c:out value="${errorMessage}"/>

            <button
                type="button"
                class="btn-close"
                data-bs-dismiss="alert">
            </button>

        </div>

    </c:if>

    <c:if test="${param.success eq 'paid'}">

        <div class="alert alert-success alert-dismissible fade show">

            Xác nhận thanh toán thành công.

            <button
                type="button"
                class="btn-close"
                data-bs-dismiss="alert">
            </button>

        </div>

    </c:if>

    <c:if test="${param.success eq 'cancelled'}">

        <div class="alert alert-success alert-dismissible fade show">

            Hủy giao dịch thành công.

            <button
                type="button"
                class="btn-close"
                data-bs-dismiss="alert">
            </button>

        </div>

    </c:if>

    <c:if test="${param.success eq 'failed'}">

        <div class="alert alert-success alert-dismissible fade show">

            Đã chuyển giao dịch sang trạng thái FAILED.

            <button
                type="button"
                class="btn-close"
                data-bs-dismiss="alert">
            </button>

        </div>

    </c:if>

    <c:if test="${param.error eq 'update_failed'}">

        <div class="alert alert-danger alert-dismissible fade show">

            Cập nhật trạng thái thanh toán thất bại.

            <button
                type="button"
                class="btn-close"
                data-bs-dismiss="alert">
            </button>

        </div>

    </c:if>

    <c:if test="${param.error eq 'server_error'}">

        <div class="alert alert-danger alert-dismissible fade show">

            Đã xảy ra lỗi hệ thống.

            <button
                type="button"
                class="btn-close"
                data-bs-dismiss="alert">
            </button>

        </div>

    </c:if>

    <%-- THỐNG KÊ --%>

    <div class="row g-3 mb-4">

        <div class="col-xl col-md-4 col-sm-6">

            <div class="card shadow-sm summary-card h-100">

                <div class="card-body">

                    <div class="summary-label">
                        Tổng hôm nay
                    </div>

                    <p class="summary-number text-primary">
                        ${empty totalPayments ? 0 : totalPayments}
                    </p>

                </div>

            </div>

        </div>

        <div class="col-xl col-md-4 col-sm-6">

            <div class="card shadow-sm summary-card h-100">

                <div class="card-body">

                    <div class="summary-label">
                        PAID
                    </div>

                    <p class="summary-number text-success">
                        ${empty paidCount ? 0 : paidCount}
                    </p>

                </div>

            </div>

        </div>

        <div class="col-xl col-md-4 col-sm-6">

            <div class="card shadow-sm summary-card h-100">

                <div class="card-body">

                    <div class="summary-label">
                        PENDING
                    </div>

                    <p class="summary-number text-warning">
                        ${empty pendingCount ? 0 : pendingCount}
                    </p>

                </div>

            </div>

        </div>

        <div class="col-xl col-md-4 col-sm-6">

            <div class="card shadow-sm summary-card h-100">

                <div class="card-body">

                    <div class="summary-label">
                        FAILED
                    </div>

                    <p class="summary-number text-danger">
                        ${empty failedCount ? 0 : failedCount}
                    </p>

                </div>

            </div>

        </div>

        <div class="col-xl col-md-4 col-sm-6">

            <div class="card shadow-sm summary-card h-100">

                <div class="card-body">

                    <div class="summary-label">
                        CANCELLED
                    </div>

                    <p class="summary-number text-secondary">
                        ${empty cancelledCount ? 0 : cancelledCount}
                    </p>

                </div>

            </div>

        </div>

    </div>

    <%-- DOANH THU HÔM NAY --%>

    <div class="card shadow-sm payment-card mb-4">

        <div class="card-body">

            <div class="d-flex flex-wrap
                        justify-content-between
                        align-items-center
                        gap-3">

                <div>

                    <div class="text-muted">
                        Tổng doanh thu thanh toán hôm nay
                    </div>

                    <div class="fs-3 fw-bold text-success">

                        <fmt:formatNumber
                            value="${empty todayRevenue
                                ? 0
                                : todayRevenue}"
                            type="number"
                            groupingUsed="true"/>

                        VNĐ

                    </div>

                </div>

                <span class="badge bg-success fs-6">
                    Chỉ tính giao dịch PAID
                </span>

            </div>

        </div>

    </div>

    <%-- DANH SÁCH --%>

    <div class="card shadow-sm payment-card">

        <div class="card-header bg-white py-3">

            <div class="d-flex
                        justify-content-between
                        align-items-center">

                <h5 class="mb-0">
                    Giao dịch hôm nay
                </h5>

                <span class="badge bg-primary">

                    ${empty payments ? 0 : payments.size()}
                    giao dịch

                </span>

            </div>

        </div>

        <div class="card-body p-0">

            <div class="table-responsive">

                <table class="table
                              table-bordered
                              table-hover
                              align-middle
                              mb-0">

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

                                <td
                                    colspan="10"
                                    class="text-center
                                           text-muted
                                           empty-data">

                                    Hôm nay chưa có giao dịch thanh toán.

                                </td>

                            </tr>

                        </c:when>

                        <c:otherwise>

                            <c:forEach
                                items="${payments}"
                                var="payment">

                                <tr>

                                    <td>
                                        ${payment.paymentId}
                                    </td>

                                    <td>

                                        <span class="transaction-code">

                                            <c:out
                                                value="${payment.transactionCode}"/>

                                        </span>

                                    </td>

                                    <td>

                                        <c:choose>

                                            <c:when test="${not empty payment.bookingCode}">

                                                <span class="booking-code">

                                                    <c:out
                                                        value="${payment.bookingCode}"/>

                                                </span>

                                            </c:when>

                                            <c:otherwise>

                                                <span class="text-muted">
                                                    Ticket #${payment.ticketId}
                                                </span>

                                            </c:otherwise>

                                        </c:choose>

                                    </td>

                                    <td>

                                        <c:choose>

                                            <c:when test="${not empty payment.customerName}">

                                                <div class="customer-name">

                                                    <c:out
                                                        value="${payment.customerName}"/>

                                                </div>

                                                <c:if test="${not empty payment.customerEmail}">

                                                    <div class="customer-email">

                                                        <c:out
                                                            value="${payment.customerEmail}"/>

                                                    </div>

                                                </c:if>

                                            </c:when>

                                            <c:otherwise>

                                                <span class="text-muted">
                                                    Không xác định
                                                </span>

                                            </c:otherwise>

                                        </c:choose>

                                    </td>

                                    <td>

                                        <c:choose>

                                            <c:when test="${not empty payment.movieTitle}">

                                                <c:out
                                                    value="${payment.movieTitle}"/>

                                            </c:when>

                                            <c:otherwise>

                                                <span class="text-muted">
                                                    Không xác định
                                                </span>

                                            </c:otherwise>

                                        </c:choose>

                                    </td>

                                    <td>

                                        <span class="badge bg-secondary">

                                            <c:out
                                                value="${payment.paymentMethod}"/>

                                        </span>

                                    </td>

                                    <td class="text-end money-value">

                                        <fmt:formatNumber
                                            value="${empty payment.amount
                                                ? 0
                                                : payment.amount}"
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

                                                    <c:out
                                                        value="${payment.status}"/>

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

                                                <span class="text-muted">
                                                    Chưa thanh toán
                                                </span>

                                            </c:otherwise>

                                        </c:choose>

                                    </td>

                                    <td class="action-column">

                                        <div class="d-flex
                                                    justify-content-center
                                                    flex-wrap
                                                    gap-1">

                                            <a
                                                href="${pageContext.request.contextPath}/staff/payments/detail?id=${payment.paymentId}"
                                                class="btn btn-sm btn-outline-primary">

                                                Chi tiết

                                            </a>

                                            <c:if test="${payment.status eq 'PENDING'}">

                                                <form
                                                    action="${pageContext.request.contextPath}/staff/payments/status"
                                                    method="post">

                                                    <input
                                                        type="hidden"
                                                        name="paymentId"
                                                        value="${payment.paymentId}">

                                                    <input
                                                        type="hidden"
                                                        name="status"
                                                        value="PAID">

                                                    <input
                                                        type="hidden"
                                                        name="returnPage"
                                                        value="today">

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

                                                    <input
                                                        type="hidden"
                                                        name="paymentId"
                                                        value="${payment.paymentId}">

                                                    <input
                                                        type="hidden"
                                                        name="status"
                                                        value="CANCELLED">

                                                    <input
                                                        type="hidden"
                                                        name="returnPage"
                                                        value="today">

                                                    <button
                                                        type="submit"
                                                        class="btn btn-sm btn-outline-danger"
                                                        onclick="return confirm('Bạn có chắc muốn hủy giao dịch?');">

                                                        Hủy

                                                    </button>

                                                </form>

                                                <form
                                                    action="${pageContext.request.contextPath}/staff/payments/status"
                                                    method="post">

                                                    <input
                                                        type="hidden"
                                                        name="paymentId"
                                                        value="${payment.paymentId}">

                                                    <input
                                                        type="hidden"
                                                        name="status"
                                                        value="FAILED">

                                                    <input
                                                        type="hidden"
                                                        name="returnPage"
                                                        value="today">

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

<script
    src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js">
</script>

</body>

</html>