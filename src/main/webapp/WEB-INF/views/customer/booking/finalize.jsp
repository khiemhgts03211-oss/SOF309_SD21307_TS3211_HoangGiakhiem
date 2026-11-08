<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<%@ taglib prefix="c"
           uri="jakarta.tags.core"%>

<%@ taglib prefix="fmt"
           uri="jakarta.tags.fmt"%>

<jsp:include
    page="/WEB-INF/views/common/header.jsp"/>

<body>

<jsp:include
    page="/WEB-INF/views/common/navbar.jsp"/>

<link
    rel="stylesheet"
    href="${pageContext.request.contextPath}/assets/css/booking-confirm.css">

<main class="booking-confirm-page">

    <div class="container">

        <header class="confirm-page-heading">

            <p class="confirm-page-label">
                Bước 4
            </p>

            <h1>
                Kiểm tra đặt vé
            </h1>

            <span>
                Dữ liệu đã được hệ thống kiểm tra lại
                trước khi tạo vé.
            </span>

        </header>

        <c:choose>

            <%-- KHÔNG CÓ DỮ LIỆU --%>

            <c:when test="${empty showtime}">

                <section class="confirm-empty">

                    <div class="confirm-empty-icon">
                        🎟️
                    </div>

                    <h2>
                        Không tìm thấy thông tin đặt vé
                    </h2>

                    <p>
                        Dữ liệu đặt vé không hợp lệ
                        hoặc phiên làm việc đã hết hạn.
                    </p>

                    <a
                        href="${pageContext.request.contextPath}/home"
                        class="btn-confirm-primary">

                        Quay lại trang chủ

                    </a>

                </section>

            </c:when>

            <%-- CÓ DỮ LIỆU --%>

            <c:otherwise>

                <div class="confirm-layout">

                    <%-- ============================
                         THÔNG TIN
                         ============================ --%>

                    <section class="confirm-main-card">

                        <div class="confirm-movie-heading">

                            <div>

                                <span class="confirm-small-label">
                                    Phim
                                </span>

                                <h2>

                                    <c:choose>

                                        <c:when test="${not empty showtime.movieTitle}">

                                            <c:out
                                                value="${showtime.movieTitle}"/>

                                        </c:when>

                                        <c:otherwise>

                                            Movie #${showtime.movieId}

                                        </c:otherwise>

                                    </c:choose>

                                </h2>

                            </div>

                            <span class="confirm-status">

                                Đã kiểm tra

                            </span>

                        </div>

                        <%-- SUẤT CHIẾU --%>

                        <section class="confirm-section">

                            <h3>
                                Suất chiếu
                            </h3>

                            <div class="confirm-information-grid">

                                <div class="confirm-information-item">

                                    <span>
                                        Ngày chiếu
                                    </span>

                                    <strong>

                                        <fmt:formatDate
                                            value="${showtime.showDate}"
                                            pattern="dd/MM/yyyy"/>

                                    </strong>

                                </div>

                                <div class="confirm-information-item">

                                    <span>
                                        Giờ chiếu
                                    </span>

                                    <strong>

                                        <fmt:formatDate
                                            value="${showtime.startTime}"
                                            pattern="HH:mm"/>

                                        -

                                        <fmt:formatDate
                                            value="${showtime.endTime}"
                                            pattern="HH:mm"/>

                                    </strong>

                                </div>

                                <div class="confirm-information-item">

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

                                                Phòng #${showtime.roomId}

                                            </c:otherwise>

                                        </c:choose>

                                    </strong>

                                </div>

                                <div class="confirm-information-item">

                                    <span>
                                        Số người
                                    </span>

                                    <strong>
                                        ${peopleCount} người
                                    </strong>

                                </div>

                            </div>

                        </section>

                        <%-- TIỀN VÉ --%>

                        <section class="confirm-section">

                            <h3>
                                Tiền vé
                            </h3>

                            <div class="confirm-price-row">

                                <span>

                                    ${peopleCount} vé ×

                                    <fmt:formatNumber
                                        value="${ticketPrice}"
                                        type="number"
                                        groupingUsed="true"/>

                                    VNĐ

                                </span>

                                <strong>

                                    <fmt:formatNumber
                                        value="${ticketTotal}"
                                        type="number"
                                        groupingUsed="true"/>

                                    VNĐ

                                </strong>

                            </div>

                        </section>

                        <%-- DỊCH VỤ --%>

                        <section class="confirm-section">

                            <h3>
                                Dịch vụ
                            </h3>

                            <c:choose>

                                <c:when test="${empty selectedServices}">

                                    <div class="confirm-no-service">

                                        Không chọn thêm dịch vụ.

                                    </div>

                                </c:when>

                                <c:otherwise>

                                    <div class="confirm-service-list">

                                        <c:forEach
                                            items="${selectedServices}"
                                            var="item">

                                            <div class="confirm-service-item">

                                                <div>

                                                    <strong>

                                                        <c:out
                                                            value="${item.serviceName}"/>

                                                    </strong>

                                                    <c:if test="${not empty item.categoryName}">

                                                        <small>

                                                            <c:out
                                                                value="${item.categoryName}"/>

                                                        </small>

                                                    </c:if>

                                                </div>

                                                <div>

                                                    ${item.quantity}
                                                    ×

                                                    <fmt:formatNumber
                                                        value="${item.unitPrice}"
                                                        type="number"
                                                        groupingUsed="true"/>

                                                    VNĐ

                                                </div>

                                                <strong>

                                                    <fmt:formatNumber
                                                        value="${item.subtotal}"
                                                        type="number"
                                                        groupingUsed="true"/>

                                                    VNĐ

                                                </strong>

                                            </div>

                                        </c:forEach>

                                    </div>

                                </c:otherwise>

                            </c:choose>

                        </section>

                    </section>

                    <%-- ============================
                         SUMMARY
                         ============================ --%>

                    <aside class="confirm-summary-card">

                        <div class="confirm-summary-heading">

                            <p>
                                Dữ liệu hợp lệ
                            </p>

                            <h2>
                                Tổng đơn hàng
                            </h2>

                        </div>

                        <div class="confirm-summary-row">

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

                        <div class="confirm-summary-row">

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

                        <div class="confirm-summary-divider">
                        </div>

                        <div class="confirm-summary-total">

                            <span>
                                Tổng thanh toán
                            </span>

                            <strong>

                                <fmt:formatNumber
                                    value="${grandTotal}"
                                    type="number"
                                    groupingUsed="true"/>

                                VNĐ

                            </strong>

                        </div>

                        <div class="confirm-customer">

                            <span>
                                Khách hàng
                            </span>

                            <strong>

                                <c:choose>

                                    <c:when test="${not empty customer.fullName}">

                                        <c:out
                                            value="${customer.fullName}"/>

                                    </c:when>

                                    <c:when test="${not empty customer.username}">

                                        <c:out
                                            value="${customer.username}"/>

                                    </c:when>

                                    <c:otherwise>

                                        Khách hàng

                                    </c:otherwise>

                                </c:choose>

                            </strong>

                        </div>

                        <div class="alert alert-success mt-3">

                            Dữ liệu đặt vé đã được kiểm tra
                            và hợp lệ.

                        </div>

                        <%--
                            Sang 24.8:
                            endpoint này sẽ tạo Ticket thật.
                        --%>

                        <form
                            action="${pageContext.request.contextPath}/booking/create-ticket"
                            method="post"
                            class="confirm-final-form">

                            <button
                                type="submit"
                                class="btn-confirm-primary">

                                Tạo vé

                            </button>

                        </form>

                        <a
                            href="${pageContext.request.contextPath}/booking/services"
                            class="btn-confirm-outline">

                            Quay lại chỉnh dịch vụ

                        </a>

                    </aside>

                </div>

            </c:otherwise>

        </c:choose>

    </div>

</main>

<jsp:include
    page="/WEB-INF/views/common/footer.jsp"/>

<script
    src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js">
</script>

</body>
</html>