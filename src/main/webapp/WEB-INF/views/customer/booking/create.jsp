<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<body>

<jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

<link rel="stylesheet"
      href="${pageContext.request.contextPath}/assets/css/booking-create.css">

<main class="booking-create-page">

    <div class="container">

        <%-- =====================================================
             THÔNG BÁO
             ===================================================== --%>

        <c:if test="${not empty errorMessage}">

            <div class="alert alert-danger alert-dismissible fade show mb-4">

                <c:out value="${errorMessage}"/>

                <button type="button"
                        class="btn-close"
                        data-bs-dismiss="alert">
                </button>

            </div>

        </c:if>

        <c:if test="${param.error eq 'invalid_people_count'}">

            <div class="alert alert-warning alert-dismissible fade show mb-4">

                Số lượng người phải từ 1 đến 20.

                <button type="button"
                        class="btn-close"
                        data-bs-dismiss="alert">
                </button>

            </div>

        </c:if>

        <c:if test="${param.error eq 'server_error'}">

            <div class="alert alert-danger alert-dismissible fade show mb-4">

                Đã xảy ra lỗi khi xử lý đặt vé.
                Vui lòng thử lại.

                <button type="button"
                        class="btn-close"
                        data-bs-dismiss="alert">
                </button>

            </div>

        </c:if>

        <c:choose>

            <%-- =================================================
                 KHÔNG CÓ SHOWTIME
                 ================================================= --%>

            <c:when test="${empty showtime}">

                <section class="booking-empty-box">

                    <div class="booking-empty-icon">
                        🎬
                    </div>

                    <h1>
                        Không tìm thấy suất chiếu
                    </h1>

                    <p>
                        Suất chiếu không tồn tại
                        hoặc không còn khả dụng.
                    </p>

                    <a
                        href="${pageContext.request.contextPath}/home"
                        class="btn-booking-primary">

                        Quay lại trang chủ

                    </a>

                </section>

            </c:when>

            <%-- =================================================
                 CÓ SHOWTIME
                 ================================================= --%>

            <c:otherwise>

                <div class="booking-page-heading">

                    <p class="booking-page-label">
                        Đặt vé xem phim
                    </p>

                    <h1>
                        Xác nhận suất chiếu
                    </h1>

                    <span>
                        Kiểm tra phòng chiếu
                        và chọn số lượng người trước khi tiếp tục.
                    </span>

                </div>

                <div class="booking-layout">

                    <%-- =================================================
                         THÔNG TIN SUẤT CHIẾU
                         ================================================= --%>

                    <section class="booking-showtime-card">

                        <div class="booking-showtime-heading">

                            <div>

                                <span class="booking-small-label">
                                    Phim
                                </span>

                                <h2>

                                    <c:choose>

                                        <c:when test="${not empty showtime.movieTitle}">

                                            <c:out value="${showtime.movieTitle}"/>

                                        </c:when>

                                        <c:otherwise>

                                            Movie #${showtime.movieId}

                                        </c:otherwise>

                                    </c:choose>

                                </h2>

                            </div>

                            <span class="booking-status">

                                <c:out value="${showtime.status}"/>

                            </span>

                        </div>

                        <div class="booking-showtime-information">

                            <%-- NGÀY CHIẾU --%>

                            <div class="booking-information-item">

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

                            <%-- GIỜ CHIẾU --%>

                            <div class="booking-information-item">

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

                                    <span>
                                        -
                                    </span>

                                    <c:choose>

                                        <c:when test="${not empty showtime.endTime}">

                                            <fmt:formatDate
                                                value="${showtime.endTime}"
                                                pattern="HH:mm"/>

                                        </c:when>

                                        <c:otherwise>
                                            --:--
                                        </c:otherwise>

                                    </c:choose>

                                </strong>

                            </div>

                            <%-- PHÒNG CHIẾU --%>

                            <div class="booking-information-item">

                                <span>
                                    Phòng chiếu
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

                            <%-- GIÁ VÉ --%>

                            <div class="booking-information-item">

                                <span>
                                    Giá mỗi vé
                                </span>

                                <strong class="booking-price">

                                    <c:choose>

                                        <c:when test="${not empty showtime.ticketPrice}">

                                            <fmt:formatNumber
                                                value="${showtime.ticketPrice}"
                                                type="number"
                                                groupingUsed="true"/>

                                            VNĐ

                                        </c:when>

                                        <c:otherwise>

                                            Chưa cập nhật

                                        </c:otherwise>

                                    </c:choose>

                                </strong>

                            </div>

                        </div>

                        <%-- =================================================
                             XÁC NHẬN PHÒNG
                             ================================================= --%>

                        <div class="booking-room-confirm">

                            <div>

                                <span class="booking-room-label">
                                    Phòng đã xác nhận
                                </span>

                                <strong class="booking-room-name">

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

                            <span class="booking-room-badge">
                                Đã xác nhận
                            </span>

                        </div>

                        <p class="booking-room-note">

                            Phòng được xác định tự động theo suất chiếu.
                            Bạn không thể thay đổi phòng ở bước này.

                        </p>

                    </section>

                    <%-- =================================================
                         FORM ĐẶT VÉ
                         ================================================= --%>

                    <section class="booking-form-card">

                        <div class="booking-form-heading">

                            <h2>
                                Thông tin đặt vé
                            </h2>

                            <p>
                                Chọn số lượng người để tiếp tục.
                            </p>

                        </div>

                        <form
                            action="${pageContext.request.contextPath}/booking/confirm"
                            method="post"
                            id="bookingForm">

                            <%-- Chỉ gửi showtimeId.
                                 Không gửi roomId.
                                 Không gửi totalPrice. --%>

                            <input
                                type="hidden"
                                name="showtimeId"
                                value="${showtime.showtimeId}">

                            <%-- Giá này chỉ dùng để JS hiển thị tạm.
                                 Backend vẫn lấy lại giá từ DB. --%>

                            <input
                                type="hidden"
                                id="ticketPrice"
                                value="${showtime.ticketPrice}">

                            <%-- =================================================
                                 24.2 CHỌN SỐ LƯỢNG NGƯỜI
                                 ================================================= --%>

                            <div class="booking-form-group">

                                <label for="peopleCount">
                                    Số lượng người
                                </label>

                                <div class="people-count-control">

                                    <button
                                        type="button"
                                        id="decreasePeople"
                                        class="people-count-button"
                                        aria-label="Giảm số lượng">

                                        −

                                    </button>

                                    <input
                                        type="number"
                                        id="peopleCount"
                                        name="peopleCount"
                                        min="1"
                                        max="20"
                                        value="1"
                                        required>

                                    <button
                                        type="button"
                                        id="increasePeople"
                                        class="people-count-button"
                                        aria-label="Tăng số lượng">

                                        +

                                    </button>

                                </div>

                                <small>
                                    Tối thiểu 1 người,
                                    tối đa 20 người cho mỗi lần đặt.
                                </small>

                            </div>

                            <%-- CUSTOMER --%>

                            <div class="booking-summary-row">

                                <span>
                                    Khách hàng
                                </span>

                                <strong>

                                    <c:choose>

                                        <c:when test="${not empty customer.fullName}">

                                            <c:out value="${customer.fullName}"/>

                                        </c:when>

                                        <c:when test="${not empty customer.username}">

                                            <c:out value="${customer.username}"/>

                                        </c:when>

                                        <c:otherwise>

                                            Khách hàng

                                        </c:otherwise>

                                    </c:choose>

                                </strong>

                            </div>

                            <%-- PHÒNG --%>

                            <div class="booking-summary-row">

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

                            <%-- GIÁ MỖI VÉ --%>

                            <div class="booking-summary-row">

                                <span>
                                    Giá mỗi vé
                                </span>

                                <strong>

                                    <fmt:formatNumber
                                        value="${showtime.ticketPrice}"
                                        type="number"
                                        groupingUsed="true"/>

                                    VNĐ

                                </strong>

                            </div>

                            <%-- SỐ LƯỢNG --%>

                            <div class="booking-summary-row">

                                <span>
                                    Số lượng
                                </span>

                                <strong id="peopleCountPreview">
                                    1 người
                                </strong>

                            </div>

                            <%-- TỔNG TIỀN TẠM TÍNH --%>

                            <div class="booking-total-box">

                                <span>
                                    Tổng tiền vé tạm tính
                                </span>

                                <strong id="totalPrice">

                                    <fmt:formatNumber
                                        value="${showtime.ticketPrice}"
                                        type="number"
                                        groupingUsed="true"/>

                                    VNĐ

                                </strong>

                            </div>

                            <p class="booking-total-note">

                                Tổng tiền này chỉ là tạm tính.
                                Hệ thống sẽ tính lại giá chính xác
                                ở bước tiếp theo.

                            </p>

                            <%-- ACTION --%>

                            <div class="booking-form-actions">

                                <a
                                    href="${pageContext.request.contextPath}/showtimes?movieId=${showtime.movieId}"
                                    class="btn-booking-outline">

                                    Quay lại lịch chiếu

                                </a>

                                <button
                                    type="submit"
                                    class="btn-booking-primary">

                                    Tiếp tục chọn dịch vụ

                                </button>

                            </div>

                        </form>

                    </section>

                </div>

            </c:otherwise>

        </c:choose>

    </div>

</main>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>

<script>
(function () {

    const peopleInput =
            document.getElementById("peopleCount");

    const decreaseButton =
            document.getElementById("decreasePeople");

    const increaseButton =
            document.getElementById("increasePeople");

    const ticketPriceInput =
            document.getElementById("ticketPrice");

    const totalPriceElement =
            document.getElementById("totalPrice");

    const peoplePreview =
            document.getElementById("peopleCountPreview");

    if (!peopleInput
            || !ticketPriceInput
            || !totalPriceElement) {

        return;
    }

    const MIN_PEOPLE = 1;
    const MAX_PEOPLE = 20;

    function getPeopleCount() {

        let value =
                Number.parseInt(
                        peopleInput.value,
                        10);

        if (!Number.isFinite(value)) {
            value = MIN_PEOPLE;
        }

        if (value < MIN_PEOPLE) {
            value = MIN_PEOPLE;
        }

        if (value > MAX_PEOPLE) {
            value = MAX_PEOPLE;
        }

        peopleInput.value = value;

        return value;
    }

    function getTicketPrice() {

        const value =
                Number.parseFloat(
                        ticketPriceInput.value);

        return Number.isFinite(value)
                ? value
                : 0;
    }

    function updateSummary() {

        const peopleCount =
                getPeopleCount();

        const ticketPrice =
                getTicketPrice();

        const total =
                peopleCount * ticketPrice;

        totalPriceElement.textContent =
                new Intl.NumberFormat("vi-VN")
                        .format(total)
                + " VNĐ";

        if (peoplePreview) {

            peoplePreview.textContent =
                    peopleCount + " người";
        }

        if (decreaseButton) {

            decreaseButton.disabled =
                    peopleCount <= MIN_PEOPLE;
        }

        if (increaseButton) {

            increaseButton.disabled =
                    peopleCount >= MAX_PEOPLE;
        }
    }

    if (decreaseButton) {

        decreaseButton.addEventListener(
                "click",
                function () {

                    const current =
                            getPeopleCount();

                    if (current > MIN_PEOPLE) {

                        peopleInput.value =
                                current - 1;

                        updateSummary();
                    }
                });
    }

    if (increaseButton) {

        increaseButton.addEventListener(
                "click",
                function () {

                    const current =
                            getPeopleCount();

                    if (current < MAX_PEOPLE) {

                        peopleInput.value =
                                current + 1;

                        updateSummary();
                    }
                });
    }

    peopleInput.addEventListener(
            "input",
            updateSummary);

    peopleInput.addEventListener(
            "change",
            updateSummary);

    document.getElementById("bookingForm")
            ?.addEventListener(
                    "submit",
                    function (event) {

                        const peopleCount =
                                getPeopleCount();

                        if (peopleCount < MIN_PEOPLE
                                || peopleCount > MAX_PEOPLE) {

                            event.preventDefault();

                            alert(
                                "Số lượng người phải từ 1 đến 20.");
                        }
                    });

    updateSummary();

})();
</script>

<script
    src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js">
</script>

</body>
</html>