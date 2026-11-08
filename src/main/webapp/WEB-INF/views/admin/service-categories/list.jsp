<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<body>

<jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

<link
    rel="stylesheet"
    href="${pageContext.request.contextPath}/assets/css/admin.css">

<div class="d-flex">

    <jsp:include page="/WEB-INF/views/common/sidebar.jsp"/>

    <main class="flex-grow-1 p-4">

        <div class="card shadow">

            <div class="card-header bg-dark text-white">

                <div class="d-flex
                            justify-content-between
                            align-items-center
                            flex-wrap
                            gap-2">

                    <h3 class="mb-0">
                        Hóa Đơn Đồ Ăn - Thức Uống
                    </h3>

                    <a
                        href="${pageContext.request.contextPath}/admin/service-categories/add"
                        class="btn btn-success">

                        <i class="bi bi-plus-circle"></i>
                        Add Category

                    </a>

                </div>

            </div>

            <div class="card-body">

                <%-- THÔNG BÁO --%>

                <c:if test="${not empty errorMessage}">

                    <div class="alert alert-danger">

                        <c:out value="${errorMessage}"/>

                    </div>

                </c:if>

                <c:if test="${not empty warningMessage}">

                    <div class="alert alert-warning">

                        <c:out value="${warningMessage}"/>

                    </div>

                </c:if>

                <%-- SEARCH --%>

                <form
                    method="get"
                    action="${pageContext.request.contextPath}/admin/service-categories"
                    class="row g-2 mb-3">

                    <div class="col-md-10">

                        <input
                            type="text"
                            name="keyword"
                            class="form-control"
                            maxlength="100"
                            placeholder="Search category..."
                            value="<c:out value='${keyword}'/>">

                    </div>

                    <div class="col-md-2">

                        <button
                            type="submit"
                            class="btn btn-primary w-100">

                            Search

                        </button>

                    </div>

                </form>

                <%-- FILTER --%>

                <form
                    method="get"
                    action="${pageContext.request.contextPath}/admin/service-categories"
                    class="row g-2 mb-3">

                    <input
                        type="hidden"
                        name="keyword"
                        value="<c:out value='${keyword}'/>">

                    <input
                        type="hidden"
                        name="sort"
                        value="<c:out value='${sort}'/>">

                    <div class="col-md-5">

                        <select
                            name="status"
                            class="form-select">

                            <option value="">
                                All Status
                            </option>

                            <option
                                value="ACTIVE"
                                <c:if test="${selectedStatus eq 'ACTIVE'}">
                                    selected
                                </c:if>>

                                ACTIVE

                            </option>

                            <option
                                value="INACTIVE"
                                <c:if test="${selectedStatus eq 'INACTIVE'}">
                                    selected
                                </c:if>>

                                INACTIVE

                            </option>

                        </select>

                    </div>

                    <div class="col-md-2">

                        <button
                            type="submit"
                            class="btn btn-success w-100">

                            Filter

                        </button>

                    </div>

                </form>

                <%-- SORT --%>

                <form
                    method="get"
                    action="${pageContext.request.contextPath}/admin/service-categories"
                    class="row g-2 mb-3">

                    <input
                        type="hidden"
                        name="keyword"
                        value="<c:out value='${keyword}'/>">

                    <input
                        type="hidden"
                        name="status"
                        value="<c:out value='${selectedStatus}'/>">

                    <div class="col-md-5">

                        <select
                            name="sort"
                            class="form-select"
                            required>

                            <option value="">
                                -- Sắp xếp theo --
                            </option>

                            <option
                                value="idAsc"
                                <c:if test="${sort eq 'idAsc'}">
                                    selected
                                </c:if>>

                                ID tăng dần

                            </option>

                            <option
                                value="idDesc"
                                <c:if test="${sort eq 'idDesc'}">
                                    selected
                                </c:if>>

                                ID giảm dần

                            </option>

                            <option
                                value="nameAsc"
                                <c:if test="${sort eq 'nameAsc'}">
                                    selected
                                </c:if>>

                                Tên A → Z

                            </option>

                            <option
                                value="nameDesc"
                                <c:if test="${sort eq 'nameDesc'}">
                                    selected
                                </c:if>>

                                Tên Z → A

                            </option>

                            <option
                                value="createdAsc"
                                <c:if test="${sort eq 'createdAsc'}">
                                    selected
                                </c:if>>

                                Ngày tạo tăng dần

                            </option>

                            <option
                                value="createdDesc"
                                <c:if test="${sort eq 'createdDesc'}">
                                    selected
                                </c:if>>

                                Ngày tạo giảm dần

                            </option>

                        </select>

                    </div>

                    <div class="col-md-2">

                        <button
                            type="submit"
                            class="btn btn-warning w-100">

                            Sort

                        </button>

                    </div>

                    <div class="col-md-2">

                        <a
                            href="${pageContext.request.contextPath}/admin/service-categories"
                            class="btn btn-outline-secondary w-100">

                            Làm mới

                        </a>

                    </div>

                </form>

                <%-- KẾT QUẢ --%>

                <c:if test="${hasCriteria}">

                    <div class="alert alert-info">

                        Tìm thấy

                        <strong>
                            ${empty totalItems ? 0 : totalItems}
                        </strong>

                        nhóm dịch vụ phù hợp.

                    </div>

                </c:if>

                <%-- TABLE --%>

                <div class="table-responsive">

                    <table class="table table-bordered table-hover align-middle">

                        <thead class="table-dark">

                            <tr>

                                <th>ID</th>
                                <th>Name</th>
                                <th>Status</th>
                                <th>Created</th>
                                <th style="width: 220px;">
                                    Action
                                </th>

                            </tr>

                        </thead>

                        <tbody>

                            <c:choose>

                                <c:when test="${empty categories}">

                                    <tr>

                                        <td
                                            colspan="5"
                                            class="text-center text-muted py-5">

                                            Không tìm thấy nhóm dịch vụ phù hợp.

                                        </td>

                                    </tr>

                                </c:when>

                                <c:otherwise>

                                    <c:forEach
                                        var="cat"
                                        items="${categories}">

                                        <tr>

                                            <td>
                                                <c:out value="${cat.categoryId}"/>
                                            </td>

                                            <td>
                                                <c:out value="${cat.categoryName}"/>
                                            </td>

                                            <td>

                                                <c:choose>

                                                    <c:when test="${cat.status eq 'ACTIVE'}">

                                                        <span class="badge bg-success">
                                                            ACTIVE
                                                        </span>

                                                    </c:when>

                                                    <c:otherwise>

                                                        <span class="badge bg-danger">
                                                            INACTIVE
                                                        </span>

                                                    </c:otherwise>

                                                </c:choose>

                                            </td>

                                            <td>

                                                <c:choose>

                                                    <c:when test="${not empty cat.createdAt}">

                                                        <fmt:formatDate
                                                            value="${cat.createdAt}"
                                                            pattern="dd/MM/yyyy HH:mm:ss"/>

                                                    </c:when>

                                                    <c:otherwise>
                                                        Chưa cập nhật
                                                    </c:otherwise>

                                                </c:choose>

                                            </td>

                                            <td>

                                                <a
                                                    class="btn btn-info btn-sm"
                                                    href="${pageContext.request.contextPath}/admin/service-categories/detail?id=${cat.categoryId}">

                                                    Detail

                                                </a>

                                                <a
                                                    class="btn btn-warning btn-sm"
                                                    href="${pageContext.request.contextPath}/admin/service-categories/edit?id=${cat.categoryId}">

                                                    Edit

                                                </a>

                                                <a
                                                    class="btn btn-danger btn-sm"
                                                    href="${pageContext.request.contextPath}/admin/service-categories/delete?id=${cat.categoryId}"
                                                    onclick="return confirm('Delete category?');">

                                                    Delete

                                                </a>

                                            </td>

                                        </tr>

                                    </c:forEach>

                                </c:otherwise>

                            </c:choose>

                        </tbody>

                    </table>

                </div>

                <%-- PAGINATION --%>

                <c:if test="${totalPages > 1}">

                    <nav>

                        <ul class="pagination justify-content-center">

                            <c:forEach
                                begin="1"
                                end="${totalPages}"
                                var="i">

                                <c:url
                                    var="pageUrl"
                                    value="/admin/service-categories">

                                    <c:param
                                        name="page"
                                        value="${i}"/>

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
                                    ${i eq currentPage
                                      ? 'active'
                                      : ''}">

                                    <a
                                        class="page-link"
                                        href="${pageUrl}">

                                        ${i}

                                    </a>

                                </li>

                            </c:forEach>

                        </ul>

                    </nav>

                </c:if>

            </div>

        </div>

    </main>

</div>

</body>
</html>