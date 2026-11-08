<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<body>

<jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

<link rel="stylesheet"
      href="${pageContext.request.contextPath}/assets/css/booking-services.css">

<main class="booking-services-page">

    <div class="container">

        <%-- ==============================
             THÔNG BÁO
             ============================== --%>

        <c:if test="${not empty errorMessage}">

            <div class="alert alert-danger alert-dismissible fade show mb-4">

                <c:out value="${errorMessage}"/>

                <button type="button"
                        class="btn-close"
                        data-bs-dismiss="alert">
                </button>

            </div>

        </c:if>

        <c:if test="${param.error eq 'invalid_service'}">

            <div class="alert alert-warning alert-dismissible fade show mb-4">

                Dịch vụ được chọn không hợp lệ.

                <button type="button"
                        class="btn-close"
                        data-bs-dismiss="alert">
                </button>

            </div>

        </c:if>

        <c:if test="${param.error eq 'invalid_quantity'}">

            <div class="alert alert-warning alert-dismissible fade show mb-4">

                Số lượng dịch vụ không hợp lệ.

                <button type="button"
                        class="btn-close"
                        data-bs-dismiss="alert">
                </button>

            </div>

        </c:if>

        <c:if test="${param.error eq 'booking_session_expired'}">

            <div class="alert alert-warning alert-dismissible fade show mb-4">

                Phiên đặt vé đã hết hạn.
                Vui lòng chọn lại suất chiếu.

                <button type="button"
                        class="btn-close"
                        data-bs-dismiss="alert">
                </button>

            </div>

        </c:if>

        <%-- ==============================
             PAGE HEADING
             ============================== --%>

        <div class="booking-services-heading">

            <p class="booking-services-label">
                Bước 2
            </p>

            <h1>
                Chọn dịch vụ
            </h1>

            <span>
                Chọn thêm đồ ăn, thức uống hoặc combo.
                Bạn cũng có thể bỏ qua nếu không có nhu cầu.
            </span>

        </div>

        <c:choose>

            <%-- ==============================
                 KHÔNG CÓ BOOKING TẠM
                 ============================== --%>

            <c:when test="${empty showtime}">

                <section class="service-empty">

                    <h2>
                        Không tìm thấy thông tin đặt vé
                    </h2>

                    <p>
                        Phiên đặt vé không còn hợp lệ.
                        Vui lòng chọn lại suất chiếu.
                    </p>

                    <a href="${pageContext.request.contextPath}/home"
                       class="btn-booking-primary">

                        Quay lại trang chủ

                    </a>

                </section>

            </c:when>

            <%-- ==============================
                 BOOKING HỢP LỆ
                 ============================== --%>

            <c:otherwise>

                <div class="booking-services-layout">

                    <%-- ==============================
                         DANH SÁCH DỊCH VỤ
                         ============================== --%>

                    <section class="service-list-card">

                        <div class="service-list-heading">

                            <div>

                                <p class="service-list-label">
                                    Dịch vụ khả dụng
                                </p>

                                <h2>
                                    Đồ ăn &amp; thức uống
                                </h2>

                                <p>

                                    Hiện có

                                    <strong>
                                        ${empty serviceCount
                                            ? 0
                                            : serviceCount}
                                    </strong>

                                    dịch vụ.

                                </p>

                            </div>

                        </div>

                        <form
                            action="${pageContext.request.contextPath}/booking/services/confirm"
                            method="post"
                            id="serviceForm">

                            <%--
                                Không gửi giá dịch vụ để backend tin tưởng.
                                Giá trong hidden input phía dưới
                                chỉ dùng cho JavaScript tính tạm.
                            --%>

                            <c:choose>

                                <%-- Không có dịch vụ --%>

                                <c:when test="${empty services}">

                                    <div class="service-empty">

                                        <div class="service-empty-icon">
                                            🍿
                                        </div>

                                        <h3>
                                            Chưa có dịch vụ
                                        </h3>

                                        <p>
                                            Hiện chưa có đồ ăn hoặc thức uống
                                            nào khả dụng.
                                            Bạn vẫn có thể tiếp tục đặt vé.
                                        </p>

                                    </div>

                                </c:when>

                                <%-- Có dịch vụ --%>

                                <c:otherwise>

                                    <div class="service-grid">

                                        <c:forEach
                                            items="${services}"
                                            var="service">

                                            <c:if test="${not empty service
                                                        and service.serviceId > 0}">

                                                <article class="service-card">

                                                    <div class="service-card-content">

                                                        <%-- CATEGORY --%>

                                                        <div class="service-category">

                                                            <c:choose>

                                                                <c:when test="${not empty service.categoryName}">

                                                                    <c:out
                                                                        value="${service.categoryName}"/>

                                                                </c:when>

                                                                <c:otherwise>

                                                                    Dịch vụ

                                                                </c:otherwise>

                                                            </c:choose>

                                                        </div>

                                                        <%-- SERVICE NAME --%>

                                                        <h3 class="service-name">

                                                            <c:choose>

                                                                <c:when test="${not empty service.serviceName}">

                                                                    <c:out
                                                                        value="${service.serviceName}"/>

                                                                </c:when>

                                                                <c:otherwise>

                                                                    Dịch vụ #${service.serviceId}

                                                                </c:otherwise>

                                                            </c:choose>

                                                        </h3>

                                                        <%-- STATUS --%>

                                                        <c:if test="${not empty service.status}">

                                                            <span class="service-status">

                                                                <c:out
                                                                    value="${service.status}"/>

                                                            </span>

                                                        </c:if>

                                                        <%-- PRICE --%>

                                                        <div class="service-price">

                                                            <c:choose>

                                                                <c:when test="${not empty service.price}">

                                                                    <fmt:formatNumber
                                                                        value="${service.price}"
                                                                        type="number"
                                                                        groupingUsed="true"/>

                                                                    VNĐ

                                                                </c:when>

                                                                <c:otherwise>

                                                                    0 VNĐ

                                                                </c:otherwise>

                                                            </c:choose>

                                                        </div>

                                                        <%--
                                                            Chỉ dùng JS tính tạm.
                                                            Backend phải truy vấn lại giá.
                                                        --%>

                                                        <input
                                                            type="hidden"
                                                            class="service-price-input"
                                                            data-service-id="${service.serviceId}"
                                                            value="${service.price}">

                                                        <%-- QUANTITY --%>

                                                        <div class="service-quantity">

                                                            <button
                                                                type="button"
                                                                class="quantity-button decrease-service"
                                                                data-service-id="${service.serviceId}"
                                                                aria-label="Giảm số lượng">

                                                                −

                                                            </button>

                                                            <input
                                                                type="number"
                                                                id="service_${service.serviceId}"
                                                                name="service_${service.serviceId}"
                                                                class="service-quantity-input"
                                                                data-service-id="${service.serviceId}"
                                                                min="0"
                                                                max="20"
                                                                value="0"
                                                                inputmode="numeric">

                                                            <button
                                                                type="button"
                                                                class="quantity-button increase-service"
                                                                data-service-id="${service.serviceId}"
                                                                aria-label="Tăng số lượng">

                                                                +

                                                            </button>

                                                        </div>

                                                        <div class="service-subtotal">

                                                            Thành tiền:

                                                            <strong
                                                                id="subtotal_${service.serviceId}">

                                                                0 VNĐ

                                                            </strong>

                                                        </div>

                                                    </div>

                                                </article>

                                            </c:if>

                                        </c:forEach>

                                    </div>

                                </c:otherwise>

                            </c:choose>

                            <%-- ==============================
                                 ACTION
                                 ============================== --%>

                            <div class="service-actions">

                                <a
                                    href="${pageContext.request.contextPath}/booking/create?showtimeId=${showtime.showtimeId}"
                                    class="btn-booking-outline">

                                    Quay lại

                                </a>

                                <button
                                    type="submit"
                                    class="btn-booking-primary">

                                    Tiếp tục xác nhận

                                </button>

                            </div>

                        </form>

                    </section>

                    <%-- ==============================
                         BOOKING SUMMARY
                         ============================== --%>

                    <aside class="booking-summary-card">

                        <div class="booking-summary-heading">

                            <p>
                                Đơn hàng
                            </p>

                            <h2>
                                Tóm tắt đặt vé
                            </h2>

                        </div>

                        <%-- MOVIE --%>

                        <div class="summary-row">

                            <span>
                                Phim
                            </span>

                            <strong>

                                <c:choose>

                                    <c:when test="${not empty showtime.movieTitle}">

                                        <c:out value="${showtime.movieTitle}"/>

                                    </c:when>

                                    <c:otherwise>

                                        Movie #${showtime.movieId}

                                    </c:otherwise>

                                </c:choose>

                            </strong>

                        </div>

                        <%-- ROOM --%>

                        <div class="summary-row">

                            <span>
                                Phòng
                            </span>

                            <strong>

                                <c:choose>

                                    <c:when test="${not empty showtime.roomName}">

                                        <c:out value="${showtime.roomName}"/>

                                    </c:when>

                                    <c:otherwise>

                                        Phòng #${showtime.roomId}

                                    </c:otherwise>

                                </c:choose>

                            </strong>

                        </div>

                        <%-- DATE --%>

                        <div class="summary-row">

                            <span>
                                Ngày chiếu
                            </span>

                            <strong>

                                <c:choose>

                                    <c:when test="${not empty showtime.showDate}">

                                        <fmt:formatDate
                                            value="${showtime.showDate}"
                                            pattern="dd/MM/yyyy"/>

                                    </c:when>

                                    <c:otherwise>

                                        Chưa cập nhật

                                    </c:otherwise>

                                </c:choose>

                            </strong>

                        </div>

                        <%-- TIME --%>

                        <div class="summary-row">

                            <span>
                                Giờ chiếu
                            </span>

                            <strong>

                                <c:choose>

                                    <c:when test="${not empty showtime.startTime}">

                                        <fmt:formatDate
                                            value="${showtime.startTime}"
                                            pattern="HH:mm"/>

                                    </c:when>

                                    <c:otherwise>
                                        --:--
                                    </c:otherwise>

                                </c:choose>

                            </strong>

                        </div>

                        <%-- PEOPLE --%>

                        <div class="summary-row">

                            <span>
                                Số người
                            </span>

                            <strong>
                                ${empty peopleCount ? 0 : peopleCount}
                            </strong>

                        </div>

                        <hr>

                        <%-- TICKET TOTAL --%>

                        <div class="summary-row">

                            <span>
                                Tiền vé
                            </span>

                            <strong>

                                <c:choose>

                                    <c:when test="${not empty ticketTotal}">

                                        <fmt:formatNumber
                                            value="${ticketTotal}"
                                            type="number"
                                            groupingUsed="true"/>

                                        VNĐ

                                    </c:when>

                                    <c:otherwise>

                                        0 VNĐ

                                    </c:otherwise>

                                </c:choose>

                            </strong>

                        </div>

                        <%-- SERVICE TOTAL --%>

                        <div class="summary-row">

                            <span>
                                Tiền dịch vụ
                            </span>

                            <strong id="serviceTotal">
                                0 VNĐ
                            </strong>

                        </div>

                        <%-- GRAND TOTAL --%>

                        <div class="summary-total">

                            <span>
                                Tổng tạm tính
                            </span>

                            <strong id="grandTotal">

                                <c:choose>

                                    <c:when test="${not empty ticketTotal}">

                                        <fmt:formatNumber
                                            value="${ticketTotal}"
                                            type="number"
                                            groupingUsed="true"/>

                                        VNĐ

                                    </c:when>

                                    <c:otherwise>

                                        0 VNĐ

                                    </c:otherwise>

                                </c:choose>

                            </strong>

                        </div>

                        <p class="summary-note">

                            Giá hiển thị tại đây chỉ là tạm tính.
                            Hệ thống sẽ kiểm tra lại giá dịch vụ
                            từ cơ sở dữ liệu trước khi tạo vé.

                        </p>

                    </aside>

                </div>

                <%--
                    Giá vé tạm để JavaScript hiển thị tổng.
                    Không submit input này.
                --%>

                <input
                    type="hidden"
                    id="ticketTotalValue"
                    value="${ticketTotal}">

            </c:otherwise>

        </c:choose>

    </div>

</main>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>

<script>
(function () {

    const quantityInputs =
            document.querySelectorAll(
                    ".service-quantity-input");

    const increaseButtons =
            document.querySelectorAll(
                    ".increase-service");

    const decreaseButtons =
            document.querySelectorAll(
                    ".decrease-service");

    const serviceTotalElement =
            document.getElementById(
                    "serviceTotal");

    const grandTotalElement =
            document.getElementById(
                    "grandTotal");

    const ticketTotalInput =
            document.getElementById(
                    "ticketTotalValue");

    const MIN_QUANTITY = 0;
    const MAX_QUANTITY = 20;

    function formatMoney(value) {

        const safeValue =
                Number.isFinite(value)
                        ? value
                        : 0;

        return new Intl.NumberFormat("vi-VN")
                .format(safeValue)
                + " VNĐ";
    }

    function getTicketTotal() {

        if (!ticketTotalInput) {
            return 0;
        }

        const value =
                Number.parseFloat(
                        ticketTotalInput.value);

        return Number.isFinite(value)
                ? value
                : 0;
    }

    function getServicePrice(serviceId) {

        const priceInput =
                document.querySelector(
                    '.service-price-input[data-service-id="'
                    + serviceId
                    + '"]');

        if (!priceInput) {
            return 0;
        }

        const value =
                Number.parseFloat(
                        priceInput.value);

        return Number.isFinite(value)
                ? value
                : 0;
    }

    function getQuantity(input) {

        if (!input) {
            return 0;
        }

        let value =
                Number.parseInt(
                        input.value,
                        10);

        if (!Number.isFinite(value)) {
            value = MIN_QUANTITY;
        }

        if (value < MIN_QUANTITY) {
            value = MIN_QUANTITY;
        }

        if (value > MAX_QUANTITY) {
            value = MAX_QUANTITY;
        }

        input.value = value;

        return value;
    }

    function updateSubtotal(input) {

        if (!input) {
            return 0;
        }

        const serviceId =
                input.dataset.serviceId;

        const quantity =
                getQuantity(input);

        const price =
                getServicePrice(serviceId);

        const subtotal =
                quantity * price;

        const subtotalElement =
                document.getElementById(
                        "subtotal_"
                        + serviceId);

        if (subtotalElement) {

            subtotalElement.textContent =
                    formatMoney(subtotal);
        }

        return subtotal;
    }

    function updateTotals() {

        let serviceTotal = 0;

        quantityInputs.forEach(
                function (input) {

                    serviceTotal +=
                            updateSubtotal(input);
                });

        const grandTotal =
                getTicketTotal()
                + serviceTotal;

        if (serviceTotalElement) {

            serviceTotalElement.textContent =
                    formatMoney(serviceTotal);
        }

        if (grandTotalElement) {

            grandTotalElement.textContent =
                    formatMoney(grandTotal);
        }
    }

    increaseButtons.forEach(
            function (button) {

                button.addEventListener(
                        "click",
                        function () {

                            const serviceId =
                                    button.dataset.serviceId;

                            const input =
                                    document.getElementById(
                                            "service_"
                                            + serviceId);

                            if (!input) {
                                return;
                            }

                            const current =
                                    getQuantity(input);

                            if (current < MAX_QUANTITY) {

                                input.value =
                                        current + 1;
                            }

                            updateTotals();
                        });
            });

    decreaseButtons.forEach(
            function (button) {

                button.addEventListener(
                        "click",
                        function () {

                            const serviceId =
                                    button.dataset.serviceId;

                            const input =
                                    document.getElementById(
                                            "service_"
                                            + serviceId);

                            if (!input) {
                                return;
                            }

                            const current =
                                    getQuantity(input);

                            if (current > MIN_QUANTITY) {

                                input.value =
                                        current - 1;
                            }

                            updateTotals();
                        });
            });

    quantityInputs.forEach(
            function (input) {

                input.addEventListener(
                        "input",
                        updateTotals);

                input.addEventListener(
                        "change",
                        updateTotals);
            });

    updateTotals();

})();
</script>

<script
    src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js">
</script>

</body>
</html>