<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<body>

<jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

<link
    rel="stylesheet"
    href="${pageContext.request.contextPath}/assets/css/home.css">

<!-- =====================================================
     HERO
     ===================================================== -->

<section class="hero-banner">

    <div class="hero-banner-bg">

        <object
            type="image/svg+xml"
            data="${pageContext.request.contextPath}/assets/images/banner-home.svg"
            class="hero-svg"
            aria-hidden="true">
        </object>

    </div>

    <div class="hero-overlay">

        <h1 class="hero-title">

            Phòng Chiếu Phim

            <span>CineStar</span>

        </h1>

        <p class="hero-subtitle">

            Đắm chìm trong vũ trụ điện ảnh —
            nơi mỗi suất chiếu là một hành trình.

        </p>

        <div class="hero-actions">

            <a
                href="#movie-content"
                class="btn btn-hero-primary">

                Xem phim

            </a>

            <a
                href="#now-showing"
                class="btn btn-hero-outline">

                Phim đang chiếu

            </a>

        </div>

    </div>

</section>

<!-- =====================================================
     TÌM KIẾM VÀ LỌC
     ===================================================== -->

<section class="search-section">

    <div class="container">

        <div class="movie-search-box">

            <form
                action="${pageContext.request.contextPath}/home"
                method="get">

                <div class="search-title">
                    Tìm kiếm và lọc phim
                </div>

                <div class="search-form-row">

                    <div class="search-input-wrap">

                        <span class="search-input-icon">
                            🔍
                        </span>

                        <input
                            type="text"
                            id="keyword"
                            name="keyword"
                            class="search-input"
                            maxlength="150"
                            value="<c:out value='${keyword}'/>"
                            placeholder="Nhập tên phim hoặc thể loại..."
                            autocomplete="off">

                    </div>

                    <div class="genre-select-wrap">

                        <select
                            id="genre"
                            name="genre"
                            class="genre-select">

                            <option value="">
                                Tất cả thể loại
                            </option>

                            <c:forEach
                                items="${genres}"
                                var="genreItem">

                                <option
                                    value="<c:out value='${genreItem}'/>"
                                    <c:if test="${selectedGenre eq genreItem}">
                                        selected
                                    </c:if>>

                                    <c:out value="${genreItem}"/>

                                </option>

                            </c:forEach>

                        </select>

                    </div>

                    <button
                        type="submit"
                        class="btn-search-movie">

                        Tìm kiếm

                    </button>

                    <c:if test="${filtering}">

                        <a
                            href="${pageContext.request.contextPath}/home"
                            class="btn-clear-search">

                            Xóa lọc

                        </a>

                    </c:if>

                </div>

            </form>

        </div>

    </div>

</section>

<main id="movie-content">

    <div class="container">

        <!-- =================================================
             THÔNG BÁO
             ================================================= -->

        <c:if test="${not empty errorMessage}">

            <div class="alert alert-danger alert-dismissible fade show mt-4">

                <c:out value="${errorMessage}"/>

                <button
                    type="button"
                    class="btn-close"
                    data-bs-dismiss="alert">
                </button>

            </div>

        </c:if>

        <c:if test="${not empty warningMessage}">

            <div class="alert alert-warning alert-dismissible fade show mt-4">

                <c:out value="${warningMessage}"/>

                <button
                    type="button"
                    class="btn-close"
                    data-bs-dismiss="alert">
                </button>

            </div>

        </c:if>

        <c:if test="${param.error eq 'invalid_movie_id'}">

            <div class="alert alert-warning alert-dismissible fade show mt-4">

                Mã phim không hợp lệ.

                <button
                    type="button"
                    class="btn-close"
                    data-bs-dismiss="alert">
                </button>

            </div>

        </c:if>

        <c:if test="${param.error eq 'movie_not_found'}">

            <div class="alert alert-warning alert-dismissible fade show mt-4">

                Không tìm thấy phim được yêu cầu.

                <button
                    type="button"
                    class="btn-close"
                    data-bs-dismiss="alert">
                </button>

            </div>

        </c:if>

        <c:if test="${param.error eq 'movie_unavailable'}">

            <div class="alert alert-warning alert-dismissible fade show mt-4">

                Phim này hiện không còn hoạt động.

                <button
                    type="button"
                    class="btn-close"
                    data-bs-dismiss="alert">
                </button>

            </div>

        </c:if>

        <c:if test="${param.error eq 'invalid_movie_data'}">

            <div class="alert alert-warning alert-dismissible fade show mt-4">

                Dữ liệu phim không hợp lệ hoặc chưa đầy đủ.

                <button
                    type="button"
                    class="btn-close"
                    data-bs-dismiss="alert">
                </button>

            </div>

        </c:if>

        <c:if test="${param.error eq 'server_error'}">

            <div class="alert alert-danger alert-dismissible fade show mt-4">

                Đã xảy ra lỗi hệ thống.
                Vui lòng thử lại sau.

                <button
                    type="button"
                    class="btn-close"
                    data-bs-dismiss="alert">
                </button>

            </div>

        </c:if>

        <!-- =================================================
             KẾT QUẢ TÌM KIẾM
             ================================================= -->

        <c:if test="${filtering}">

            <div class="search-result-box">

                <div>

                    Tìm thấy

                    <strong>
                        ${empty nowShowing
                            ? 0
                            : nowShowing.size()}
                    </strong>

                    phim phù hợp.

                    <c:if test="${not empty keyword}">

                        <span class="result-condition">

                            Từ khóa:

                            <strong>
                                "<c:out value="${keyword}"/>"
                            </strong>

                        </span>

                    </c:if>

                    <c:if test="${not empty selectedGenre}">

                        <span class="result-condition">

                            Thể loại:

                            <strong>
                                <c:out value="${selectedGenre}"/>
                            </strong>

                        </span>

                    </c:if>

                </div>

                <a
                    href="${pageContext.request.contextPath}/home"
                    class="search-result-reset">

                    Xem tất cả phim

                </a>

            </div>

        </c:if>

    </div>

    <!-- =================================================
         PHIM ĐANG CHIẾU / KẾT QUẢ TÌM KIẾM
         ================================================= -->

    <section
        id="now-showing"
        class="movie-section container">

        <div class="section-header">

            <div>

                <h2 class="section-title">

                    <c:choose>

                        <c:when test="${filtering}">
                            Kết quả tìm kiếm
                        </c:when>

                        <c:otherwise>
                            Phim đang chiếu
                        </c:otherwise>

                    </c:choose>

                </h2>

                <p class="section-description">

                    <c:choose>

                        <c:when test="${filtering}">

                            Có

                            <strong>
                                ${empty nowShowingCount
                                    ? 0
                                    : nowShowingCount}
                            </strong>

                            phim phù hợp.

                        </c:when>

                        <c:otherwise>

                            Hiện có

                            <strong>
                                ${empty nowShowingCount
                                    ? 0
                                    : nowShowingCount}
                            </strong>

                            phim đang chiếu.

                        </c:otherwise>

                    </c:choose>

                </p>

            </div>

        </div>

        <c:choose>

            <c:when test="${empty nowShowing}">

                <div class="empty-movie">

                    <div class="empty-movie-icon">
                        🎬
                    </div>

                    <c:choose>

                        <c:when test="${filtering}">

                            <h4>
                                Không tìm thấy phim
                            </h4>

                            <p>
                                Không có phim phù hợp với
                                điều kiện tìm kiếm đã chọn.
                            </p>

                            <a
                                href="${pageContext.request.contextPath}/home"
                                class="btn btn-hero-primary">

                                Xem tất cả phim

                            </a>

                        </c:when>

                        <c:otherwise>

                            <h4>
                                Chưa có phim đang chiếu
                            </h4>

                            <p>
                                Danh sách phim sẽ được cập nhật sớm.
                            </p>

                        </c:otherwise>

                    </c:choose>

                </div>

            </c:when>

            <c:otherwise>

                <div class="row g-4">

                    <c:forEach
                        items="${nowShowing}"
                        var="movie">

                        <c:if test="${not empty movie
                                    and movie.movieId > 0
                                    and not empty movie.title}">

                            <div class="col-6 col-md-4 col-lg-3">

                                <article class="movie-card">

                                    <div class="movie-poster-wrap">

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

                                            <span class="movie-genre-badge">

                                                <c:out value="${movie.genre}"/>

                                            </span>

                                        </c:if>

                                        <span class="movie-status-badge">
                                            Đang chiếu
                                        </span>

                                    </div>

                                    <div class="movie-card-body">

                                        <h3
                                            class="movie-title"
                                            title="<c:out value='${movie.title}'/>">

                                            <c:out value="${movie.title}"/>

                                        </h3>

                                        <p class="movie-meta">

                                            ⏱

                                            <c:choose>

                                                <c:when test="${movie.duration > 0}">

                                                    ${movie.duration} phút

                                                </c:when>

                                                <c:otherwise>
                                                    Chưa cập nhật
                                                </c:otherwise>

                                            </c:choose>

                                        </p>

                                        <c:if test="${not empty movie.releaseDate}">

                                            <p class="movie-meta">

                                                Khởi chiếu:

                                                <fmt:formatDate
                                                    value="${movie.releaseDate}"
                                                    pattern="dd/MM/yyyy"/>

                                            </p>

                                        </c:if>

                                        <div class="movie-card-actions">

                                            <a
                                                href="${pageContext.request.contextPath}/movies/detail?id=${movie.movieId}"
                                                class="btn-movie-detail">

                                                Chi tiết

                                            </a>

                                            <a
                                                href="${pageContext.request.contextPath}/showtimes?movieId=${movie.movieId}"
                                                class="btn-book-now">

                                                Lịch chiếu

                                            </a>

                                        </div>

                                    </div>

                                </article>

                            </div>

                        </c:if>

                    </c:forEach>

                </div>

            </c:otherwise>

        </c:choose>

    </section>

    <!-- =================================================
         PHIM SẮP CHIẾU
         ================================================= -->

    <c:if test="${not filtering}">

        <section
            id="coming-soon"
            class="movie-section container coming-soon-section">

            <div class="section-header">

                <div>

                    <h2 class="section-title">
                        Phim sắp chiếu
                    </h2>

                    <p class="section-description">

                        Có

                        <strong>
                            ${empty comingSoonCount
                                ? 0
                                : comingSoonCount}
                        </strong>

                        phim sắp ra mắt.

                    </p>

                </div>

            </div>

            <c:choose>

                <c:when test="${empty comingSoon}">

                    <div class="empty-movie">

                        <div class="empty-movie-icon">
                            📅
                        </div>

                        <h4>
                            Chưa có phim sắp chiếu
                        </h4>

                        <p>
                            Danh sách phim mới sẽ được cập nhật sớm.
                        </p>

                    </div>

                </c:when>

                <c:otherwise>

                    <div class="row g-4">

                        <c:forEach
                            items="${comingSoon}"
                            var="movie">

                            <c:if test="${not empty movie
                                        and movie.movieId > 0
                                        and not empty movie.title}">

                                <div class="col-6 col-md-4 col-lg-3">

                                    <article class="movie-card movie-card-soon">

                                        <div class="movie-poster-wrap">

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

                                                <span class="movie-genre-badge">

                                                    <c:out value="${movie.genre}"/>

                                                </span>

                                            </c:if>

                                            <span class="movie-status-badge badge-soon">
                                                Sắp chiếu
                                            </span>

                                        </div>

                                        <div class="movie-card-body">

                                            <h3
                                                class="movie-title"
                                                title="<c:out value='${movie.title}'/>">

                                                <c:out value="${movie.title}"/>

                                            </h3>

                                            <p class="movie-meta">

                                                ⏱

                                                <c:choose>

                                                    <c:when test="${movie.duration > 0}">

                                                        ${movie.duration} phút

                                                    </c:when>

                                                    <c:otherwise>
                                                        Chưa cập nhật
                                                    </c:otherwise>

                                                </c:choose>

                                            </p>

                                            <p class="movie-meta">

                                                Dự kiến:

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

                                            </p>

                                            <a
                                                href="${pageContext.request.contextPath}/movies/detail?id=${movie.movieId}"
                                                class="btn-soon-detail">

                                                Xem chi tiết

                                            </a>

                                        </div>

                                    </article>

                                </div>

                            </c:if>

                        </c:forEach>

                    </div>

                </c:otherwise>

            </c:choose>

        </section>

    </c:if>

</main>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>

<script
    src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js">
</script>

</body>

</html>