<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<!DOCTYPE html>
<html lang="vi">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1">

    <title>Quản lý suất chiếu</title>

    <link
        href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
        rel="stylesheet">

    <style>

        body {
            background-color: #f4f6f9;
        }

        .page-title {
            font-weight: 700;
        }

        .showtime-card {
            overflow: hidden;
            border: none;
            border-radius: 12px;
        }

        .table th,
        .table td {
            vertical-align: middle;
        }

        .table th {
            white-space: nowrap;
        }

        .movie-title {
            font-weight: 600;
        }

        .room-name {
            color: #6f42c1;
            font-weight: 600;
        }

        .price-value {
            color: #198754;
            font-weight: 700;
            white-space: nowrap;
        }

        .time-value {
            white-space: nowrap;
        }

        .action-column {
            min-width: 210px;
            white-space: nowrap;
        }

        .empty-data {
            padding: 50px 15px !important;
        }

        .pagination {
            margin-bottom: 0;
        }

    </style>

</head>

<body>

<div class="container-fluid py-4 px-3 px-md-4">

    <!-- HEADER -->

    <div class="d-flex flex-wrap
                justify-content-between
                align-items-center
                gap-3 mb-4">

        <div>

            <h2 class="page-title mb-1">
                Quản lý suất chiếu
            </h2>

            <p class="text-muted mb-0">
                Danh sách, tìm kiếm và quản lý lịch chiếu phim
            </p>

        </div>

        <div class="d-flex flex-wrap gap-2">

            <a
                href="${pageContext.request.contextPath}/admin/showtimes/add"
                class="btn btn-success">

                Thêm suất chiếu

            </a>

            <a
                href="${pageContext.request.contextPath}/admin/dashboard"
                class="btn btn-outline-secondary">

                Quay lại Dashboard

            </a>

        </div>

    </div>

    <!-- THÔNG BÁO -->

    <c:if test="${not empty errorMessage}">

        <div class="alert alert-danger alert-dismissible fade show">

            <c:out value="${errorMessage}"/>

            <button
                type="button"
                class="btn-close"
                data-bs-dismiss="alert">
            </button>

        </div>

    </c:if>

    <c:if test="${not empty warningMessage}">

        <div class="alert alert-warning alert-dismissible fade show">

            <c:out value="${warningMessage}"/>

            <button
                type="button"
                class="btn-close"
                data-bs-dismiss="alert">
            </button>

        </div>

    </c:if>

    <c:if test="${param.success eq 'created'}">

        <div class="alert alert-success alert-dismissible fade show">

            Thêm suất chiếu thành công.

            <button
                type="button"
                class="btn-close"
                data-bs-dismiss="alert">
            </button>

        </div>

    </c:if>

    <c:if test="${param.success eq 'updated'}">

        <div class="alert alert-success alert-dismissible fade show">

            Cập nhật suất chiếu thành công.

            <button
                type="button"
                class="btn-close"
                data-bs-dismiss="alert">
            </button>

        </div>

    </c:if>

    <c:if test="${param.success eq 'deleted'}">

        <div class="alert alert-success alert-dismissible fade show">

            Xóa suất chiếu thành công.

            <button
                type="button"
                class="btn-close"
                data-bs-dismiss="alert">
            </button>

        </div>

    </c:if>

    <c:if test="${param.error eq 'invalid_id'}">

        <div class="alert alert-warning alert-dismissible fade show">

            Mã suất chiếu không hợp lệ.

            <button
                type="button"
                class="btn-close"
                data-bs-dismiss="alert">
            </button>

        </div>

    </c:if>

    <c:if test="${param.error eq 'not_found'}">

        <div class="alert alert-warning alert-dismissible fade show">

            Không tìm thấy suất chiếu.

            <button
                type="button"
                class="btn-close"
                data-bs-dismiss="alert">
            </button>

        </div>

    </c:if>

    <c:if test="${param.error eq 'delete_failed'}">

        <div class="alert alert-danger alert-dismissible fade show">

            Không thể xóa suất chiếu này.

            <button
                type="button"
                class="btn-close"
                data-bs-dismiss="alert">
            </button>

        </div>

    </c:if>

    <!-- SEARCH + FILTER + SORT -->

    <div class="card shadow-sm showtime-card mb-4">

        <div class="card-header bg-white py-3">

            <h5 class="mb-0">
                Tìm kiếm và lọc suất chiếu
            </h5>

        </div>

        <div class="card-body">

            <form
                action="${pageContext.request.contextPath}/admin/showtimes"
                method="get">

                <div class="row g-3 align-items-end">

                    <div class="col-lg-4 col-md-6">

                        <label
                            for="keyword"
                            class="form-label">

                            Từ khóa

                        </label>

                        <input
                            type="text"
                            id="keyword"
                            name="keyword"
                            class="form-control"
                            maxlength="150"
                            value="<c:out value='${keyword}'/>"
                            placeholder="Tên phim, phòng hoặc trạng thái...">

                    </div>

                    <div class="col-lg-2 col-md-3">

                        <label
                            for="status"
                            class="form-label">

                            Trạng thái

                        </label>

                        <select
                            id="status"
                            name="status"
                            class="form-select">

                            <option value="">
                                Tất cả trạng thái
                            </option>

                            <option
                                value="OPEN"
                                <c:if test="${selectedStatus eq 'OPEN'}">
                                    selected
                                </c:if>>

                                OPEN

                            </option>

                            <option
                                value="CLOSED"
                                <c:if test="${selectedStatus eq 'CLOSED'}">
                                    selected
                                </c:if>>

                                CLOSED

                            </option>

                            <option
                                value="CANCELLED"
                                <c:if test="${selectedStatus eq 'CANCELLED'}">
                                    selected
                                </c:if>>

                                CANCELLED

                            </option>

                        </select>

                    </div>

                    <div class="col-lg-3 col-md-3">

                        <label
                            for="sort"
                            class="form-label">

                            Sắp xếp

                        </label>

                        <select
                            id="sort"
                            name="sort"
                            class="form-select">

                            <option value="">
                                Mặc định
                            </option>

                            <option
                                value="date_asc"
                                <c:if test="${sort eq 'date_asc'}">
                                    selected
                                </c:if>>

                                Ngày tăng dần

                            </option>

                            <option
                                value="date_desc"
                                <c:if test="${sort eq 'date_desc'}">
                                    selected
                                </c:if>>

                                Ngày giảm dần

                            </option>

                            <option
                                value="price_asc"
                                <c:if test="${sort eq 'price_asc'}">
                                    selected
                                </c:if>>

                                Giá tăng dần

                            </option>

                            <option
                                value="price_desc"
                                <c:if test="${sort eq 'price_desc'}">
                                    selected
                                </c:if>>

                                Giá giảm dần

                            </option>

                            <option
                                value="movie_asc"
                                <c:if test="${sort eq 'movie_asc'}">
                                    selected
                                </c:if>>

                                Tên phim A-Z

                            </option>

                            <option
                                value="movie_desc"
                                <c:if test="${sort eq 'movie_desc'}">
                                    selected
                                </c:if>>

                                Tên phim Z-A

                            </option>

                            <option
                                value="room_asc"
                                <c:if test="${sort eq 'room_asc'}">
                                    selected
                                </c:if>>

                                Phòng A-Z

                            </option>

                            <option
                                value="room_desc"
                                <c:if test="${sort eq 'room_desc'}">
                                    selected
                                </c:if>>

                                Phòng Z-A

                            </option>

                        </select>

                    </div>

                    <div class="col-lg-3">

                        <div class="d-flex gap-2">

                            <button
                                type="submit"
                                class="btn btn-primary flex-grow-1">

                                Áp dụng

                            </button>

                            <a
                                href="${pageContext.request.contextPath}/admin/showtimes"
                                class="btn btn-outline-secondary">

                                Làm mới

                            </a>

                        </div>

                    </div>

                </div>

            </form>

        </div>

    </div>

    <!-- KẾT QUẢ -->

    <c:if test="${hasCriteria}">

        <div class="alert alert-info">

            Tìm thấy

            <strong>
                ${empty totalItems ? 0 : totalItems}
            </strong>

            suất chiếu phù hợp.

            <c:if test="${not empty keyword}">

                <span class="ms-2">

                    Từ khóa:

                    <strong>
                        "<c:out value="${keyword}"/>"
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

    <!-- DANH SÁCH -->

    <div class="card shadow-sm showtime-card">

        <div class="card-header bg-white py-3">

            <div class="d-flex flex-wrap
                        justify-content-between
                        align-items-center
                        gap-2">

                <h5 class="mb-0">
                    Danh sách suất chiếu
                </h5>

                <span class="badge bg-primary">

                    ${empty totalItems ? 0 : totalItems}
                    suất chiếu

                </span>

            </div>

        </div>

        <div class="card-body p-0">

            <div class="table-responsive">

                <table class="table
                              table-bordered
                              table-hover
                              align-middle
                              mb-0">

                    <thead class="table-dark">

                    <tr>

                        <th>ID</th>
                        <th>Phim</th>
                        <th>Phòng</th>
                        <th>Ngày chiếu</th>
                        <th>Giờ bắt đầu</th>
                        <th>Giờ kết thúc</th>
                        <th class="text-end">Giá vé</th>
                        <th>Trạng thái</th>
                        <th>Ngày tạo</th>
                        <th class="text-center">Thao tác</th>

                    </tr>

                    </thead>

                    <tbody>

                    <c:choose>

                        <c:when test="${empty showtimes}">

                            <tr>

                                <td
                                    colspan="10"
                                    class="text-center text-muted empty-data">

                                    <c:choose>

                                        <c:when test="${hasCriteria}">

                                            Không tìm thấy suất chiếu phù hợp.

                                        </c:when>

                                        <c:otherwise>

                                            Chưa có dữ liệu suất chiếu.

                                        </c:otherwise>

                                    </c:choose>

                                </td>

                            </tr>

                        </c:when>

                        <c:otherwise>

                            <c:forEach
                                items="${showtimes}"
                                var="showtime">

                                <tr>

                                    <td>
                                        <c:out value="${showtime.showtimeId}"/>
                                    </td>

                                    <td>

                                        <c:choose>

                                            <c:when test="${not empty showtime.movieTitle}">

                                                <span class="movie-title">

                                                    <c:out
                                                        value="${showtime.movieTitle}"/>

                                                </span>

                                            </c:when>

                                            <c:otherwise>

                                                Movie #${showtime.movieId}

                                            </c:otherwise>

                                        </c:choose>

                                    </td>

                                    <td>

                                        <c:choose>

                                            <c:when test="${not empty showtime.roomName}">

                                                <span class="room-name">

                                                    <c:out
                                                        value="${showtime.roomName}"/>

                                                </span>

                                            </c:when>

                                            <c:otherwise>

                                                Room #${showtime.roomId}

                                            </c:otherwise>

                                        </c:choose>

                                    </td>

                                    <td>

                                        <c:choose>

                                            <c:when test="${not empty showtime.showDate}">

                                                <fmt:formatDate
                                                    value="${showtime.showDate}"
                                                    pattern="dd/MM/yyyy"/>

                                            </c:when>

                                            <c:otherwise>
                                                Không xác định
                                            </c:otherwise>

                                        </c:choose>

                                    </td>

                                    <td class="time-value">

                                        <c:choose>

                                            <c:when test="${not empty showtime.startTime}">

                                                <fmt:formatDate
                                                    value="${showtime.startTime}"
                                                    pattern="HH:mm"/>

                                            </c:when>

                                            <c:otherwise>
                                                --
                                            </c:otherwise>

                                        </c:choose>

                                    </td>

                                    <td class="time-value">

                                        <c:choose>

                                            <c:when test="${not empty showtime.endTime}">

                                                <fmt:formatDate
                                                    value="${showtime.endTime}"
                                                    pattern="HH:mm"/>

                                            </c:when>

                                            <c:otherwise>
                                                --
                                            </c:otherwise>

                                        </c:choose>

                                    </td>

                                    <td class="text-end price-value">

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

                                    </td>

                                    <td>

                                        <c:choose>

                                            <c:when test="${showtime.status eq 'OPEN'}">

                                                <span class="badge bg-success">
                                                    OPEN
                                                </span>

                                            </c:when>

                                            <c:when test="${showtime.status eq 'CLOSED'}">

                                                <span class="badge bg-secondary">
                                                    CLOSED
                                                </span>

                                            </c:when>

                                            <c:when test="${showtime.status eq 'CANCELLED'}">

                                                <span class="badge bg-danger">
                                                    CANCELLED
                                                </span>

                                            </c:when>

                                            <c:otherwise>

                                                <span class="badge bg-dark">

                                                    <c:out
                                                        value="${showtime.status}"/>

                                                </span>

                                            </c:otherwise>

                                        </c:choose>

                                    </td>

                                    <td>

                                        <c:choose>

                                            <c:when test="${not empty showtime.createdAt}">

                                                <fmt:formatDate
                                                    value="${showtime.createdAt}"
                                                    pattern="dd/MM/yyyy HH:mm:ss"/>

                                            </c:when>

                                            <c:otherwise>
                                                Không xác định
                                            </c:otherwise>

                                        </c:choose>

                                    </td>

                                    <td class="text-center action-column">

                                        <a
                                            href="${pageContext.request.contextPath}/admin/showtimes/detail?id=${showtime.showtimeId}"
                                            class="btn btn-sm btn-outline-info">

                                            Chi tiết

                                        </a>

                                        <a
                                            href="${pageContext.request.contextPath}/admin/showtimes/edit?id=${showtime.showtimeId}"
                                            class="btn btn-sm btn-outline-warning">

                                            Sửa

                                        </a>

                                        <form
                                            action="${pageContext.request.contextPath}/admin/showtimes/delete"
                                            method="post"
                                            class="d-inline">

                                            <input
                                                type="hidden"
                                                name="id"
                                                value="${showtime.showtimeId}">

                                            <button
                                                type="submit"
                                                class="btn btn-sm btn-outline-danger"
                                                onclick="return confirm('Bạn có chắc muốn xóa suất chiếu này?');">

                                                Xóa

                                            </button>

                                        </form>

                                    </td>

                                </tr>

                            </c:forEach>

                        </c:otherwise>

                    </c:choose>

                    </tbody>

                </table>

            </div>

        </div>

        <!-- PHÂN TRANG -->

        <c:if test="${totalPages > 1}">

            <div class="card-footer bg-white py-3">

                <nav aria-label="Phân trang suất chiếu">

                    <ul class="pagination justify-content-center flex-wrap">

                        <c:url
                            var="previousUrl"
                            value="/admin/showtimes">

                            <c:param
                                name="page"
                                value="${currentPage - 1}"/>

                            <c:param
                                name="keyword"
                                value="${keyword}"/>

                            <c:param
                                name="status"
                                value="${selectedStatus}"/>

                            <c:param
                                name="sort"
                                value="${sort}"/>

                        </c:url>

                        <li class="page-item
                            ${currentPage <= 1
                                ? 'disabled'
                                : ''}">

                            <a
                                class="page-link"
                                href="${currentPage <= 1
                                      ? '#'
                                      : previousUrl}">

                                Trước

                            </a>

                        </li>

                        <c:forEach
                            begin="1"
                            end="${totalPages}"
                            var="pageNumber">

                            <c:url
                                var="pageUrl"
                                value="/admin/showtimes">

                                <c:param
                                    name="page"
                                    value="${pageNumber}"/>

                                <c:param
                                    name="keyword"
                                    value="${keyword}"/>

                                <c:param
                                    name="status"
                                    value="${selectedStatus}"/>

                                <c:param
                                    name="sort"
                                    value="${sort}"/>

                            </c:url>

                            <li class="page-item
                                ${pageNumber eq currentPage
                                    ? 'active'
                                    : ''}">

                                <a
                                    class="page-link"
                                    href="${pageUrl}">

                                    ${pageNumber}

                                </a>

                            </li>

                        </c:forEach>

                        <c:url
                            var="nextUrl"
                            value="/admin/showtimes">

                            <c:param
                                name="page"
                                value="${currentPage + 1}"/>

                            <c:param
                                name="keyword"
                                value="${keyword}"/>

                            <c:param
                                name="status"
                                value="${selectedStatus}"/>

                            <c:param
                                name="sort"
                                value="${sort}"/>

                        </c:url>

                        <li class="page-item
                            ${currentPage >= totalPages
                                ? 'disabled'
                                : ''}">

                            <a
                                class="page-link"
                                href="${currentPage >= totalPages
                                      ? '#'
                                      : nextUrl}">

                                Sau

                            </a>

                        </li>

                    </ul>

                </nav>

            </div>

        </c:if>

    </div>

</div>

<script
    src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js">
</script>

</body>
</html>