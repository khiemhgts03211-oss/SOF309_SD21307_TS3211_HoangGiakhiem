<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<body>

<jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

<link
    rel="stylesheet"
    href="${pageContext.request.contextPath}/assets/css/movie-detail.css">

<main class="movie-detail-page">

    <div class="container">

        <%-- ==================================================
             THÔNG BÁO LỖI
             ================================================== --%>

        <c:if test="${not empty errorMessage}">

            <div class="alert alert-danger alert-dismissible fade show mb-4">

                <c:out value="${errorMessage}"/>

                <button
                    type="button"
                    class="btn-close"
                    data-bs-dismiss="alert"
                    aria-label="Đóng">
                </button>

            </div>

        </c:if>

        <c:choose>

            <%-- ==================================================
                 KHÔNG CÓ DỮ LIỆU PHIM
                 ================================================== --%>

            <c:when test="${empty movie}">

                <section class="movie-detail-empty">

                    <div class="empty-icon">
                        🎬
                    </div>

                    <h1>
                        Không tìm thấy phim
                    </h1>

                    <p>
                        Phim không tồn tại, không còn hoạt động
                        hoặc dữ liệu phim chưa đầy đủ.
                    </p>

                    <a
                        href="${pageContext.request.contextPath}/home"
                        class="btn-back-home">

                        Quay lại trang chủ

                    </a>

                </section>

            </c:when>

            <%-- ==================================================
                 CÓ DỮ LIỆU PHIM
                 ================================================== --%>

            <c:otherwise>

                <section class="movie-detail-card">

                    <div class="row g-0">

                        <%-- ==========================================
                             POSTER PHIM
                             ========================================== --%>

                        <div class="col-lg-4">

                            <div class="movie-detail-poster">

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
                                            alt="Không có poster"
                                            loading="lazy">

                                    </c:otherwise>

                                </c:choose>

                                <c:if test="${not empty movie.genre}">

                                    <span class="movie-detail-genre">

                                        <c:out value="${movie.genre}"/>

                                    </span>

                                </c:if>

                            </div>

                        </div>

                        <%-- ==========================================
                             THÔNG TIN PHIM
                             ========================================== --%>

                        <div class="col-lg-8">

                            <div class="movie-detail-content">

                                <div class="movie-detail-heading">

                                    <div>

                                        <p class="movie-detail-label">
                                            Chi tiết phim
                                        </p>

                                        <h1 class="movie-detail-title">

                                            <c:choose>

                                                <c:when test="${not empty movie.title}">

                                                    <c:out value="${movie.title}"/>

                                                </c:when>

                                                <c:otherwise>

                                                    Chưa cập nhật tên phim

                                                </c:otherwise>

                                            </c:choose>

                                        </h1>

                                    </div>

                                    <c:choose>

                                        <c:when test="${movie.status eq 'ACTIVE'}">

                                            <span class="movie-detail-status status-active">
                                                Đang hoạt động
                                            </span>

                                        </c:when>

                                        <c:otherwise>

                                            <span class="movie-detail-status status-inactive">

                                                <c:choose>

                                                    <c:when test="${not empty movie.status}">

                                                        <c:out value="${movie.status}"/>

                                                    </c:when>

                                                    <c:otherwise>

                                                        Không xác định

                                                    </c:otherwise>

                                                </c:choose>

                                            </span>

                                        </c:otherwise>

                                    </c:choose>

                                </div>

                                <%-- ==================================
                                     THÔNG TIN NHANH
                                     ================================== --%>

                                <div class="movie-detail-meta">

                                    <div class="movie-detail-meta-item">

                                        <span class="meta-name">
                                            Thể loại
                                        </span>

                                        <span class="meta-value">

                                            <c:choose>

                                                <c:when test="${not empty movie.genre}">

                                                    <c:out value="${movie.genre}"/>

                                                </c:when>

                                                <c:otherwise>

                                                    Chưa cập nhật

                                                </c:otherwise>

                                            </c:choose>

                                        </span>

                                    </div>

                                    <div class="movie-detail-meta-item">

                                        <span class="meta-name">
                                            Thời lượng
                                        </span>

                                        <span class="meta-value">

                                            <c:choose>

                                                <c:when test="${movie.duration > 0}">

                                                    ${movie.duration} phút

                                                </c:when>

                                                <c:otherwise>

                                                    Chưa cập nhật

                                                </c:otherwise>

                                            </c:choose>

                                        </span>

                                    </div>

                                    <div class="movie-detail-meta-item">

                                        <span class="meta-name">
                                            Ngày khởi chiếu
                                        </span>

                                        <span class="meta-value">

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

                                        </span>

                                    </div>

                                </div>

                                <%-- ==================================
                                     NỘI DUNG PHIM
                                     ================================== --%>

                                <div class="movie-detail-description">

                                    <h2>
                                        Nội dung phim
                                    </h2>

                                    <p>

                                        <c:choose>

                                            <c:when test="${not empty movie.description}">

                                                <c:out value="${movie.description}"/>

                                            </c:when>

                                            <c:otherwise>

                                                Nội dung phim đang được cập nhật.

                                            </c:otherwise>

                                        </c:choose>

                                    </p>

                                </div>

                                <%-- ==================================
                                     NÚT THAO TÁC
                                     ================================== --%>

                                <div class="movie-detail-actions">

                                    <c:choose>

                                        <c:when test="${movie.movieId > 0
                                                    and movie.status eq 'ACTIVE'}">

                                            <a
                                                href="${pageContext.request.contextPath}/showtimes?movieId=${movie.movieId}"
                                                class="btn-book-ticket">

                                                Xem lịch chiếu

                                            </a>

                                        </c:when>

                                        <c:otherwise>

                                            <button
                                                type="button"
                                                class="btn-book-ticket btn-disabled"
                                                disabled>

                                                Phim không khả dụng

                                            </button>

                                        </c:otherwise>

                                    </c:choose>

                                    <a
                                        href="${pageContext.request.contextPath}/home"
                                        class="btn-back-list">

                                        Quay lại danh sách phim

                                    </a>

                                </div>

                            </div>

                        </div>

                    </div>

                </section>

                <%-- ==================================================
                     KHỐI GỢI Ý XEM LỊCH CHIẾU
                     ================================================== --%>

                <c:if test="${movie.movieId > 0
                            and movie.status eq 'ACTIVE'}">

                    <section class="movie-detail-note">

                        <div>

                            <h3>
                                Bạn muốn đặt vé xem phim?
                            </h3>

                            <p>
                                Xem ngày chiếu, giờ chiếu, phòng chiếu
                                và giá vé trước khi tiến hành đặt vé.
                            </p>

                        </div>

                        <a
                            href="${pageContext.request.contextPath}/showtimes?movieId=${movie.movieId}"
                            class="btn-note-showtime">

                            Chọn lịch chiếu

                        </a>

                    </section>

                </c:if>

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