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

        <c:choose>

            <c:when test="${empty movie}">

                <div class="movie-detail-empty">

                    <div class="empty-icon">
                        🎬
                    </div>

                    <h3>
                        Không tìm thấy phim
                    </h3>

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

                <section class="movie-detail-card">

                    <div class="row g-0">

                        <%-- =====================================
                             POSTER
                             ===================================== --%>

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
                                            alt="Không có poster">

                                    </c:otherwise>

                                </c:choose>

                                <c:if test="${not empty movie.genre}">

                                    <span class="movie-detail-genre">

                                        <c:out value="${movie.genre}"/>

                                    </span>

                                </c:if>

                            </div>

                        </div>

                        <%-- =====================================
                             THÔNG TIN PHIM
                             ===================================== --%>

                        <div class="col-lg-8">

                            <div class="movie-detail-content">

                                <div class="movie-detail-heading">

                                    <div>

                                        <p class="movie-detail-label">
                                            Chi tiết phim
                                        </p>

                                        <h1 class="movie-detail-title">

                                            <c:out value="${movie.title}"/>

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

                                                <c:out value="${movie.status}"/>

                                            </span>

                                        </c:otherwise>

                                    </c:choose>

                                </div>

                                <%-- =================================
                                     THÔNG TIN NHANH
                                     ================================= --%>

                                <div class="movie-detail-meta">

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

                                </div>

                                <%-- =================================
                                     MÔ TẢ PHIM
                                     ================================= --%>

                                <div class="movie-detail-description">

                                    <h3>
                                        Nội dung phim
                                    </h3>

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

                                <%-- =================================
                                     THAO TÁC
                                     ================================= --%>

                                <div class="movie-detail-actions">

                                    <a
    href="${pageContext.request.contextPath}/showtimes?movieId=${movie.movieId}"
    class="btn-book-ticket">

    Xem suất chiếu

</a>

                                    <a
                                        href="${pageContext.request.contextPath}/home"
                                        class="btn-back-list">

                                        Quay lại danh sách

                                    </a>

                                </div>

                            </div>

                        </div>

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