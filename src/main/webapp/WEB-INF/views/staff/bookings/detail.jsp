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

        .summary-number {
            font-size: 28px;
            font-weight: 700;
            margin-bottom: 0;
        }

        .booking-code {
            color: #0d6efd;
            font-weight: 700;
        }

        .table th {
            white-space: nowrap;
        }

        .table td {
            vertical-align: middle;
        }

        .action-area form {
            margin: 0;
        }

        .empty-data {
            padding: 45px 15px !important;
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
                Chi tiết khách hàng
            </h2>

            <p class="text-muted mb-0">
                Thông tin tài khoản và lịch sử đặt vé
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

    <c:if test="${param.error eq 'invalid_status'}">

        <div class="alert alert-warning alert-dismissible fade show">

            Trạng thái tài khoản không hợp lệ.

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

                    <div class="d-flex
                                justify-content-between
                                align-items-center">

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

                            <c:when test="${customer.status eq 'BLOCKED'}">

                                <span class="badge bg-danger">
                                    BLOCKED
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
                                Ngày tạo tài khoản
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
                                gap-2 action-area">

                        <a
                            href="${pageContext.request.contextPath}/staff/customers/edit?id=${customer.userId}"
                            class="btn btn-warning">

                            Cập nhật thông tin

                        </a>

                        <c:if test="${customer.status eq 'ACTIVE'}">

                            <form
                                action="${pageContext.request.contextPath}/staff/customers/status"
                                method="post">

                                <input
                                    type="hidden"
                                    name="userId"
                                    value="${customer.userId}">

                                <input
                                    type="hidden"
                                    name="status"
                                    value="BLOCKED">

                                <input
                                    type="hidden"
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

                        <c:if test="${customer.status eq 'BLOCKED'
                                    or customer.status eq 'INACTIVE'}">

                            <form
                                action="${pageContext.request.contextPath}/staff/customers/status"
                                method="post">

                                <input
                                    type="hidden"
                                    name="userId"
                                    value="${customer.userId}">

                                <input
                                    type="hidden"
                                    name="status"
                                    value="ACTIVE">

                                <input
                                    type="hidden"
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

            <%-- Thống kê --%>

            <div class="row g-3 mb-4">

                <div class="col-xl-3 col-md-6">

                    <div class="card shadow-sm summary-card h-100">

                        <div class="card-body">

                            <div class="text-muted mb-2">
                                Tổng lượt đặt vé
                            </div>

                            <p class="summary-number text-primary">

                                ${empty totalTickets ? 0 : totalTickets}

                            </p>

                        </div>

                    </div>

                </div>

                <div class="col-xl-3 col-md-6">

                    <div class="card shadow-sm summary-card h-100">

                        <div class="card-body">

                            <div class="text-muted mb-2">
                                Đang đặt
                            </div>

                            <p class="summary-number text-warning">

                                ${empty bookedTickets ? 0 : bookedTickets}

                            </p>

                        </div>

                    </div>

                </div>

                <div class="col-xl-3 col-md-6">

                    <div class="card shadow-sm summary-card h-100">

                        <div class="card-body">

                            <div class="text-muted mb-2">
                                Đã xác nhận
                            </div>

                            <p class="summary-number text-success">

                                ${empty confirmedTickets
                                    ? 0
                                    : confirmedTickets}

                            </p>

                        </div>

                    </div>

                </div>

                <div class="col-xl-3 col-md-6">

                    <div class="card shadow-sm summary-card h-100">

                        <div class="card-body">

                            <div class="text-muted mb-2">
                                Đã hủy
                            </div>

                            <p class="summary-number text-danger">

                                ${empty cancelledTickets
                                    ? 0
                                    : cancelledTickets}

                            </p>

                        </div>

                    </div>

                </div>

            </div>

            <%-- Lịch sử đặt vé --%>

            <div class="card shadow-sm detail-card">

                <div class="card-header bg-white py-3">

                    <div class="d-flex
                                justify-content-between
                                align-items-center">

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

                                    <c:forEach items="${tickets}"
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

                                                <c:out value="${ticket.movieTitle}"/>

                                            </td>

                                            <td>

                                                <c:out value="${ticket.roomName}"/>

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
                                                    value="${ticket.totalPrice}"
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