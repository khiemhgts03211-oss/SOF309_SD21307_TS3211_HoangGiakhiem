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

    <title>Chi tiết khách hàng</title>

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

        .detail-card,
        .summary-card {
            border: none;
            border-radius: 12px;
            overflow: hidden;
        }

        .detail-label {
            color: #6c757d;
            font-size: 14px;
            margin-bottom: 4px;
        }

        .detail-value {
            font-size: 16px;
            font-weight: 600;
            margin-bottom: 0;
            word-break: break-word;
        }

        .summary-label {
            color: #6c757d;
            margin-bottom: 8px;
        }

        .summary-number {
            font-size: 27px;
            font-weight: 700;
            margin-bottom: 0;
        }

        .booking-code {
            color: #0d6efd;
            font-weight: 700;
        }

        .money-value {
            color: #198754;
            font-weight: 700;
        }

        .table th {
            white-space: nowrap;
            vertical-align: middle;
        }

        .table td {
            vertical-align: middle;
        }

        .empty-data {
            padding: 45px 15px !important;
        }

        .action-area form {
            margin: 0;
        }

    </style>

</head>

<body>

<div class="container-fluid py-4 px-3 px-md-4">

    <%-- Header --%>

    <div class="d-flex flex-wrap
                justify-content-between
                align-items-center
                gap-3 mb-4">

        <div>

            <h2 class="page-title mb-1">
                Chi tiết khách hàng
            </h2>

            <p class="text-muted mb-0">
                Thông tin tài khoản, thống kê và lịch sử đặt vé
            </p>

        </div>

        <div class="d-flex flex-wrap gap-2">

            <c:if test="${not empty customer}">

                <a
                    href="${pageContext.request.contextPath}/staff/customers/edit?id=${customer.userId}"
                    class="btn btn-warning">

                    Cập nhật thông tin

                </a>

            </c:if>

            <a
                href="${pageContext.request.contextPath}/staff/customers"
                class="btn btn-outline-secondary">

                Quay lại danh sách

            </a>

        </div>

    </div>

    <%-- Thông báo --%>

    <c:if test="${not empty errorMessage}">

        <div class="alert alert-danger alert-dismissible fade show">

            <c:out value="${errorMessage}"/>

            <button type="button"
                    class="btn-close"
                    data-bs-dismiss="alert">
            </button>

        </div>

    </c:if>

    <c:if test="${param.success eq 'updated'}">

        <div class="alert alert-success alert-dismissible fade show">

            Cập nhật thông tin khách hàng thành công.

            <button type="button"
                    class="btn-close"
                    data-bs-dismiss="alert">
            </button>

        </div>

    </c:if>

    <c:if test="${param.success eq 'locked'}">

        <div class="alert alert-success alert-dismissible fade show">

            Khóa tài khoản khách hàng thành công.

            <button type="button"
                    class="btn-close"
                    data-bs-dismiss="alert">
            </button>

        </div>

    </c:if>

    <c:if test="${param.success eq 'unlocked'}">

        <div class="alert alert-success alert-dismissible fade show">

            Mở khóa tài khoản khách hàng thành công.

            <button type="button"
                    class="btn-close"
                    data-bs-dismiss="alert">
            </button>

        </div>

    </c:if>

    <c:if test="${param.error eq 'update_failed'}">

        <div class="alert alert-danger alert-dismissible fade show">

            Cập nhật trạng thái tài khoản thất bại.

            <button type="button"
                    class="btn-close"
                    data-bs-dismiss="alert">
            </button>

        </div>

    </c:if>

    <c:if test="${param.error eq 'already_locked'}">

        <div class="alert alert-info alert-dismissible fade show">

            Tài khoản này đã bị khóa trước đó.

            <button type="button"
                    class="btn-close"
                    data-bs-dismiss="alert">
            </button>

        </div>

    </c:if>

    <c:if test="${param.error eq 'already_active'}">

        <div class="alert alert-info alert-dismissible fade show">

            Tài khoản này đang hoạt động.

            <button type="button"
                    class="btn-close"
                    data-bs-dismiss="alert">
            </button>

        </div>

    </c:if>

    <c:if test="${param.error eq 'server_error'}">

        <div class="alert alert-danger alert-dismissible fade show">

            Đã xảy ra lỗi hệ thống. Vui lòng thử lại.

            <button type="button"
                    class="btn-close"
                    data-bs-dismiss="alert">
            </button>

        </div>

    </c:if>

    <c:choose>

        <c:when test="${empty customer}">

            <div class="alert alert-warning">

                Không tìm thấy thông tin khách hàng.

            </div>

        </c:when>

        <c:otherwise>

            <%-- Thông tin tài khoản --%>

            <div class="card shadow-sm detail-card mb-4">

                <div class="card-header bg-white py-3">

                    <div class="d-flex flex-wrap
                                justify-content-between
                                align-items-center
                                gap-2">

                        <h5 class="mb-0">
                            Thông tin tài khoản
                        </h5>

                        <c:choose>

                            <c:when test="${customer.status eq 'ACTIVE'}">

                                <span class="badge bg-success">
                                    ACTIVE
                                </span>

                            </c:when>

                            <c:when test="${customer.status eq 'INACTIVE'}">

                                <span class="badge bg-warning text-dark">
                                    INACTIVE
                                </span>

                            </c:when>

                            <c:when test="${customer.status eq 'LOCKED'}">

                                <span class="badge bg-danger">
                                    LOCKED
                                </span>

                            </c:when>

                            <c:otherwise>

                                <span class="badge bg-secondary">

                                    <c:out value="${customer.status}"/>

                                </span>

                            </c:otherwise>

                        </c:choose>

                    </div>

                </div>

                <div class="card-body">

                    <div class="row g-4">

                        <div class="col-lg-3 col-md-6">

                            <div class="detail-label">
                                User ID
                            </div>

                            <p class="detail-value">
                                #${customer.userId}
                            </p>

                        </div>

                        <div class="col-lg-3 col-md-6">

                            <div class="detail-label">
                                Username
                            </div>

                            <p class="detail-value">

                                <c:out value="${customer.username}"/>

                            </p>

                        </div>

                        <div class="col-lg-3 col-md-6">

                            <div class="detail-label">
                                Vai trò
                            </div>

                            <p class="detail-value">

                                <c:out value="${customer.role}"/>

                            </p>

                        </div>

                        <div class="col-lg-3 col-md-6">

                            <div class="detail-label">
                                Trạng thái
                            </div>

                            <p class="detail-value">

                                <c:out value="${customer.status}"/>

                            </p>

                        </div>

                        <div class="col-md-4">

                            <div class="detail-label">
                                Họ và tên
                            </div>

                            <p class="detail-value">

                                <c:choose>

                                    <c:when test="${not empty customer.fullName}">

                                        <c:out value="${customer.fullName}"/>

                                    </c:when>

                                    <c:otherwise>

                                        <span class="text-muted">
                                            Chưa cập nhật
                                        </span>

                                    </c:otherwise>

                                </c:choose>

                            </p>

                        </div>

                        <div class="col-md-4">

                            <div class="detail-label">
                                Email
                            </div>

                            <p class="detail-value">

                                <c:choose>

                                    <c:when test="${not empty customer.email}">

                                        <c:out value="${customer.email}"/>

                                    </c:when>

                                    <c:otherwise>

                                        <span class="text-muted">
                                            Chưa cập nhật
                                        </span>

                                    </c:otherwise>

                                </c:choose>

                            </p>

                        </div>

                        <div class="col-md-4">

                            <div class="detail-label">
                                Số điện thoại
                            </div>

                            <p class="detail-value">

                                <c:choose>

                                    <c:when test="${not empty customer.phone}">

                                        <c:out value="${customer.phone}"/>

                                    </c:when>

                                    <c:otherwise>

                                        <span class="text-muted">
                                            Chưa cập nhật
                                        </span>

                                    </c:otherwise>

                                </c:choose>

                            </p>

                        </div>

                        <div class="col-md-4">

                            <div class="detail-label">
                                Ngày tham gia
                            </div>

                            <p class="detail-value">

                                <c:choose>

                                    <c:when test="${not empty customer.createdAt}">

                                        <fmt:formatDate
                                            value="${customer.createdAt}"
                                            pattern="dd/MM/yyyy HH:mm:ss"/>

                                    </c:when>

                                    <c:otherwise>

                                        <span class="text-muted">
                                            Không xác định
                                        </span>

                                    </c:otherwise>

                                </c:choose>

                            </p>

                        </div>

                    </div>

                </div>

                <div class="card-footer bg-white">

                    <div class="d-flex flex-wrap
                                justify-content-end
                                gap-2
                                action-area">

                        <a
                            href="${pageContext.request.contextPath}/staff/customers/edit?id=${customer.userId}"
                            class="btn btn-warning">

                            Cập nhật thông tin

                        </a>

                        <c:if test="${customer.status eq 'ACTIVE'}">

                            <form
                                action="${pageContext.request.contextPath}/staff/customers/status"
                                method="post">

                                <input type="hidden"
                                       name="userId"
                                       value="${customer.userId}">

                                <input type="hidden"
                                       name="status"
                                       value="LOCKED">

                                <input type="hidden"
                                       name="returnPage"
                                       value="detail">

                                <button
                                    type="submit"
                                    class="btn btn-danger"
                                    onclick="return confirm('Bạn có chắc muốn khóa tài khoản này?');">

                                    Khóa tài khoản

                                </button>

                            </form>

                        </c:if>

                        <c:if test="${customer.status eq 'LOCKED'
                                    or customer.status eq 'INACTIVE'}">

                            <form
                                action="${pageContext.request.contextPath}/staff/customers/status"
                                method="post">

                                <input type="hidden"
                                       name="userId"
                                       value="${customer.userId}">

                                <input type="hidden"
                                       name="status"
                                       value="ACTIVE">

                                <input type="hidden"
                                       name="returnPage"
                                       value="detail">

                                <button
                                    type="submit"
                                    class="btn btn-success"
                                    onclick="return confirm('Bạn có chắc muốn mở khóa tài khoản này?');">

                                    Mở khóa tài khoản

                                </button>

                            </form>

                        </c:if>

                    </div>

                </div>

            </div>

            <%-- Thống kê trạng thái vé --%>

            <div class="row g-3 mb-4">

                <div class="col-xl-2 col-md-4 col-sm-6">

                    <div class="card shadow-sm summary-card h-100">

                        <div class="card-body">

                            <div class="summary-label">
                                Tổng lượt đặt
                            </div>

                            <p class="summary-number text-primary">
                                ${empty totalTickets ? 0 : totalTickets}
                            </p>

                        </div>

                    </div>

                </div>

                <div class="col-xl-2 col-md-4 col-sm-6">

                    <div class="card shadow-sm summary-card h-100">

                        <div class="card-body">

                            <div class="summary-label">
                                BOOKED
                            </div>

                            <p class="summary-number text-warning">
                                ${empty bookedTickets ? 0 : bookedTickets}
                            </p>

                        </div>

                    </div>

                </div>

                <div class="col-xl-2 col-md-4 col-sm-6">

                    <div class="card shadow-sm summary-card h-100">

                        <div class="card-body">

                            <div class="summary-label">
                                PAID
                            </div>

                            <p class="summary-number text-info">
                                ${empty paidTickets ? 0 : paidTickets}
                            </p>

                        </div>

                    </div>

                </div>

                <div class="col-xl-2 col-md-4 col-sm-6">

                    <div class="card shadow-sm summary-card h-100">

                        <div class="card-body">

                            <div class="summary-label">
                                CONFIRMED
                            </div>

                            <p class="summary-number text-success">
                                ${empty confirmedTickets ? 0 : confirmedTickets}
                            </p>

                        </div>

                    </div>

                </div>

                <div class="col-xl-2 col-md-4 col-sm-6">

                    <div class="card shadow-sm summary-card h-100">

                        <div class="card-body">

                            <div class="summary-label">
                                CHECKED_IN
                            </div>

                            <p class="summary-number text-primary">
                                ${empty checkedInTickets ? 0 : checkedInTickets}
                            </p>

                        </div>

                    </div>

                </div>

                <div class="col-xl-2 col-md-4 col-sm-6">

                    <div class="card shadow-sm summary-card h-100">

                        <div class="card-body">

                            <div class="summary-label">
                                CANCELLED
                            </div>

                            <p class="summary-number text-danger">
                                ${empty cancelledTickets ? 0 : cancelledTickets}
                            </p>

                        </div>

                    </div>

                </div>

            </div>

            <%-- Thống kê khách hàng --%>

            <div class="card shadow-sm detail-card mb-4">

                <div class="card-header bg-white py-3">

                    <h5 class="mb-0">
                        Thống kê khách hàng
                    </h5>

                </div>

                <div class="card-body">

                    <div class="row g-4">

                        <div class="col-lg-3 col-md-6">

                            <div class="detail-label">
                                Đã là khách hàng
                            </div>

                            <p class="detail-value">

                                ${empty memberDays ? 0 : memberDays}
                                ngày

                            </p>

                        </div>

                        <div class="col-lg-3 col-md-6">

                            <div class="detail-label">
                                Lượt đặt đầu tiên
                            </div>

                            <p class="detail-value">

                                <c:choose>

                                    <c:when test="${not empty firstBookingDate}">

                                        <fmt:formatDate
                                            value="${firstBookingDate}"
                                            pattern="dd/MM/yyyy HH:mm"/>

                                    </c:when>

                                    <c:otherwise>

                                        <span class="text-muted">
                                            Chưa có
                                        </span>

                                    </c:otherwise>

                                </c:choose>

                            </p>

                        </div>

                        <div class="col-lg-3 col-md-6">

                            <div class="detail-label">
                                Lượt đặt gần nhất
                            </div>

                            <p class="detail-value">

                                <c:choose>

                                    <c:when test="${not empty latestBookingDate}">

                                        <fmt:formatDate
                                            value="${latestBookingDate}"
                                            pattern="dd/MM/yyyy HH:mm"/>

                                    </c:when>

                                    <c:otherwise>

                                        <span class="text-muted">
                                            Chưa có
                                        </span>

                                    </c:otherwise>

                                </c:choose>

                            </p>

                        </div>

                        <div class="col-lg-3 col-md-6">

                            <div class="detail-label">
                                Trung bình mỗi lượt đặt
                            </div>

                            <p class="detail-value">

                                <fmt:formatNumber
                                    value="${empty averageBookingAmount
                                        ? 0
                                        : averageBookingAmount}"
                                    type="number"
                                    groupingUsed="true"/>

                                VNĐ

                            </p>

                        </div>

                        <div class="col-md-6">

                            <div class="detail-label">
                                Tổng giá trị đặt vé
                            </div>

                            <p class="summary-number">

                                <fmt:formatNumber
                                    value="${empty totalBookingAmount
                                        ? 0
                                        : totalBookingAmount}"
                                    type="number"
                                    groupingUsed="true"/>

                                VNĐ

                            </p>

                        </div>

                        <div class="col-md-6">

                            <div class="detail-label">
                                Tổng tiền đã thanh toán
                            </div>

                            <p class="summary-number money-value">

                                <fmt:formatNumber
                                    value="${empty totalPaidAmount
                                        ? 0
                                        : totalPaidAmount}"
                                    type="number"
                                    groupingUsed="true"/>

                                VNĐ

                            </p>

                        </div>

                    </div>

                </div>

            </div>

            <%-- Lịch sử đặt vé --%>

            <div class="card shadow-sm detail-card">

                <div class="card-header bg-white py-3">

                    <div class="d-flex flex-wrap
                                justify-content-between
                                align-items-center
                                gap-2">

                        <h5 class="mb-0">
                            Lịch sử đặt vé
                        </h5>

                        <span class="badge bg-primary">

                            ${empty tickets ? 0 : tickets.size()}
                            vé

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
                                <th>Mã đặt vé</th>
                                <th>Phim</th>
                                <th>Phòng</th>
                                <th>Suất chiếu</th>
                                <th class="text-center">Số người</th>
                                <th class="text-end">Tổng tiền</th>
                                <th>Trạng thái</th>
                                <th>Ngày đặt</th>
                                <th class="text-center">Thao tác</th>

                            </tr>

                            </thead>

                            <tbody>

                            <c:choose>

                                <c:when test="${empty tickets}">

                                    <tr>

                                        <td colspan="10"
                                            class="text-center
                                                   text-muted
                                                   empty-data">

                                            Khách hàng chưa có lịch sử đặt vé.

                                        </td>

                                    </tr>

                                </c:when>

                                <c:otherwise>

                                    <c:forEach
                                        items="${tickets}"
                                        var="ticket">

                                        <tr>

                                            <td>
                                                ${ticket.ticketId}
                                            </td>

                                            <td>

                                                <span class="booking-code">

                                                    <c:out value="${ticket.bookingCode}"/>

                                                </span>

                                            </td>

                                            <td>

                                                <c:choose>

                                                    <c:when test="${not empty ticket.movieTitle}">

                                                        <c:out value="${ticket.movieTitle}"/>

                                                    </c:when>

                                                    <c:otherwise>

                                                        <span class="text-muted">
                                                            Chưa xác định
                                                        </span>

                                                    </c:otherwise>

                                                </c:choose>

                                            </td>

                                            <td>

                                                <c:choose>

                                                    <c:when test="${not empty ticket.roomName}">

                                                        <c:out value="${ticket.roomName}"/>

                                                    </c:when>

                                                    <c:otherwise>

                                                        <span class="text-muted">
                                                            Chưa xác định
                                                        </span>

                                                    </c:otherwise>

                                                </c:choose>

                                            </td>

                                            <td>

                                                <c:choose>

                                                    <c:when test="${not empty ticket.startTime}">

                                                        <fmt:formatDate
                                                            value="${ticket.startTime}"
                                                            pattern="dd/MM/yyyy HH:mm"/>

                                                    </c:when>

                                                    <c:otherwise>

                                                        <span class="text-muted">
                                                            Chưa xác định
                                                        </span>

                                                    </c:otherwise>

                                                </c:choose>

                                            </td>

                                            <td class="text-center">

                                                ${ticket.peopleCount}

                                            </td>

                                            <td class="text-end">

                                                <fmt:formatNumber
                                                    value="${empty ticket.totalPrice
                                                        ? 0
                                                        : ticket.totalPrice}"
                                                    type="number"
                                                    groupingUsed="true"/>

                                                VNĐ

                                            </td>

                                            <td>

                                                <c:choose>

                                                    <c:when test="${ticket.status eq 'BOOKED'}">

                                                        <span class="badge bg-warning text-dark">
                                                            BOOKED
                                                        </span>

                                                    </c:when>

                                                    <c:when test="${ticket.status eq 'PAID'}">

                                                        <span class="badge bg-info text-dark">
                                                            PAID
                                                        </span>

                                                    </c:when>

                                                    <c:when test="${ticket.status eq 'CONFIRMED'}">

                                                        <span class="badge bg-success">
                                                            CONFIRMED
                                                        </span>

                                                    </c:when>

                                                    <c:when test="${ticket.status eq 'CHECKED_IN'}">

                                                        <span class="badge bg-primary">
                                                            CHECKED_IN
                                                        </span>

                                                    </c:when>

                                                    <c:when test="${ticket.status eq 'CANCELLED'}">

                                                        <span class="badge bg-danger">
                                                            CANCELLED
                                                        </span>

                                                    </c:when>

                                                    <c:otherwise>

                                                        <span class="badge bg-secondary">

                                                            <c:out value="${ticket.status}"/>

                                                        </span>

                                                    </c:otherwise>

                                                </c:choose>

                                            </td>

                                            <td>

                                                <c:choose>

                                                    <c:when test="${not empty ticket.createdAt}">

                                                        <fmt:formatDate
                                                            value="${ticket.createdAt}"
                                                            pattern="dd/MM/yyyy HH:mm"/>

                                                    </c:when>

                                                    <c:otherwise>

                                                        <span class="text-muted">
                                                            Không xác định
                                                        </span>

                                                    </c:otherwise>

                                                </c:choose>

                                            </td>

                                            <td class="text-center">

                                                <a
                                                    href="${pageContext.request.contextPath}/staff/booking-detail?ticketId=${ticket.ticketId}"
                                                    class="btn btn-sm btn-outline-primary">

                                                    Xem vé

                                                </a>

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

        </c:otherwise>

    </c:choose>

</div>

<script
    src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js">
</script>

</body>

</html>