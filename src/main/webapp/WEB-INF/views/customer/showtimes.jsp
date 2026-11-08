<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<body>

<jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

<link rel="stylesheet"
      href="${pageContext.request.contextPath}/assets/css/showtimes.css">

<main class="customer-showtime-page">

    <div class="container">

        <%-- ===============================
             THÔNG BÁO
             =============================== --%>

        <c:if test="${not empty errorMessage}">

            <div class="alert alert-danger alert-dismissible fade show mb-4">

                <c:out value="${errorMessage}"/>

                <button
                    type="button"
                    class="btn-close"
                    data-bs-dismiss="alert">
                </button>

            </div>

        </c:if>

        <c:if test="${param.error eq 'showtime_unavailable'}">

            <div class="alert alert-warning alert-dismissible fade show mb-4">

                Suất chiếu đã đóng, bị hủy hoặc không còn khả dụng.

                <button
                    type="button"
                    class="btn-close"
                    data-bs-dismiss="alert">
                </button>

            </div>

        </c:if>

        <c:if test="${param.error eq 'invalid_showtime'}">

            <div class="alert alert-warning alert-dismissible fade show mb-4">

                Suất chiếu không hợp lệ.

                <button
                    type="button"
                    class="btn-close"
                    data-bs-dismiss="alert">
                </button>

            </div>

        </c:if>

        <c:if test="${param.error eq 'showtime_not_found'}">

            <div class="alert alert-warning alert-dismissible fade show mb-4">

                Không tìm thấy suất chiếu.

                <button
                    type="button"
                    class="btn-close"
                    data-bs-dismiss="alert">
                </button>

            </div>

        </c:if>

        <c:if test="${param.error eq 'forbidden'}">

            <div class="alert alert-warning alert-dismissible fade show mb-4">

                Chỉ tài khoản khách hàng mới được đặt vé.

                <button
                    type="button"
                    class="btn-close"
                    data-bs-dismiss="alert">
                </button>

            </div>

        </c:if>

        <c:choose>

            <%-- ===============================
                 KHÔNG CÓ PHIM
                 =============================== --%>

            <c:when test="${empty movie}">

                <section class="empty-showtime-box">

                    <div class="empty-showtime-icon">
                        🎬
                    </div>

                    <h1>
                        Không tìm thấy phim
                    </h1>

                    <p>
                        Phim không tồn tại hoặc không còn khả dụng.
                    </p>

                    <a
                        href="${pageContext.request.contextPath}/home"
                        class="btn-empty-showtime">

                        Quay lại trang chủ

                    </a>

                </section>

            </c:when>

            <%-- ===============================
                 CÓ PHIM
                 =============================== --%>

            <c:otherwise>

                <section class="showtime-movie-summary">

                    <div class="summary-poster">

                        <c:choose>

                            <c:when test="${not empty movie.poster}">

                                <img
                                    src="${pageContext.request.contextPath}/${movie.poster}"
                                    alt="<c:out value='${movie.title}'/>"
                                    loading="lazy"
                                    onerror="this.onerror=null;
                                             this.src='${pageContext.request.contextPath}/assets/images/logo.png';">

                            </c:when>

                            <c:otherwise>

                                <img
                                    src="${pageContext.request.contextPath}/assets/images/logo.png"
                                    alt="Không có poster">

                            </c:otherwise>

                        </c:choose>

                    </div>

                    <div class="summary-content">

                        <p class="summary-label">
                            Lịch chiếu phim
                        </p>

                        <h1 class="summary-title">

                            <c:out value="${movie.title}"/>

                        </h1>

                        <div class="summary-information">

                            <div class="summary-information-item">

                                <span>
                                    Thể loại
                                </span>

                                <strong>

                                    <c:choose>

                                        <c:when test="${not empty movie.genre}">

                                            <c:out value="${movie.genre}"/>

                                        </c:when>

                                        <c:otherwise>
                                            Chưa cập nhật
                                        </c:otherwise>

                                    </c:choose>

                                </strong>

                            </div>

                            <div class="summary-information-item">

                                <span>
                                    Thời lượng
                                </span>

                                <strong>

                                    <c:choose>

                                        <c:when test="${movie.duration > 0}">

                                            ${movie.duration} phút

                                        </c:when>

                                        <c:otherwise>
                                            Chưa cập nhật
                                        </c:otherwise>

                                    </c:choose>

                                </strong>

                            </div>

                            <div class="summary-information-item">

                                <span>
                                    Khởi chiếu
                                </span>

                                <strong>

                                    <c:choose>

                                        <c:when test="${not empty movie.releaseDate}">

                                            <fmt:formatDate
                                                value="${movie.releaseDate}"
                                                pattern="dd/MM/yyyy"/>

                                        </c:when>

                                        <c:otherwise>
                                            Chưa cập nhật
                                        </c:otherwise>

                                    </c:choose>

                                </strong>

                            </div>

                        </div>

                        <div class="summary-actions">

                            <a
                                href="${pageContext.request.contextPath}/movies/detail?id=${movie.movieId}"
                                class="btn-summary-outline">

                                Chi tiết phim

                            </a>

                            <a
                                href="${pageContext.request.contextPath}/home"
                                class="btn-summary-primary">

                                Danh sách phim

                            </a>

                        </div>

                    </div>

                </section>

                <%-- ===============================
                     DANH SÁCH SUẤT CHIẾU
                     =============================== --%>

                <section class="showtime-list-section">

                    <div class="showtime-section-heading">

                        <div>

                            <p class="showtime-section-label">
                                Chọn suất chiếu phù hợp
                            </p>

                            <h2>
                                Danh sách suất chiếu
                            </h2>

                            <p class="showtime-count">

                                Hiện có

                                <strong>
                                    ${empty showtimeCount
                                        ? 0
                                        : showtimeCount}
                                </strong>

                                suất chiếu khả dụng.

                            </p>

                        </div>

                    </div>

                    <c:choose>

                        <%-- Không có suất chiếu --%>

                        <c:when test="${empty showtimes}">

                            <div class="empty-showtime-box">

                                <div class="empty-showtime-icon">
                                    📅
                                </div>

                                <h3>
                                    Hiện chưa có suất chiếu
                                </h3>

                                <p>
                                    Phim này chưa có lịch chiếu khả dụng.
                                    Vui lòng quay lại sau.
                                </p>

                                <div class="empty-showtime-actions">

                                    <a
                                        href="${pageContext.request.contextPath}/movies/detail?id=${movie.movieId}"
                                        class="btn-empty-outline">

                                        Quay lại chi tiết phim

                                    </a>

                                    <a
                                        href="${pageContext.request.contextPath}/home"
                                        class="btn-empty-showtime">

                                        Chọn phim khác

                                    </a>

                                </div>

                            </div>

                        </c:when>

                        <%-- Có suất chiếu --%>

                        <c:otherwise>

                            <div class="showtime-grid">

                                <c:forEach
                                    items="${showtimes}"
                                    var="showtime">

                                    <c:if test="${not empty showtime
                                                and showtime.showtimeId > 0}">

                                        <article class="showtime-card">

                                            <div class="showtime-card-header">

                                                <div>

                                                    <span class="showtime-card-label">
                                                        Giờ chiếu
                                                    </span>

                                                    <div class="showtime-time">

                                                        <span class="start-time">

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

                                                        </span>

                                                        <span class="time-separator">
                                                            -
                                                        </span>

                                                        <span class="end-time">

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

                                                        </span>

                                                    </div>

                                                </div>

                                                <c:choose>

                                                    <c:when test="${showtime.status eq 'OPEN'
                                                                or showtime.status eq 'ACTIVE'}">

                                                        <span class="showtime-status status-open">

                                                            <c:out value="${showtime.status}"/>

                                                        </span>

                                                    </c:when>

                                                    <c:when test="${showtime.status eq 'CLOSED'}">

                                                        <span class="showtime-status status-closed">
                                                            CLOSED
                                                        </span>

                                                    </c:when>

                                                    <c:when test="${showtime.status eq 'CANCELLED'}">

                                                        <span class="showtime-status status-cancelled">
                                                            CANCELLED
                                                        </span>

                                                    </c:when>

                                                    <c:otherwise>

                                                        <span class="showtime-status status-unknown">

                                                            <c:out value="${showtime.status}"/>

                                                        </span>

                                                    </c:otherwise>

                                                </c:choose>

                                            </div>

                                            <div class="showtime-details">

                                                <div class="showtime-detail-row">

                                                    <span class="detail-label">
                                                        Ngày chiếu
                                                    </span>

                                                    <strong class="detail-value">

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

                                                <div class="showtime-detail-row">

                                                    <span class="detail-label">
                                                        Phòng chiếu
                                                    </span>

                                                    <strong class="detail-value">

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

                                                <div class="showtime-detail-row">

                                                    <span class="detail-label">
                                                        Giá vé
                                                    </span>

                                                    <strong class="showtime-price">

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

                                            <%-- ===============================
                                                 NÚT ĐẶT VÉ THEO QUYỀN
                                                 =============================== --%>

                                            <c:choose>

                                                <%-- Suất chiếu không khả dụng --%>

                                                <c:when test="${showtime.status ne 'OPEN'
                                                            and showtime.status ne 'ACTIVE'}">

                                                    <button
                                                        type="button"
                                                        class="btn-book-showtime btn-showtime-disabled"
                                                        disabled>

                                                        Suất chiếu không khả dụng

                                                    </button>

                                                </c:when>

                                                <%-- Chưa đăng nhập --%>

                                                <c:when test="${empty sessionScope.user}">

                                                    <a
                                                        href="${pageContext.request.contextPath}/booking/create?showtimeId=${showtime.showtimeId}"
                                                        class="btn-book-showtime">

                                                        Đăng nhập để đặt vé

                                                    </a>

                                                </c:when>

                                                <%-- CUSTOMER --%>

                                                <c:when test="${sessionScope.user.role eq 'CUSTOMER'}">

                                                    <a
                                                        href="${pageContext.request.contextPath}/booking/create?showtimeId=${showtime.showtimeId}"
                                                        class="btn-book-showtime">

                                                        Đặt vé

                                                    </a>

                                                </c:when>

                                                <%-- ADMIN / STAFF --%>

                                                <c:otherwise>

                                                    <button
                                                        type="button"
                                                        class="btn-book-showtime btn-showtime-disabled"
                                                        disabled>

                                                        Chỉ dành cho khách hàng

                                                    </button>

                                                </c:otherwise>

                                            </c:choose>

                                        </article>

                                    </c:if>

                                </c:forEach>

                            </div>

                        </c:otherwise>

                    </c:choose>

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