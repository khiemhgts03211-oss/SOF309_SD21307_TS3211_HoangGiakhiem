<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<body>

<jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

<link
    rel="stylesheet"
    href="${pageContext.request.contextPath}/assets/css/movie-showtimes.css">

<main class="showtime-page">

    <div class="container">

        <c:choose>

            <c:when test="${empty movie}">

                <div class="showtime-empty">

                    <h3>Không tìm thấy phim</h3>

                    <p>
                        Phim không tồn tại hoặc đã ngừng hoạt động.
                    </p>

                    <a
                        href="${pageContext.request.contextPath}/home"
                        class="btn-back-home">

                        Quay lại trang chủ

                    </a>

                </div>

            </c:when>

            <c:otherwise>

                <section class="movie-summary">

                    <div class="movie-summary-poster">

                        <c:choose>

                            <c:when test="${not empty movie.poster}">

                                <img
                                    src="${pageContext.request.contextPath}/${movie.poster}"
                                    alt="<c:out value='${movie.title}'/>"
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

                    <div class="movie-summary-content">

                        <p class="summary-label">
                            Lịch chiếu phim
                        </p>

                        <h1 class="summary-title">

                            <c:out value="${movie.title}"/>

                        </h1>

                        <div class="summary-meta">

                            <c:if test="${not empty movie.genre}">

                                <span>
                                    <strong>Thể loại:</strong>
                                    <c:out value="${movie.genre}"/>
                                </span>

                            </c:if>

                            <c:if test="${movie.duration > 0}">

                                <span>
                                    <strong>Thời lượng:</strong>
                                    ${movie.duration} phút
                                </span>

                            </c:if>

                            <c:if test="${not empty movie.releaseDate}">

                                <span>

                                    <strong>Khởi chiếu:</strong>

                                    <fmt:formatDate
                                        value="${movie.releaseDate}"
                                        pattern="dd/MM/yyyy"/>

                                </span>

                            </c:if>

                        </div>

                        <div class="summary-actions">

                            <a
                                href="${pageContext.request.contextPath}/movies/detail?id=${movie.movieId}"
                                class="btn-back-detail">

                                Chi tiết phim

                            </a>

                            <a
                                href="${pageContext.request.contextPath}/home"
                                class="btn-back-list">

                                Danh sách phim

                            </a>

                        </div>

                    </div>

                </section>

                <section class="showtime-section">

                    <div class="showtime-heading">

                        <div>

                            <h2>Chọn suất chiếu</h2>

                            <p>

                                Có

                                <strong>
                                    ${empty showtimeCount
                                        ? 0
                                        : showtimeCount}
                                </strong>

                                suất chiếu còn khả dụng.

                            </p>

                        </div>

                    </div>

                    <c:choose>

                        <c:when test="${empty showtimesByDate}">

                            <div class="showtime-empty">

                                <div class="empty-icon">
                                    🎬
                                </div>

                                <h3>
                                    Chưa có suất chiếu
                                </h3>

                                <p>
                                    Hiện chưa có suất chiếu khả dụng
                                    cho bộ phim này.
                                </p>

                                <a
                                    href="${pageContext.request.contextPath}/home"
                                    class="btn-back-home">

                                    Xem phim khác

                                </a>

                            </div>

                        </c:when>

                        <c:otherwise>

                            <c:forEach
                                items="${showtimesByDate}"
                                var="dateEntry">

                                <div class="showtime-date-card">

                                    <div class="showtime-date-header">

                                        <div>

                                            <span class="date-label">
                                                Ngày chiếu
                                            </span>

                                            <h3>

                                                <fmt:formatDate
                                                    value="${dateEntry.key}"
                                                    pattern="dd/MM/yyyy"/>

                                            </h3>

                                        </div>

                                    </div>

                                    <div class="showtime-grid">

                                        <c:forEach
                                            items="${dateEntry.value}"
                                            var="showtime">

                                            <article class="showtime-item">

                                                <div class="showtime-time">

                                                    <span class="start-time">

                                                        <fmt:formatDate
                                                            value="${showtime.startTime}"
                                                            pattern="HH:mm"/>

                                                    </span>

                                                    <span class="time-separator">
                                                        -
                                                    </span>

                                                    <span class="end-time">

                                                        <fmt:formatDate
                                                            value="${showtime.endTime}"
                                                            pattern="HH:mm"/>

                                                    </span>

                                                </div>

                                                <div class="showtime-info">

                                                    <span>

                                                        Phòng:

                                                        <strong>

                                                            <c:choose>

                                                                <c:when test="${not empty showtime.roomName}">

                                                                    <c:out value="${showtime.roomName}"/>

                                                                </c:when>

                                                                <c:otherwise>

                                                                    #${showtime.roomId}

                                                                </c:otherwise>

                                                            </c:choose>

                                                        </strong>

                                                    </span>

                                                    <c:if test="${not empty showtime.ticketPrice}">

                                                        <span>

                                                            Giá:

                                                            <strong class="showtime-price">

                                                                <fmt:formatNumber
                                                                    value="${showtime.ticketPrice}"
                                                                    type="number"
                                                                    groupingUsed="true"/>

                                                                VNĐ

                                                            </strong>

                                                        </span>

                                                    </c:if>

                                                </div>

                                                <a
                                                    href="${pageContext.request.contextPath}/booking/create?showtimeId=${showtime.showtimeId}"
                                                    class="btn-select-showtime">

                                                    Chọn suất chiếu

                                                </a>

                                            </article>

                                        </c:forEach>

                                    </div>

                                </div>

                            </c:forEach>

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