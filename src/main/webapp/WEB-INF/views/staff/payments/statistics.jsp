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

    <title>Thống kê thanh toán</title>

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

        .statistic-card,
        .content-card {
            border: none;
            border-radius: 12px;
            overflow: hidden;
        }

        .statistic-label {
            color: #6c757d;
            margin-bottom: 6px;
        }

        .statistic-number {
            font-size: 26px;
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

        .revenue-value {
            color: #198754;
            font-weight: 700;
            white-space: nowrap;
        }

        .empty-data {
            padding: 40px 15px !important;
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
                Thống kê thanh toán
            </h2>

            <p class="text-muted mb-0">
                Theo dõi số lượng giao dịch và doanh thu
            </p>

        </div>

        <div class="d-flex flex-wrap gap-2">

            <a
                href="${pageContext.request.contextPath}/staff/payments/today"
                class="btn btn-outline-primary">

                Thanh toán hôm nay

            </a>

            <a
                href="${pageContext.request.contextPath}/staff/payments"
                class="btn btn-outline-secondary">

                Danh sách thanh toán

            </a>

        </div>

    </div>

    <c:if test="${not empty errorMessage}">

        <div class="alert alert-danger">

            <c:out value="${errorMessage}"/>

        </div>

    </c:if>

    <%-- Chọn khoảng ngày --%>

    <div class="card shadow-sm content-card mb-4">

        <div class="card-header bg-white py-3">

            <h5 class="mb-0">
                Chọn khoảng thời gian
            </h5>

        </div>

        <div class="card-body">

            <form
                action="${pageContext.request.contextPath}/staff/payments/statistics"
                method="get">

                <div class="row g-3 align-items-end">

                    <div class="col-md-4">

                        <label
                            for="fromDate"
                            class="form-label">

                            Từ ngày

                        </label>

                        <input
                            type="date"
                            id="fromDate"
                            name="fromDate"
                            class="form-control"
                            value="<c:out value='${fromDate}'/>"
                            required>

                    </div>

                    <div class="col-md-4">

                        <label
                            for="toDate"
                            class="form-label">

                            Đến ngày

                        </label>

                        <input
                            type="date"
                            id="toDate"
                            name="toDate"
                            class="form-control"
                            value="<c:out value='${toDate}'/>"
                            required>

                    </div>

                    <div class="col-md-4">

                        <div class="d-flex gap-2">

                            <button
                                type="submit"
                                class="btn btn-primary flex-grow-1">

                                Thống kê

                            </button>

                            <a
                                href="${pageContext.request.contextPath}/staff/payments/statistics"
                                class="btn btn-outline-secondary">

                                30 ngày

                            </a>

                        </div>

                    </div>

                </div>

            </form>

        </div>

    </div>

    <%-- Các thẻ thống kê --%>

    <div class="row g-3 mb-4">

        <div class="col-xl-3 col-md-6">

            <div class="card shadow-sm statistic-card h-100">

                <div class="card-body">

                    <div class="statistic-label">
                        Tổng giao dịch
                    </div>

                    <p class="statistic-number text-primary">

                        ${empty totalPayments
                            ? 0
                            : totalPayments}

                    </p>

                </div>

            </div>

        </div>

        <div class="col-xl-3 col-md-6">

            <div class="card shadow-sm statistic-card h-100">

                <div class="card-body">

                    <div class="statistic-label">
                        Tổng doanh thu
                    </div>

                    <p class="statistic-number text-success">

                        <fmt:formatNumber
                            value="${empty totalRevenue
                                ? 0
                                : totalRevenue}"
                            type="number"
                            groupingUsed="true"/>

                        VNĐ

                    </p>

                </div>

            </div>

        </div>

        <div class="col-xl-3 col-md-6">

            <div class="card shadow-sm statistic-card h-100">

                <div class="card-body">

                    <div class="statistic-label">
                        PAID
                    </div>

                    <p class="statistic-number text-success">

                        ${empty paidCount ? 0 : paidCount}

                    </p>

                </div>

            </div>

        </div>

        <div class="col-xl-3 col-md-6">

            <div class="card shadow-sm statistic-card h-100">

                <div class="card-body">

                    <div class="statistic-label">
                        PENDING
                    </div>

                    <p class="statistic-number text-warning">

                        ${empty pendingCount
                            ? 0
                            : pendingCount}

                    </p>

                </div>

            </div>

        </div>

        <div class="col-xl-3 col-md-6">

            <div class="card shadow-sm statistic-card h-100">

                <div class="card-body">

                    <div class="statistic-label">
                        FAILED
                    </div>

                    <p class="statistic-number text-danger">

                        ${empty failedCount
                            ? 0
                            : failedCount}

                    </p>

                </div>

            </div>

        </div>

        <div class="col-xl-3 col-md-6">

            <div class="card shadow-sm statistic-card h-100">

                <div class="card-body">

                    <div class="statistic-label">
                        CANCELLED
                    </div>

                    <p class="statistic-number text-secondary">

                        ${empty cancelledCount
                            ? 0
                            : cancelledCount}

                    </p>

                </div>

            </div>

        </div>

    </div>

    <%-- Doanh thu theo ngày --%>

    <div class="card shadow-sm content-card mb-4">

        <div class="card-header bg-white py-3">

            <h5 class="mb-0">
                Doanh thu theo ngày
            </h5>

        </div>

        <div class="card-body p-0">

            <div class="table-responsive">

                <table class="table table-bordered
                              table-hover mb-0">

                    <thead class="table-dark">

                    <tr>

                        <th>Ngày</th>
                        <th class="text-center">
                            Số giao dịch PAID
                        </th>
                        <th class="text-end">
                            Doanh thu
                        </th>

                    </tr>

                    </thead>

                    <tbody>

                    <c:choose>

                        <c:when test="${empty dailyStatistics}">

                            <tr>

                                <td
                                    colspan="3"
                                    class="text-center text-muted empty-data">

                                    Không có doanh thu trong khoảng ngày này.

                                </td>

                            </tr>

                        </c:when>

                        <c:otherwise>

                            <c:forEach
                                items="${dailyStatistics}"
                                var="statistic">

                                <tr>

                                    <td>

                                        <fmt:formatDate
                                            value="${statistic.statisticDate}"
                                            pattern="dd/MM/yyyy"/>

                                    </td>

                                    <td class="text-center">

                                        ${statistic.transactionCount}

                                    </td>

                                    <td class="text-end revenue-value">

                                        <fmt:formatNumber
                                            value="${statistic.totalRevenue}"
                                            type="number"
                                            groupingUsed="true"/>

                                        VNĐ

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

    <%-- Thống kê theo phương thức --%>

    <div class="card shadow-sm content-card">

        <div class="card-header bg-white py-3">

            <h5 class="mb-0">
                Thống kê theo phương thức thanh toán
            </h5>

        </div>

        <div class="card-body p-0">

            <div class="table-responsive">

                <table class="table table-bordered
                              table-hover mb-0">

                    <thead class="table-dark">

                    <tr>

                        <th>Phương thức</th>
                        <th class="text-center">
                            Số giao dịch
                        </th>
                        <th class="text-end">
                            Doanh thu
                        </th>

                    </tr>

                    </thead>

                    <tbody>

                    <c:choose>

                        <c:when test="${empty methodStatistics}">

                            <tr>

                                <td
                                    colspan="3"
                                    class="text-center text-muted empty-data">

                                    Không có dữ liệu phương thức thanh toán.

                                </td>

                            </tr>

                        </c:when>

                        <c:otherwise>

                            <c:forEach
                                items="${methodStatistics}"
                                var="statistic">

                                <tr>

                                    <td>

                                        <span class="badge bg-secondary">

                                            <c:out
                                                value="${statistic.paymentMethod}"/>

                                        </span>

                                    </td>

                                    <td class="text-center">

                                        ${statistic.transactionCount}

                                    </td>

                                    <td class="text-end revenue-value">

                                        <fmt:formatNumber
                                            value="${statistic.totalRevenue}"
                                            type="number"
                                            groupingUsed="true"/>

                                        VNĐ

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