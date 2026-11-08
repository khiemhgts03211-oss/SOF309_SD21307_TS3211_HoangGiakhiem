<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<body>

<jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

<link rel="stylesheet"
      href="${pageContext.request.contextPath}/assets/css/admin.css">

<div class="d-flex">

    <jsp:include page="/WEB-INF/views/common/sidebar.jsp"/>

    <main class="flex-grow-1 p-4">

        <div class="card shadow">

            <!-- =========================================
                 HEADER
                 ========================================= -->
            <div class="card-header bg-dark text-white">

                <div class="d-flex
                            justify-content-between
                            align-items-center
                            flex-wrap
                            gap-2">

                    <h3 class="mb-0">
                        Quản Lí Phim
                    </h3>

                    <span class="badge bg-primary">

                        ${empty totalMovie ? 0 : totalMovie} phim

                    </span>

                </div>

            </div>

            <div class="card-body">

                <!-- =========================================
                     THÔNG BÁO
                     ========================================= -->

                <c:if test="${not empty errorMessage}">

                    <div class="alert alert-danger alert-dismissible fade show">

                        <c:out value="${errorMessage}"/>

                        <button type="button"
                                class="btn-close"
                                data-bs-dismiss="alert">
                        </button>

                    </div>

                </c:if>

                <c:if test="${not empty warningMessage}">

                    <div class="alert alert-warning alert-dismissible fade show">

                        <c:out value="${warningMessage}"/>

                        <button type="button"
                                class="btn-close"
                                data-bs-dismiss="alert">
                        </button>

                    </div>

                </c:if>

                <!-- =========================================
                     TÌM KIẾM NHANH
                     ========================================= -->

                <form method="get"
                      action="${pageContext.request.contextPath}/admin/movies"
                      class="row g-2 mb-3">

                    <div class="col-md-10">

                        <input type="text"
                               name="keyword"
                               class="form-control"
                               maxlength="150"
                               placeholder="Search title or genre..."
                               value="<c:out value='${keyword}'/>">

                    </div>

                    <div class="col-md-2">

                        <button type="submit"
                                class="btn btn-primary w-100">

                            Search

                        </button>

                    </div>

                </form>

                <!-- =========================================
                     NÚT THÊM VÀ LÀM MỚI
                     ========================================= -->

                <div class="d-flex flex-wrap gap-2 mb-3">

                    <a href="${pageContext.request.contextPath}/admin/movies/add"
                       class="btn btn-success">

                        Add Movie

                    </a>

                    <a href="${pageContext.request.contextPath}/admin/movies"
                       class="btn btn-outline-secondary">

                        Làm mới

                    </a>

                </div>

                <!-- =========================================
                     FILTER
                     ========================================= -->

                <form method="get"
                      action="${pageContext.request.contextPath}/admin/movies"
                      class="row g-2 mb-3">

                    <%-- Giữ từ khóa khi lọc --%>
                    <input type="hidden"
                           name="keyword"
                           value="<c:out value='${keyword}'/>">

                    <%-- Giữ sort khi lọc --%>
                    <input type="hidden"
                           name="sort"
                           value="<c:out value='${sort}'/>">

                    <input type="hidden"
                           name="order"
                           value="<c:out value='${order}'/>">

                    <div class="col-md-5">

                        <select name="genre"
                                class="form-select">

                            <option value="">
                                All Genre
                            </option>

                            <c:forEach items="${genres}"
                                       var="genreItem">

                                <option value="<c:out value='${genreItem}'/>"
                                    <c:if test="${selectedGenre eq genreItem}">
                                        selected
                                    </c:if>>

                                    <c:out value="${genreItem}"/>

                                </option>

                            </c:forEach>

                        </select>

                    </div>

                    <div class="col-md-5">

                        <select name="status"
                                class="form-select">

                            <option value="">
                                All Status
                            </option>

                            <option value="ACTIVE"
                                <c:if test="${selectedStatus eq 'ACTIVE'}">
                                    selected
                                </c:if>>

                                ACTIVE

                            </option>

                            <option value="INACTIVE"
                                <c:if test="${selectedStatus eq 'INACTIVE'}">
                                    selected
                                </c:if>>

                                INACTIVE

                            </option>

                        </select>

                    </div>

                    <div class="col-md-2">

                        <button type="submit"
                                class="btn btn-primary w-100">

                            Filter

                        </button>

                    </div>

                </form>

                <!-- =========================================
                     SORT
                     ========================================= -->

                <form method="get"
                      action="${pageContext.request.contextPath}/admin/movies"
                      class="row g-2 mb-4">

                    <%-- Giữ search và filter khi sort --%>
                    <input type="hidden"
                           name="keyword"
                           value="<c:out value='${keyword}'/>">

                    <input type="hidden"
                           name="genre"
                           value="<c:out value='${selectedGenre}'/>">

                    <input type="hidden"
                           name="status"
                           value="<c:out value='${selectedStatus}'/>">

                    <div class="col-md-5">

                        <select name="sort"
                                class="form-select"
                                required>

                            <option value="">
                                -- Sắp xếp theo --
                            </option>

                            <option value="movie_id"
                                <c:if test="${sort eq 'movie_id'}">
                                    selected
                                </c:if>>

                                ID

                            </option>

                            <option value="title"
                                <c:if test="${sort eq 'title'}">
                                    selected
                                </c:if>>

                                Tên phim

                            </option>

                            <option value="genre"
                                <c:if test="${sort eq 'genre'}">
                                    selected
                                </c:if>>

                                Thể loại

                            </option>

                            <option value="duration"
                                <c:if test="${sort eq 'duration'}">
                                    selected
                                </c:if>>

                                Thời lượng

                            </option>

                            <option value="release_date"
                                <c:if test="${sort eq 'release_date'}">
                                    selected
                                </c:if>>

                                Ngày phát hành

                            </option>

                            <option value="status"
                                <c:if test="${sort eq 'status'}">
                                    selected
                                </c:if>>

                                Trạng thái

                            </option>

                            <option value="created_at"
                                <c:if test="${sort eq 'created_at'}">
                                    selected
                                </c:if>>

                                Ngày tạo

                            </option>

                        </select>

                    </div>

                    <div class="col-md-5">

                        <select name="order"
                                class="form-select">

                            <option value="ASC"
                                <c:if test="${order eq 'ASC'}">
                                    selected
                                </c:if>>

                                Tăng dần

                            </option>

                            <option value="DESC"
                                <c:if test="${order eq 'DESC'}">
                                    selected
                                </c:if>>

                                Giảm dần

                            </option>

                        </select>

                    </div>

                    <div class="col-md-2">

                        <button type="submit"
                                class="btn btn-primary w-100">

                            Sort

                        </button>

                    </div>

                </form>

                <!-- =========================================
                     KẾT QUẢ ĐANG ÁP DỤNG
                     ========================================= -->

                <c:if test="${hasSearch or hasFilter or hasSort}">

                    <div class="alert alert-info">

                        <strong>Kết quả:</strong>

                        Tìm thấy

                        <strong>
                            ${empty totalMovie ? 0 : totalMovie}
                        </strong>

                        phim.

                        <c:if test="${not empty keyword}">

                            <span class="ms-2">

                                Từ khóa:

                                <strong>
                                    "<c:out value="${keyword}"/>"
                                </strong>

                            </span>

                        </c:if>

                        <c:if test="${not empty selectedGenre}">

                            <span class="ms-2">

                                Thể loại:

                                <strong>
                                    <c:out value="${selectedGenre}"/>
                                </strong>

                            </span>

                        </c:if>

                        <c:if test="${not empty selectedStatus}">

                            <span class="ms-2">

                                Trạng thái:

                                <strong>
                                    <c:out value="${selectedStatus}"/>
                                </strong>

                            </span>

                        </c:if>

                    </div>

                </c:if>

                <!-- =========================================
                     BẢNG PHIM
                     ========================================= -->

                <div class="table-responsive">

                    <table class="table table-bordered table-hover align-middle">

                        <thead class="table-dark">

                            <tr>

                                <th style="width: 90px;">
                                    Poster
                                </th>

                                <th>
                                    ID
                                </th>

                                <th>
                                    Title
                                </th>

                                <th>
                                    Genre
                                </th>

                                <th>
                                    Duration
                                </th>

                                <th>
                                    Release Date
                                </th>

                                <th>
                                    Status
                                </th>

                                <th>
                                    Created
                                </th>

                                <th style="min-width: 180px;">
                                    Action
                                </th>

                            </tr>

                        </thead>

                        <tbody>

                            <c:choose>

                                <c:when test="${empty movies}">

                                    <tr>

                                        <td colspan="9"
                                            class="text-center text-muted py-5">

                                            Không tìm thấy phim phù hợp.

                                        </td>

                                    </tr>

                                </c:when>

                                <c:otherwise>

                                    <c:forEach var="m"
                                               items="${movies}">

                                        <tr>

                                            <!-- POSTER -->
                                            <td>

                                                <c:choose>

                                                    <c:when test="${not empty m.poster}">

                                                        <img
                                                            src="${pageContext.request.contextPath}/${m.poster}"
                                                            alt="<c:out value='${m.title}'/>"
                                                            width="70"
                                                            height="95"
                                                            class="img-thumbnail"
                                                            style="object-fit: cover;"
                                                            onerror="this.onerror=null;
                                                                     this.src='${pageContext.request.contextPath}/assets/images/logo.png';">

                                                    </c:when>

                                                    <c:otherwise>

                                                        <span class="text-muted">
                                                            No Image
                                                        </span>

                                                    </c:otherwise>

                                                </c:choose>

                                            </td>

                                            <!-- ID -->
                                            <td>

                                                <c:out value="${m.movieId}"/>

                                            </td>

                                            <!-- TITLE -->
                                            <td>

                                                <c:choose>

                                                    <c:when test="${not empty m.title}">

                                                        <c:out value="${m.title}"/>

                                                    </c:when>

                                                    <c:otherwise>

                                                        <span class="text-muted">
                                                            Chưa cập nhật
                                                        </span>

                                                    </c:otherwise>

                                                </c:choose>

                                            </td>

                                            <!-- GENRE -->
                                            <td>

                                                <c:choose>

                                                    <c:when test="${not empty m.genre}">

                                                        <c:out value="${m.genre}"/>

                                                    </c:when>

                                                    <c:otherwise>

                                                        <span class="text-muted">
                                                            Chưa cập nhật
                                                        </span>

                                                    </c:otherwise>

                                                </c:choose>

                                            </td>

                                            <!-- DURATION -->
                                            <td>

                                                <c:choose>

                                                    <c:when test="${m.duration > 0}">

                                                        ${m.duration} phút

                                                    </c:when>

                                                    <c:otherwise>

                                                        <span class="text-muted">
                                                            Chưa cập nhật
                                                        </span>

                                                    </c:otherwise>

                                                </c:choose>

                                            </td>

                                            <!-- RELEASE DATE -->
                                            <td>

                                                <c:choose>

                                                    <c:when test="${not empty m.releaseDate}">

                                                        <fmt:formatDate
                                                            value="${m.releaseDate}"
                                                            pattern="dd/MM/yyyy"/>

                                                    </c:when>

                                                    <c:otherwise>

                                                        <span class="text-muted">
                                                            Chưa cập nhật
                                                        </span>

                                                    </c:otherwise>

                                                </c:choose>

                                            </td>

                                            <!-- STATUS -->
                                            <td>

                                                <c:choose>

                                                    <c:when test="${m.status eq 'ACTIVE'}">

                                                        <span class="badge bg-success">

                                                            ACTIVE

                                                        </span>

                                                    </c:when>

                                                    <c:when test="${m.status eq 'INACTIVE'}">

                                                        <span class="badge bg-danger">

                                                            INACTIVE

                                                        </span>

                                                    </c:when>

                                                    <c:otherwise>

                                                        <span class="badge bg-secondary">

                                                            <c:out value="${m.status}"/>

                                                        </span>

                                                    </c:otherwise>

                                                </c:choose>

                                            </td>

                                            <!-- CREATED -->
                                            <td>

                                                <c:choose>

                                                    <c:when test="${not empty m.createdAt}">

                                                        <fmt:formatDate
                                                            value="${m.createdAt}"
                                                            pattern="dd/MM/yyyy HH:mm:ss"/>

                                                    </c:when>

                                                    <c:otherwise>

                                                        <span class="text-muted">
                                                            Chưa cập nhật
                                                        </span>

                                                    </c:otherwise>

                                                </c:choose>

                                            </td>

                                            <!-- ACTION -->
                                            <td>

                                                <div class="d-flex
                                                            flex-wrap
                                                            gap-1">

                                                    <a
                                                        class="btn btn-info btn-sm"
                                                        href="${pageContext.request.contextPath}/admin/movies/detail?id=${m.movieId}">

                                                        Detail

                                                    </a>

                                                    <a
                                                        class="btn btn-warning btn-sm"
                                                        href="${pageContext.request.contextPath}/admin/movies/edit?id=${m.movieId}">

                                                        Edit

                                                    </a>

                                                    <a
                                                        class="btn btn-danger btn-sm"
                                                        href="${pageContext.request.contextPath}/admin/movies/delete?id=${m.movieId}"
                                                        onclick="return confirm('Bạn có chắc muốn xóa phim này?');">

                                                        Delete

                                                    </a>

                                                </div>

                                            </td>

                                        </tr>

                                    </c:forEach>

                                </c:otherwise>

                            </c:choose>

                        </tbody>

                    </table>

                </div>

                <!-- =========================================
                     PHÂN TRANG
                     ========================================= -->

                <c:if test="${totalPage > 1}">

                    <nav class="mt-3"
                         aria-label="Movie pagination">

                        <ul class="pagination justify-content-center flex-wrap">

                            <!-- Previous -->
                            <c:if test="${currentPage > 1}">

                                <c:url var="previousUrl"
                                       value="/admin/movies">

                                    <c:param name="page"
                                             value="${currentPage - 1}"/>

                                    <c:param name="keyword"
                                             value="${keyword}"/>

                                    <c:param name="genre"
                                             value="${selectedGenre}"/>

                                    <c:param name="status"
                                             value="${selectedStatus}"/>

                                    <c:param name="sort"
                                             value="${sort}"/>

                                    <c:param name="order"
                                             value="${order}"/>

                                </c:url>

                                <li class="page-item">

                                    <a class="page-link"
                                       href="${previousUrl}">

                                        Previous

                                    </a>

                                </li>

                            </c:if>

                            <!-- Page numbers -->
                            <c:forEach begin="1"
                                       end="${totalPage}"
                                       var="pageNumber">

                                <c:url var="pageUrl"
                                       value="/admin/movies">

                                    <c:param name="page"
                                             value="${pageNumber}"/>

                                    <c:param name="keyword"
                                             value="${keyword}"/>

                                    <c:param name="genre"
                                             value="${selectedGenre}"/>

                                    <c:param name="status"
                                             value="${selectedStatus}"/>

                                    <c:param name="sort"
                                             value="${sort}"/>

                                    <c:param name="order"
                                             value="${order}"/>

                                </c:url>

                                <li class="page-item
                                           ${pageNumber eq currentPage
                                             ? 'active'
                                             : ''}">

                                    <a class="page-link"
                                       href="${pageUrl}">

                                        ${pageNumber}

                                    </a>

                                </li>

                            </c:forEach>

                            <!-- Next -->
                            <c:if test="${currentPage < totalPage}">

                                <c:url var="nextUrl"
                                       value="/admin/movies">

                                    <c:param name="page"
                                             value="${currentPage + 1}"/>

                                    <c:param name="keyword"
                                             value="${keyword}"/>

                                    <c:param name="genre"
                                             value="${selectedGenre}"/>

                                    <c:param name="status"
                                             value="${selectedStatus}"/>

                                    <c:param name="sort"
                                             value="${sort}"/>

                                    <c:param name="order"
                                             value="${order}"/>

                                </c:url>

                                <li class="page-item">

                                    <a class="page-link"
                                       href="${nextUrl}">

                                        Next

                                    </a>

                                </li>

                            </c:if>

                        </ul>

                    </nav>

                </c:if>

            </div>

        </div>

    </main>

</div>

<script
    src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js">
</script>

</body>
</html>