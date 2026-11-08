<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<%@ taglib prefix="c"
           uri="jakarta.tags.core"%>

<%@ taglib prefix="fmt"
           uri="jakarta.tags.fmt"%>

<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<body>

<jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

<link
    rel="stylesheet"
    href="${pageContext.request.contextPath}/assets/css/booking-success.css">

<main class="booking-success-page">

    <div class="container">

        <c:choose>

            <c:when test="${empty ticket}">

                <div class="booking-success-card">

                    <h1>
                        Không tìm thấy vé
                    </h1>

                    <p>
                        Không có thông tin đặt vé gần đây.
                    </p>

                    <a
                        href="${pageContext.request.contextPath}/home"
                        class="btn-success-primary">

                        Về trang chủ

                    </a>

                </div>

            </c:when>

            <c:otherwise>

                <section class="booking-success-card">

                    <div class="success-icon">
                        ✓
                    </div>

                    <p class="success-label">
                        Đặt vé thành công
                    </p>

                    <h1>
                        Vé của bạn đã được tạo
                    </h1>

                    <p class="success-description">

                        Hãy kiểm tra thông tin vé
                        trước khi tiếp tục thanh toán.

                    </p>

                    <div class="booking-code-box">

                        <span>
                            Mã đặt vé
                        </span>

                        <strong>

                            <c:out
                                value="${ticket.bookingCode}"/>

                        </strong>

                    </div>

                    <div class="success-information">

                        <div class="success-row">

                            <span>
                                Phim
                            </span>

                            <strong>

                                <c:choose>

                                    <c:when test="${not empty showtime.movieTitle}">

                                        <c:out
                                            value="${showtime.movieTitle}"/>

                                    </c:when>

                                    <c:otherwise>

                                        Movie #${ticket.showtimeId}

                                    </c:otherwise>

                                </c:choose>

                            </strong>

                        </div>

                        <div class="success-row">

                            <span>
                                Phòng
                            </span>

                            <strong>

                                <c:choose>

                                    <c:when test="${not empty showtime.roomName}">

                                        <c:out
                                            value="${showtime.roomName}"/>

                                    </c:when>

                                    <c:otherwise>

                                        Phòng #${ticket.roomId}

                                    </c:otherwise>

                                </c:choose>

                            </strong>

                        </div>

                        <div class="success-row">

                            <span>
                                Số người
                            </span>

                            <strong>
                                ${ticket.peopleCount}
                            </strong>

                        </div>

                        <div class="success-row">

                            <span>
                                Trạng thái
                            </span>

                            <strong class="status-booked">

                                <c:out
                                    value="${ticket.status}"/>

                            </strong>

                        </div>

                    </div>

                    <div class="success-payment-summary">

                        <div>

                            <span>
                                Tiền vé
                            </span>

                            <strong>

                                <fmt:formatNumber
                                    value="${ticketTotal}"
                                    type="number"
                                    groupingUsed="true"/>

                                VNĐ

                            </strong>

                        </div>

                        <div>

                            <span>
                                Tiền dịch vụ
                            </span>

                            <strong>

                                <fmt:formatNumber
                                    value="${serviceTotal}"
                                    type="number"
                                    groupingUsed="true"/>

                                VNĐ

                            </strong>

                        </div>

                        <div class="success-grand-total">

                            <span>
                                Tổng cộng
                            </span>

                            <strong>

                                <fmt:formatNumber
                                    value="${ticket.totalPrice}"
                                    type="number"
                                    groupingUsed="true"/>

                                VNĐ

                            </strong>

                        </div>

                    </div>

                    <div class="success-actions">

                        <a
                            href="${pageContext.request.contextPath}/home"
                            class="btn-success-outline">

                            Về trang chủ

                        </a>

                        <a
                            href="${pageContext.request.contextPath}/customer/tickets"
                            class="btn-success-primary">

                            Vé của tôi

                        </a>

                    </div>

                </section>

            </c:otherwise>

        </c:choose>

    </div>

</main>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>

<script
    src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js">
</script>

</body>
</html>