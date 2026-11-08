<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
                    <i class="bi bi-cash-stack"></i>
                    Doanh thu hôm nay
                </h3>
            </div>

            <div class="card-body">

                <!-- Revenue Summary -->
                <div class="row mb-4">

                    <div class="col-md-4">

                        <div class="card border-success shadow-sm h-100">

                            <div class="card-header bg-success text-white">

                                <i class="bi bi-calendar-day"></i>
                                Hôm nay

                            </div>

                            <div class="card-body">

                                <h3 class="text-success">

                                    <fmt:formatNumber
                                            value="${todayRevenue}"
                                            type="number"
                                            groupingUsed="true"/>

                                    VNĐ

                                </h3>

                            </div>

                        </div>

                    </div>

                    <div class="col-md-4">

                        <div class="card border-primary shadow-sm h-100">

                            <div class="card-header bg-primary text-white">

                                <i class="bi bi-calendar-month"></i>
                                Tháng này

                            </div>

                            <div class="card-body">

                                <h3 class="text-primary">

                                    <fmt:formatNumber
                                            value="${monthRevenue}"
                                            type="number"
                                            groupingUsed="true"/>

                                    VNĐ

                                </h3>

                            </div>

                        </div>

                    </div>

                    <div class="col-md-4">

                        <div class="card border-dark shadow-sm h-100">

                            <div class="card-header bg-dark text-white">

                                <i class="bi bi-graph-up-arrow"></i>
                                Năm nay

                            </div>

                            <div class="card-body">

                                <h3>

                                    <fmt:formatNumber
                                            value="${yearRevenue}"
                                            type="number"
                                            groupingUsed="true"/>

                                    VNĐ

                                </h3>

                            </div>

                        </div>

                    </div>

                </div>

                <!-- Payment Detail -->

                <div class="card shadow-sm">

                    <div class="card-header bg-secondary text-white">

                        <i class="bi bi-receipt"></i>

                        Chi tiết thanh toán hôm nay

                    </div>

                    <div class="card-body">

                        <div class="table-responsive">

                            <table class="table table-bordered table-hover align-middle">

                                <thead class="table-dark">

                                <tr>

                                    <th>ID</th>
                                    <th>Mã GD</th>
                                    <th>Mã Đặt Vé</th>
                                    <th>Khách hàng</th>
                                    <th>Phim</th>
                                    <th>Thanh toán</th>
                                    <th>Số tiền</th>
                                    <th>Trạng thái</th>
                                    <th>Thời gian</th>

                                </tr>

                                </thead>

                                <tbody>

                                <c:choose>

                                    <c:when test="${empty payments}">

                                        <tr>

                                            <td colspan="9"
                                                class="text-center text-muted">

                                                Không có giao dịch hôm nay.

                                            </td>

                                        </tr>

                                    </c:when>

                                    <c:otherwise>

                                        <c:forEach items="${payments}" var="payment">

                                            <tr>

                                                <td>${payment.paymentId}</td>

                                                <td>${payment.transactionCode}</td>

                                                <td>${payment.bookingCode}</td>

                                                <td>${payment.customerName}</td>

                                                <td>${payment.movieTitle}</td>

                                                <td>${payment.paymentMethod}</td>

                                                <td>

                                                    <fmt:formatNumber
                                                            value="${payment.amount}"
                                                            type="number"
                                                            groupingUsed="true"/>

                                                    VNĐ

                                                </td>

                                                <td>

                                                    <c:choose>

                                                        <c:when test="${payment.status=='PAID'}">

                                                            <span class="badge bg-success">

                                                                PAID

                                                            </span>

                                                        </c:when>

                                                        <c:when test="${payment.status=='PENDING'}">

                                                            <span class="badge bg-warning text-dark">

                                                                PENDING

                                                            </span>

                                                        </c:when>

                                                        <c:otherwise>

                                                            <span class="badge bg-danger">

                                                                CANCELLED

                                                            </span>

                                                        </c:otherwise>

                                                    </c:choose>

                                                </td>

                                                <td>${payment.paidAt}</td>

                                            </tr>

                                        </c:forEach>

                                    </c:otherwise>

                                </c:choose>

                                </tbody>

                            </table>

                        </div>

                    </div>

                </div>

                <div class="mt-4">

                    <a href="${pageContext.request.contextPath}/staff/dashboard"
                       class="btn btn-primary">


                    </a>

                </div>

            </div>

        </div>

    </div>

</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>

</body>

</html>