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

    <title>Chi tiết giao dịch thanh toán</title>

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

        .info-card {
            border: none;
            border-radius: 12px;
            overflow: hidden;
        }

        .info-card .card-header {
            font-weight: 700;
        }

        .info-row {
            display: flex;
            justify-content: space-between;
            gap: 12px;
            padding: 10px 0;
            border-bottom: 1px solid #f0f0f0;
        }

        .info-row:last-child {
            border-bottom: none;
        }

        .info-label {
            color: #6c757d;
        }

        .info-value {
            font-weight: 600;
            text-align: right;
        }

        .transaction-code {
            color: #0d6efd;
            font-weight: 700;
        }

        .booking-code {
            color: #6f42c1;
            font-weight: 700;
        }

        .money-value {
            color: #198754;
            font-weight: 700;
        }

    </style>

</head>

<body>

<div class="container-fluid py-4 px-3 px-md-4">

    <div class="d-flex flex-wrap
                justify-content-between
                align-items-center
                gap-3 mb-4">

        <div>

            <h2 class="page-title mb-1">
                Chi tiết giao dịch thanh toán
            </h2>

            <p class="text-muted mb-0">
                Thông tin đầy đủ của một giao dịch thanh toán
            </p>

        </div>

        <div class="d-flex gap-2">

            <a
                href="${pageContext.request.contextPath}/staff/payments"
                class="btn btn-outline-secondary">

                &larr; Quay lại danh sách

            </a>

        </div>

    </div>

    <c:if test="${empty payment}">

        <div class="alert alert-warning">
            Không tìm thấy giao dịch thanh toán.
        </div>

    </c:if>

    <c:if test="${not empty payment}">

    <div class="row g-4">

        <div class="col-lg-6">

            <div class="card info-card shadow-sm h-100">

                <div class="card-header bg-dark text-white">
                    Thông tin giao dịch
                </div>

                <div class="card-body">

                    <div class="info-row">
                        <span class="info-label">Mã giao dịch</span>
                        <span class="info-value transaction-code">
                            <c:out value="${payment.transactionCode}"/>
                        </span>
                    </div>

                    <div class="info-row">
                        <span class="info-label">Mã đặt vé</span>
                        <span class="info-value booking-code">
                            <c:out value="${payment.bookingCode}"/>
                        </span>
                    </div>

                    <div class="info-row">
                        <span class="info-label">Phương thức</span>
                        <span class="info-value">
                            <span class="badge bg-secondary">
                                <c:out value="${payment.paymentMethod}"/>
                            </span>
                        </span>
                    </div>

                    <div class="info-row">
                        <span class="info-label">Số tiền</span>
                        <span class="info-value money-value">
                            <fmt:formatNumber
                                value="${empty payment.amount ? 0 : payment.amount}"
                                type="number"
                                groupingUsed="true"/> VNĐ
                        </span>
                    </div>

                    <div class="info-row">
                        <span class="info-label">Trạng thái</span>
                        <span class="info-value">

                            <c:choose>

                                <c:when test="${payment.status eq 'PAID'}">
                                    <span class="badge bg-success">PAID</span>
                                </c:when>

                                <c:when test="${payment.status eq 'PENDING'}">
                                    <span class="badge bg-warning text-dark">PENDING</span>
                                </c:when>

                                <c:when test="${payment.status eq 'FAILED'}">
                                    <span class="badge bg-danger">FAILED</span>
                                </c:when>

                                <c:otherwise>
                                    <span class="badge bg-secondary">
                                        <c:out value="${payment.status}"/>
                                    </span>
                                </c:otherwise>

                            </c:choose>

                        </span>
                    </div>

                    <div class="info-row">
                        <span class="info-label">Thời gian thanh toán</span>
                        <span class="info-value">

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

                        </span>
                    </div>

                    <div class="info-row">
                        <span class="info-label">Tạo lúc</span>
                        <span class="info-value">
                            <fmt:formatDate
                                value="${payment.createdAt}"
                                pattern="dd/MM/yyyy HH:mm:ss"/>
                        </span>
                    </div>

                </div>

            </div>

        </div>

        <div class="col-lg-6">

            <div class="card info-card shadow-sm h-100">

                <div class="card-header bg-dark text-white">
                    Thông tin khách hàng &amp; vé
                </div>

                <div class="card-body">

                    <div class="info-row">
                        <span class="info-label">Khách hàng</span>
                        <span class="info-value">
                            <c:out value="${payment.customerName}"/>
                        </span>
                    </div>

                    <div class="info-row">
                        <span class="info-label">Email</span>
                        <span class="info-value">
                            <c:out value="${payment.customerEmail}"/>
                        </span>
                    </div>

                    <div class="info-row">
                        <span class="info-label">Số điện thoại</span>
                        <span class="info-value">
                            <c:out value="${payment.customerPhone}"/>
                        </span>
                    </div>

                    <div class="info-row">
                        <span class="info-label">Phim</span>
                        <span class="info-value">
                            <c:out value="${payment.movieTitle}"/>
                        </span>
                    </div>

                    <div class="info-row">
                        <span class="info-label">Phòng chiếu</span>
                        <span class="info-value">
                            <c:out value="${payment.roomName}"/>
                        </span>
                    </div>

                    <div class="info-row">
                        <span class="info-label">Suất chiếu</span>
                        <span class="info-value">

                            <fmt:formatDate
                                value="${payment.showDate}"
                                pattern="dd/MM/yyyy"/>

                            &nbsp;

                            <fmt:formatDate
                                value="${payment.startTime}"
                                pattern="HH:mm"/>

                            -

                            <fmt:formatDate
                                value="${payment.endTime}"
                                pattern="HH:mm"/>

                        </span>
                    </div>

                    <div class="info-row">
                        <span class="info-label">Số lượng vé</span>
                        <span class="info-value">
                            <c:out value="${payment.peopleCount}"/>
                        </span>
                    </div>

                    <div class="info-row">
                        <span class="info-label">Tổng tiền vé</span>
                        <span class="info-value money-value">
                            <fmt:formatNumber
                                value="${empty payment.ticketTotalPrice
                                    ? 0 : payment.ticketTotalPrice}"
                                type="number"
                                groupingUsed="true"/> VNĐ
                        </span>
                    </div>

                    <div class="info-row">
                        <span class="info-label">Trạng thái vé</span>
                        <span class="info-value">

                            <c:choose>

                                <c:when test="${payment.ticketStatus eq 'PAID'}">
                                    <span class="badge bg-success">PAID</span>
                                </c:when>

                                <c:when test="${payment.ticketStatus eq 'BOOKED'}">
                                    <span class="badge bg-warning text-dark">BOOKED</span>
                                </c:when>

                                <c:when test="${payment.ticketStatus eq 'USED'}">
                                    <span class="badge bg-secondary">USED</span>
                                </c:when>

                                <c:when test="${payment.ticketStatus eq 'CANCELLED'}">
                                    <span class="badge bg-danger">CANCELLED</span>
                                </c:when>

                                <c:otherwise>
                                    <span class="badge bg-secondary">
                                        <c:out value="${payment.ticketStatus}"/>
                                    </span>
                                </c:otherwise>

                            </c:choose>

                        </span>
                    </div>

                </div>

            </div>

        </div>

    </div>

    <div class="d-flex gap-2 mt-4">

        <a
            href="${pageContext.request.contextPath}/staff/print-ticket?bookingCode=${payment.bookingCode}"
            class="btn btn-success">

            In vé

        </a>

        <a
            href="${pageContext.request.contextPath}/staff/search-ticket?bookingCode=${payment.bookingCode}"
            class="btn btn-outline-primary">

            Xem vé liên quan

        </a>

    </div>

    </c:if>

</div>

</body>
</html>
