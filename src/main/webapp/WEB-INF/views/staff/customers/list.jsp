<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<c:set var="pageTitle" value="Danh sách khách hàng" scope="request"/>

<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<link rel="stylesheet"
      href="${pageContext.request.contextPath}/assets/css/admin.css">

    <style>

        .page-title {
            font-weight: 700;
        }

        .customer-card {
            border: none;
            border-radius: 12px;
            overflow: hidden;
        }

        .table th {
            white-space: nowrap;
            vertical-align: middle;
        }

        .table td {
            vertical-align: middle;
        }

        .customer-name {
            font-weight: 600;
        }

        .customer-email {
            color: #6c757d;
            font-size: 14px;
        }

        .empty-data {
            padding: 45px 15px !important;
        }

        .action-buttons {
            min-width: 235px;
            white-space: nowrap;
        }

    </style>

<body>

<jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

<div class="d-flex">

<jsp:include page="/WEB-INF/views/common/sidebar-staff.jsp"/>

<div class="flex-grow-1 p-4">

    <div class="d-flex flex-wrap
                justify-content-between
                align-items-center
                gap-3 mb-4">

        <div>

            <h2 class="page-title mb-1">
                Danh sách khách hàng
            </h2>

            <p class="text-muted mb-0">
                Tìm kiếm, lọc và quản lý tài khoản khách hàng
            </p>

        </div>

        <a
            href="${pageContext.request.contextPath}/staff/dashboard"
            class="btn btn-outline-secondary">

            Quay lại Dashboard

        </a>

    </div>

    <%-- Thông báo lỗi từ Servlet --%>

    <c:if test="${not empty errorMessage}">

        <div class="alert alert-danger alert-dismissible fade show">

            <c:out value="${errorMessage}"/>

            <button type="button"
                    class="btn-close"
                    data-bs-dismiss="alert">
            </button>

        </div>

    </c:if>

    <%-- Thông báo thành công --%>

    <c:if test="${param.success eq 'updated'}">

        <div class="alert alert-success alert-dismissible fade show">

            Cập nhật thông tin khách hàng thành công.

            <button type="button"
                    class="btn-close"
                    data-bs-dismiss="alert">
            </button>

        </div>

    </c:if>

    <c:if test="${param.success eq 'locked'}">

        <div class="alert alert-success alert-dismissible fade show">

            Khóa tài khoản khách hàng thành công.

            <button type="button"
                    class="btn-close"
                    data-bs-dismiss="alert">
            </button>

        </div>

    </c:if>

    <c:if test="${param.success eq 'unlocked'}">

        <div class="alert alert-success alert-dismissible fade show">

            Mở khóa tài khoản khách hàng thành công.

            <button type="button"
                    class="btn-close"
                    data-bs-dismiss="alert">
            </button>

        </div>

    </c:if>

    <%-- Thông báo lỗi thao tác --%>

    <c:if test="${param.error eq 'invalid_id'}">

        <div class="alert alert-warning alert-dismissible fade show">

            Mã khách hàng không hợp lệ.

            <button type="button"
                    class="btn-close"
                    data-bs-dismiss="alert">
            </button>

        </div>

    </c:if>

    <c:if test="${param.error eq 'not_found'}">

        <div class="alert alert-warning alert-dismissible fade show">

            Không tìm thấy tài khoản khách hàng.

            <button type="button"
                    class="btn-close"
                    data-bs-dismiss="alert">
            </button>

        </div>

    </c:if>

    <c:if test="${param.error eq 'not_customer'}">

        <div class="alert alert-danger alert-dismissible fade show">

            Không được phép thao tác với tài khoản STAFF hoặc ADMIN.

            <button type="button"
                    class="btn-close"
                    data-bs-dismiss="alert">
            </button>

        </div>

    </c:if>

    <c:if test="${param.error eq 'invalid_status'}">

        <div class="alert alert-warning alert-dismissible fade show">

            Trạng thái tài khoản không hợp lệ.

            <button type="button"
                    class="btn-close"
                    data-bs-dismiss="alert">
            </button>

        </div>

    </c:if>

    <c:if test="${param.error eq 'already_locked'}">

        <div class="alert alert-info alert-dismissible fade show">

            Tài khoản này đã bị khóa trước đó.

            <button type="button"
                    class="btn-close"
                    data-bs-dismiss="alert">
            </button>

        </div>

    </c:if>

    <c:if test="${param.error eq 'already_active'}">

        <div class="alert alert-info alert-dismissible fade show">

            Tài khoản này đang hoạt động.

            <button type="button"
                    class="btn-close"
                    data-bs-dismiss="alert">
            </button>

        </div>

    </c:if>

    <c:if test="${param.error eq 'cannot_lock'}">

        <div class="alert alert-warning alert-dismissible fade show">

            Chỉ có thể khóa tài khoản đang hoạt động.

            <button type="button"
                    class="btn-close"
                    data-bs-dismiss="alert">
            </button>

        </div>

    </c:if>

    <c:if test="${param.error eq 'cannot_unlock'}">

        <div class="alert alert-warning alert-dismissible fade show">

            Chỉ có thể mở khóa tài khoản LOCKED hoặc INACTIVE.

            <button type="button"
                    class="btn-close"
                    data-bs-dismiss="alert">
            </button>

        </div>

    </c:if>

    <c:if test="${param.error eq 'update_failed'}">

        <div class="alert alert-danger alert-dismissible fade show">

            Cập nhật trạng thái tài khoản thất bại.

            <button type="button"
                    class="btn-close"
                    data-bs-dismiss="alert">
            </button>

        </div>

    </c:if>

    <c:if test="${param.error eq 'server_error'}">

        <div class="alert alert-danger alert-dismissible fade show">

            Đã xảy ra lỗi hệ thống. Vui lòng thử lại.

            <button type="button"
                    class="btn-close"
                    data-bs-dismiss="alert">
            </button>

        </div>

    </c:if>

    <%-- Tìm kiếm và lọc --%>

    <div class="card shadow-sm customer-card mb-4">

        <div class="card-header bg-white py-3">

            <h5 class="mb-0">
                Tìm kiếm và lọc khách hàng
            </h5>

        </div>

        <div class="card-body">

            <form
                action="${pageContext.request.contextPath}/staff/customers"
                method="get">

                <div class="row g-3 align-items-end">

                    <div class="col-lg-6">

                        <label for="keyword"
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
                            placeholder="Username, họ tên, email, số điện thoại...">

                    </div>

                    <div class="col-lg-3">

                        <label for="status"
                               class="form-label">

                            Trạng thái

                        </label>

                        <select id="status"
                                name="status"
                                class="form-select">

                            <option value="">
                                Tất cả trạng thái
                            </option>

                            <option value="ACTIVE"
                                ${selectedStatus eq 'ACTIVE'
                                    ? 'selected'
                                    : ''}>
                                ACTIVE
                            </option>

                            <option value="INACTIVE"
                                ${selectedStatus eq 'INACTIVE'
                                    ? 'selected'
                                    : ''}>
                                INACTIVE
                            </option>

                            <option value="LOCKED"
                                ${selectedStatus eq 'LOCKED'
                                    ? 'selected'
                                    : ''}>
                                LOCKED
                            </option>

                        </select>

                    </div>

                    <div class="col-lg-3">

                        <div class="d-flex gap-2">

                            <button
                                type="submit"
                                class="btn btn-primary flex-grow-1">

                                Tìm kiếm

                            </button>

                            <a
                                href="${pageContext.request.contextPath}/staff/customers"
                                class="btn btn-outline-secondary">

                                Làm mới

                            </a>

                        </div>

                    </div>

                </div>

            </form>

        </div>

    </div>

    <c:if test="${not empty keyword
                or not empty selectedStatus}">

        <div class="alert alert-info">

            Tìm thấy

            <strong>
                ${empty customers ? 0 : customers.size()}
            </strong>

            khách hàng phù hợp.

        </div>

    </c:if>

    <%-- Danh sách khách hàng --%>

    <div class="card shadow-sm customer-card">

        <div class="card-header bg-white py-3">

            <div class="d-flex
                        justify-content-between
                        align-items-center">

                <h5 class="mb-0">
                    Danh sách tài khoản khách hàng
                </h5>

                <span class="badge bg-primary">

                    ${empty customers ? 0 : customers.size()}
                    khách hàng

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
                        <th>Username</th>
                        <th>Họ và tên</th>
                        <th>Email</th>
                        <th>Số điện thoại</th>
                        <th>Vai trò</th>
                        <th>Trạng thái</th>
                        <th>Ngày tạo</th>
                        <th class="text-center">Thao tác</th>

                    </tr>

                    </thead>

                    <tbody>

                    <c:choose>

                        <c:when test="${empty customers}">

                            <tr>

                                <td colspan="9"
                                    class="text-center
                                           text-muted
                                           empty-data">

                                    Không tìm thấy khách hàng phù hợp.

                                </td>

                            </tr>

                        </c:when>

                        <c:otherwise>

                            <c:forEach
                                items="${customers}"
                                var="customer">

                                <tr>

                                    <td>
                                        ${customer.userId}
                                    </td>

                                    <td>

                                        <strong>
                                            <c:out value="${customer.username}"/>
                                        </strong>

                                    </td>

                                    <td>

                                        <c:choose>

                                            <c:when test="${not empty customer.fullName}">

                                                <span class="customer-name">
                                                    <c:out value="${customer.fullName}"/>
                                                </span>

                                            </c:when>

                                            <c:otherwise>

                                                <span class="text-muted">
                                                    Chưa cập nhật
                                                </span>

                                            </c:otherwise>

                                        </c:choose>

                                    </td>

                                    <td>

                                        <c:choose>

                                            <c:when test="${not empty customer.email}">

                                                <span class="customer-email">
                                                    <c:out value="${customer.email}"/>
                                                </span>

                                            </c:when>

                                            <c:otherwise>

                                                <span class="text-muted">
                                                    Chưa cập nhật
                                                </span>

                                            </c:otherwise>

                                        </c:choose>

                                    </td>

                                    <td>

                                        <c:choose>

                                            <c:when test="${not empty customer.phone}">

                                                <c:out value="${customer.phone}"/>

                                            </c:when>

                                            <c:otherwise>

                                                <span class="text-muted">
                                                    Chưa cập nhật
                                                </span>

                                            </c:otherwise>

                                        </c:choose>

                                    </td>

                                    <td>

                                        <span class="badge bg-secondary">

                                            <c:out value="${customer.role}"/>

                                        </span>

                                    </td>

                                    <td>

                                        <c:choose>

                                            <c:when test="${customer.status eq 'ACTIVE'}">

                                                <span class="badge bg-success">
                                                    ACTIVE
                                                </span>

                                            </c:when>

                                            <c:when test="${customer.status eq 'INACTIVE'}">

                                                <span class="badge bg-warning text-dark">
                                                    INACTIVE
                                                </span>

                                            </c:when>

                                            <c:when test="${customer.status eq 'LOCKED'}">

                                                <span class="badge bg-danger">
                                                    LOCKED
                                                </span>

                                            </c:when>

                                            <c:otherwise>

                                                <span class="badge bg-secondary">
                                                    <c:out value="${customer.status}"/>
                                                </span>

                                            </c:otherwise>

                                        </c:choose>

                                    </td>

                                    <td>

                                        <c:choose>

                                            <c:when test="${not empty customer.createdAt}">

                                                <fmt:formatDate
                                                    value="${customer.createdAt}"
                                                    pattern="dd/MM/yyyy HH:mm"/>

                                            </c:when>

                                            <c:otherwise>

                                                <span class="text-muted">
                                                    Không xác định
                                                </span>

                                            </c:otherwise>

                                        </c:choose>

                                    </td>

                                    <td class="text-center action-buttons">

                                        <a
                                            href="${pageContext.request.contextPath}/staff/customers/detail?id=${customer.userId}"
                                            class="btn btn-sm btn-outline-primary">

                                            Chi tiết

                                        </a>

                                        <a
                                            href="${pageContext.request.contextPath}/staff/customers/edit?id=${customer.userId}"
                                            class="btn btn-sm btn-outline-warning">

                                            Sửa

                                        </a>

                                        <c:if test="${customer.status eq 'ACTIVE'}">

                                            <form
                                                action="${pageContext.request.contextPath}/staff/customers/status"
                                                method="post"
                                                class="d-inline">

                                                <input type="hidden"
                                                       name="userId"
                                                       value="${customer.userId}">

                                                <input type="hidden"
                                                       name="status"
                                                       value="LOCKED">

                                                <input type="hidden"
                                                       name="returnPage"
                                                       value="list">

                                                <input type="hidden"
                                                       name="keyword"
                                                       value="<c:out value='${keyword}'/>">

                                                <input type="hidden"
                                                       name="selectedStatus"
                                                       value="<c:out value='${selectedStatus}'/>">

                                                <button
                                                    type="submit"
                                                    class="btn btn-sm btn-outline-danger"
                                                    onclick="return confirm('Bạn có chắc muốn khóa tài khoản này?');">

                                                    Khóa

                                                </button>

                                            </form>

                                        </c:if>

                                        <c:if test="${customer.status eq 'LOCKED'
                                                    or customer.status eq 'INACTIVE'}">

                                            <form
                                                action="${pageContext.request.contextPath}/staff/customers/status"
                                                method="post"
                                                class="d-inline">

                                                <input type="hidden"
                                                       name="userId"
                                                       value="${customer.userId}">

                                                <input type="hidden"
                                                       name="status"
                                                       value="ACTIVE">

                                                <input type="hidden"
                                                       name="returnPage"
                                                       value="list">

                                                <input type="hidden"
                                                       name="keyword"
                                                       value="<c:out value='${keyword}'/>">

                                                <input type="hidden"
                                                       name="selectedStatus"
                                                       value="<c:out value='${selectedStatus}'/>">

                                                <button
                                                    type="submit"
                                                    class="btn btn-sm btn-outline-success"
                                                    onclick="return confirm('Bạn có chắc muốn mở khóa tài khoản này?');">

                                                    Mở khóa

                                                </button>

                                            </form>

                                        </c:if>

                                    </td>

                                </tr>

                            </c:forEach>

                        </c:otherwise>

                    </c:choose>

                    </tbody>

                </table>

            </div>

        </div>

    </div>

</div>

</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>

</body>
</html>