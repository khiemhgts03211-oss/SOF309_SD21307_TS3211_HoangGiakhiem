<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<body>

<jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/booking.css">

<div class="booking-page">

    <div class="booking-header">

        <div class="bh-item">
            <i class="bi bi-film"></i>
            <span>${movie.title}</span>
        </div>

        <div class="bh-item">
            <i class="bi bi-calendar3"></i>
            <fmt:formatDate value="${showtime.showDate}" pattern="dd/MM/yyyy"/>
        </div>

        <div class="bh-item">
            <i class="bi bi-clock"></i>
            <fmt:formatDate value="${showtime.startTime}" pattern="HH:mm"/>
        </div>

        <div class="bh-item">
            <i class="bi bi-door-open"></i>
            ${room.roomName}
        </div>

    </div>

    <div class="cinema-room">

        <div class="screen">MÀN HÌNH</div>

        <div id="seatMap" class="seat-map"></div>

        <div class="legend">

            <div class="legend-item">
                <span class="seat-demo available"></span>
                Ghế trống
            </div>

            <div class="legend-item">
                <span class="seat-demo selected"></span>
                Đang chọn
            </div>

            <div class="legend-item">
                <span class="seat-demo sold"></span>
                Đã bán
            </div>

            <div class="legend-item">
                <span class="seat-demo vip"></span>
                Ghế VIP
            </div>

        </div>

    </div>

    <div class="booking-summary">

        <h4>${movie.title}</h4>

        <p class="bs-sub">
            <fmt:formatDate value="${showtime.showDate}" pattern="dd/MM/yyyy"/>
            &middot;
            <fmt:formatDate value="${showtime.startTime}" pattern="HH:mm"/>
            &middot;
            ${room.roomName}
        </p>

        <div class="bs-row">
            <span>Ghế đã chọn</span>
            <span id="selectedSeatsText">Chưa chọn ghế</span>
        </div>

        <div class="bs-row">
            <span>Số lượng</span>
            <span id="seatCount">0</span>
        </div>

        <div class="bs-row">
            <span>Giá vé</span>
            <span><fmt:formatNumber value="${showtime.ticketPrice}" pattern="#,###"/>đ / ghế</span>
        </div>

        <div class="bs-row bs-total">
            <span>Tổng tiền</span>
            <span id="totalPrice">0đ</span>
        </div>

        <div class="bs-actions">

            <button type="button" id="cancelBtn" class="btn-cancel">Hủy</button>

            <button type="button" id="confirmBtn" class="btn-confirm">Đặt Vé</button>

        </div>

    </div>

</div>

<script>
    const ROOM_CAPACITY = ${not empty room.capacity ? room.capacity : 60};
    const TICKET_PRICE = ${not empty showtime.ticketPrice ? showtime.ticketPrice : 0};
</script>

<script src="${pageContext.request.contextPath}/assets/js/booking.js"></script>

</body>
</html>
