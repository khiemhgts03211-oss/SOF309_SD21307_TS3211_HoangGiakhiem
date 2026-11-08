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

            <div class="card-header bg-primary text-white">

                <div class="d-flex
                            justify-content-between
                            align-items-center
                            flex-wrap
                            gap-2">

                    <div>

                        <h3 class="mb-1">
                            Quản Lý Thanh Toán
                        </h3>

                        <small>
                            Tìm kiếm, lọc và cập nhật giao dịch
                        </small>

                    </div>

                    <span class="badge bg-light text-primary fs-6">

                        ${empty totalItems ? 0 : totalItems}
                        giao dịch

                    </span>

                </div>

            </div>

            <div class="card-body">

                <%-- =================================================
                     THÔNG BÁO
                     ================================================= --%>

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

                <c:if test="${param.success eq 'updated'}">

                    <div class="alert alert-success alert-dismissible fade show">

                        Cập nhật trạng thái thanh toán thành công.

                        <button type="button"
                                class="btn-close"
                                data-bs-dismiss="alert">
                        </button>

                    </div>

                </c:if>

                <c:if test="${param.error eq 'invalidId'}">

                    <div class="alert alert-warning alert-dismissible fade show">

                        Mã thanh toán không hợp lệ.

                        <button type="button"
                                class="btn-close"
                                data-bs-dismiss="alert">
                        </button>

                    </div>

                </c:if>

                <c:if test="${param.error eq 'invalidStatus'}">

                    <div class="alert alert-warning alert-dismissible fade show">

                        Trạng thái thanh toán không hợp lệ.

                        <button type="button"
                                class="btn-close"
                                data-bs-dismiss="alert">
                        </button>

                    </div>

                </c:if>

                <c:if test="${param.error eq 'updateFailed'}">

                    <div class="alert alert-danger alert-dismissible fade show">

                        Không thể cập nhật trạng thái thanh toán.

                        <button type="button"
                                class="btn-close"
                                data-bs-dismiss="alert">
                        </button>

                    </div>

                </c:if>

                <c:if test="${param.error eq 'server'}">

                    <div class="alert alert-danger alert-dismissible fade show">

                        Đã xảy ra lỗi hệ thống.

                        <button type="button"
                                class="btn-close"
                                data-bs-dismiss="alert">
                        </button>

                    </div>

                </c:if>

                <%-- =================================================
                     SEARCH + FILTER + SORT
                     ================================================= --%>

                <form action="${pageContext.request.contextPath}/admin/payments"
                      method="get"
                      class="mb-4">

                    <div class="row g-3 align-items-end">

                        <div class="col-lg-3 col-md-6">

                            <label for="keyword"
                                   class="form-label">

                                Từ khóa

                            </label>

                            <input type="text"
                                   id="keyword"
                                   name="keyword"
                                   class="form-control"
                                   maxlength="150"
                                   value="<c:out value='${keyword}'/>"
                                   placeholder="Mã giao dịch, mã đặt vé...">

                        </div>

                        <div class="col-lg-2 col-md-3">

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

                                <option value="PENDING"
                                    <c:if test="${selectedStatus eq 'PENDING'}">
                                        selected
                                    </c:if>>

                                    PENDING

                                </option>

                                <option value="PAID"
                                    <c:if test="${selectedStatus eq 'PAID'}">
                                        selected
                                    </c:if>>

                                    PAID

                                </option>

                                <option value="FAILED"
                                    <c:if test="${selectedStatus eq 'FAILED'}">
                                        selected
                                    </c:if>>

                                    FAILED

                                </option>

                                <option value="CANCELLED"
                                    <c:if test="${selectedStatus eq 'CANCELLED'}">
                                        selected
                                    </c:if>>

                                    CANCELLED

                                </option>

                            </select>

                        </div>

                        <div class="col-lg-2 col-md-3">

                            <label for="method"
                                   class="form-label">

                                Phương thức

                            </label>

                            <select id="method"
                                    name="method"
                                    class="form-select">

                                <option value="">
                                    Tất cả phương thức
                                </option>

                                <option value="CASH"
                                    <c:if test="${selectedMethod eq 'CASH'}">
                                        selected
                                    </c:if>>

                                    CASH

                                </option>

                                <option value="BANKING"
                                    <c:if test="${selectedMethod eq 'BANKING'}">
                                        selected
                                    </c:if>>

                                    BANKING

                                </option>

                                <option value="MOMO"
                                    <c:if test="${selectedMethod eq 'MOMO'}">
                                        selected
                                    </c:if>>

                                    MOMO

                                </option>

                                <option value="VNPAY"
                                    <c:if test="${selectedMethod eq 'VNPAY'}">
                                        selected
                                    </c:if>>

                                    VNPAY

                                </option>

                            </select>

                        </div>

                        <div class="col-lg-2 col-md-4">

                            <label for="sort"
                                   class="form-label">

                                Sắp xếp

                            </label>

                            <select id="sort"
                                    name="sort"
                                    class="form-select">

                                <option value="">
                                    Mặc định
                                </option>

                                <option value="newest"
                                    <c:if test="${sort eq 'newest'}">
                                        selected
                                    </c:if>>

                                    Mới nhất

                                </option>

                                <option value="oldest"
                                    <c:if test="${sort eq 'oldest'}">
                                        selected
                                    </c:if>>

                                    Cũ nhất

                                </option>

                                <option value="amount_asc"
                                    <c:if test="${sort eq 'amount_asc'
                                                or sort eq 'amountasc'}">
                                        selected
                                    </c:if>>

                                    Tiền tăng dần

                                </option>

                                <option value="amount_desc"
                                    <c:if test="${sort eq 'amount_desc'
                                                or sort eq 'amountdesc'}">
                                        selected
                                    </c:if>>

                                    Tiền giảm dần

                                </option>

                                <option value="transaction_asc"
                                    <c:if test="${sort eq 'transaction_asc'}">
                                        selected
                                    </c:if>>

                                    Mã giao dịch A-Z

                                </option>

                                <option value="transaction_desc"
                                    <c:if test="${sort eq 'transaction_desc'}">
                                        selected
                                    </c:if>>

                                    Mã giao dịch Z-A

                                </option>

                            </select>

                        </div>

                        <div class="col-lg-3 col-md-8">

                            <div class="d-flex gap-2">

                                <button type="submit"
                                        class="btn btn-primary flex-grow-1">

                                    Tìm kiếm

                                </button>

                                <a href="${pageContext.request.contextPath}/admin/payments"
                                   class="btn btn-outline-secondary">

                                    Làm mới

                                </a>

                            </div>

                        </div>

                    </div>

                </form>

                <%-- =================================================
                     EXPORT
                     ================================================= --%>

                <div class="mb-4">

                    <c:url var="exportUrl"
                           value="/admin/payments/export">

                        <c:param name="keyword"
                                 value="${keyword}"/>

                        <c:param name="status"
                                 value="${selectedStatus}"/>

                        <c:param name="method"
                                 value="${selectedMethod}"/>

                        <c:param name="sort"
                                 value="${sort}"/>

                    </c:url>

                    <a href="${exportUrl}"
                       class="btn btn-success w-100">

                        Export CSV

                    </a>

                </div>

                <%-- =================================================
                     KẾT QUẢ
                     ================================================= --%>

                <c:if test="${hasCriteria}">

                    <div class="alert alert-info">

                        Tìm thấy

                        <strong>
                            ${empty totalItems ? 0 : totalItems}
                        </strong>

                        giao dịch phù hợp.

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

                        <c:if test="${not empty selectedMethod}">

                            <span class="ms-2">

                                Phương thức:

                                <strong>
                                    <c:out value="${selectedMethod}"/>
                                </strong>

                            </span>

                        </c:if>

                    </div>

                </c:if>

                <%-- =================================================
                     TABLE
                     ================================================= --%>

                <div class="table-responsive">

                    <table class="table
                                  table-bordered
                                  table-hover
                                  align-middle">

                        <thead class="table-dark">

                            <tr>

                                <th>ID</th>
                                <th>Transaction</th>
                                <th>Booking</th>
                                <th>Customer</th>
                                <th>Movie</th>
                                <th>Method</th>
                                <th class="text-end">Amount</th>
                                <th>Status</th>
                                <th>Paid At</th>
                                <th style="min-width: 250px;">
                                    Action
                                </th>

                            </tr>

                        </thead>

                        <tbody>

                            <c:choose>

                                <%-- Không có dữ liệu --%>

                                <c:when test="${empty payments}">

                                    <tr>

                                        <td colspan="10"
                                            class="text-center text-muted py-5">

                                            <c:choose>

                                                <c:when test="${hasCriteria}">

                                                    Không tìm thấy giao dịch phù hợp.

                                                </c:when>

                                                <c:otherwise>

                                                    Chưa có dữ liệu thanh toán.

                                                </c:otherwise>

                                            </c:choose>

                                        </td>

                                    </tr>

                                </c:when>

                                <%-- Có dữ liệu --%>

                                <c:otherwise>

                                    <c:forEach items="${payments}"
                                               var="payment">

                                        <tr>

                                            <td>
                                                <c:out value="${payment.paymentId}"/>
                                            </td>

                                            <td>

                                                <c:choose>

                                                    <c:when test="${not empty payment.transactionCode}">

                                                        <strong>

                                                            <c:out
                                                                value="${payment.transactionCode}"/>

                                                        </strong>

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

                                                    <c:when test="${not empty payment.bookingCode}">

                                                        <c:out
                                                            value="${payment.bookingCode}"/>

                                                    </c:when>

                                                    <c:otherwise>

                                                        Ticket #${payment.ticketId}

                                                    </c:otherwise>

                                                </c:choose>

                                            </td>

                                            <td>

                                                <c:choose>

                                                    <c:when test="${not empty payment.customerName}">

                                                        <c:out
                                                            value="${payment.customerName}"/>

                                                    </c:when>

                                                    <c:otherwise>

                                                        <span class="text-muted">
                                                            Không xác định
                                                        </span>

                                                    </c:otherwise>

                                                </c:choose>

                                            </td>

                                            <td>

                                                <c:choose>

                                                    <c:when test="${not empty payment.movieTitle}">

                                                        <c:out
                                                            value="${payment.movieTitle}"/>

                                                    </c:when>

                                                    <c:otherwise>

                                                        <span class="text-muted">
                                                            Không xác định
                                                        </span>

                                                    </c:otherwise>

                                                </c:choose>

                                            </td>

                                            <td>

                                                <c:choose>

                                                    <c:when test="${payment.paymentMethod eq 'CASH'}">

                                                        <span class="badge bg-secondary">
                                                            CASH
                                                        </span>

                                                    </c:when>

                                                    <c:when test="${payment.paymentMethod eq 'BANKING'}">

                                                        <span class="badge bg-primary">
                                                            BANKING
                                                        </span>

                                                    </c:when>

                                                    <c:when test="${payment.paymentMethod eq 'MOMO'}">

                                                        <span class="badge bg-danger">
                                                            MOMO
                                                        </span>

                                                    </c:when>

                                                    <c:when test="${payment.paymentMethod eq 'VNPAY'}">

                                                        <span class="badge bg-info text-dark">
                                                            VNPAY
                                                        </span>

                                                    </c:when>

                                                    <c:otherwise>

                                                        <span class="badge bg-dark">

                                                            <c:out
                                                                value="${payment.paymentMethod}"/>

                                                        </span>

                                                    </c:otherwise>

                                                </c:choose>

                                            </td>

                                            <td class="text-end fw-bold">

                                                <c:choose>

                                                    <c:when test="${not empty payment.amount}">

                                                        <fmt:formatNumber
                                                            value="${payment.amount}"
                                                            type="number"
                                                            groupingUsed="true"/>

                                                        VNĐ

                                                    </c:when>

                                                    <c:otherwise>
                                                        0 VNĐ
                                                    </c:otherwise>

                                                </c:choose>

                                            </td>

                                            <td>

                                                <c:choose>

                                                    <c:when test="${payment.status eq 'PAID'}">

                                                        <span class="badge bg-success">
                                                            PAID
                                                        </span>

                                                    </c:when>

                                                    <c:when test="${payment.status eq 'PENDING'}">

                                                        <span class="badge bg-warning text-dark">
                                                            PENDING
                                                        </span>

                                                    </c:when>

                                                    <c:when test="${payment.status eq 'FAILED'}">

                                                        <span class="badge bg-danger">
                                                            FAILED
                                                        </span>

                                                    </c:when>

                                                    <c:when test="${payment.status eq 'CANCELLED'}">

                                                        <span class="badge bg-secondary">
                                                            CANCELLED
                                                        </span>

                                                    </c:when>

                                                    <c:otherwise>

                                                        <span class="badge bg-dark">

                                                            <c:out
                                                                value="${payment.status}"/>

                                                        </span>

                                                    </c:otherwise>

                                                </c:choose>

                                            </td>

                                            <td>

                                                <c:choose>

                                                    <c:when test="${not empty payment.paidAt}">

                                                        <fmt:formatDate
                                                            value="${payment.paidAt}"
                                                            pattern="dd/MM/yyyy HH:mm:ss"/>

                                                    </c:when>

                                                    <c:otherwise>

                                                        <span class="text-muted">
                                                            Chưa thanh toán
                                                        </span>

                                                    </c:otherwise>

                                                </c:choose>

                                            </td>

                                            <td>

                                                <div class="d-flex
                                                            flex-column
                                                            gap-2">

                                                    <a
                                                        href="${pageContext.request.contextPath}/admin/payments/detail?id=${payment.paymentId}"
                                                        class="btn btn-sm btn-info">

                                                        Chi tiết

                                                    </a>

                                                    <form
                                                        action="${pageContext.request.contextPath}/admin/payments/update-status"
                                                        method="post"
                                                        class="d-flex gap-1">

                                                        <input
                                                            type="hidden"
                                                            name="paymentId"
                                                            value="${payment.paymentId}">

                                                        <input
                                                            type="hidden"
                                                            name="id"
                                                            value="${payment.paymentId}">

                                                        <select
                                                            name="status"
                                                            class="form-select form-select-sm">

                                                            <option value="PENDING"
                                                                <c:if test="${payment.status eq 'PENDING'}">
                                                                    selected
                                                                </c:if>>

                                                                PENDING

                                                            </option>

                                                            <option value="PAID"
                                                                <c:if test="${payment.status eq 'PAID'}">
                                                                    selected
                                                                </c:if>>

                                                                PAID

                                                            </option>

                                                            <option value="FAILED"
                                                                <c:if test="${payment.status eq 'FAILED'}">
                                                                    selected
                                                                </c:if>>

                                                                FAILED

                                                            </option>

                                                            <option value="CANCELLED"
                                                                <c:if test="${payment.status eq 'CANCELLED'}">
                                                                    selected
                                                                </c:if>>

                                                                CANCELLED

                                                            </option>

                                                        </select>

                                                        <button
                                                            type="submit"
                                                            class="btn btn-sm btn-warning"
                                                            onclick="return confirm('Bạn có chắc muốn cập nhật trạng thái?');">

                                                            Update

                                                        </button>

                                                    </form>

                                                </div>

                                            </td>

                                        </tr>

                                    </c:forEach>

                                </c:otherwise>

                            </c:choose>

                        </tbody>

                    </table>

                </div>

                <%-- =================================================
                     PAGINATION
                     ================================================= --%>

                <c:if test="${totalPages > 1}">

                    <nav aria-label="Payment pagination">

                        <ul class="pagination
                                   justify-content-center
                                   flex-wrap">

                            <c:url var="previousUrl"
                                   value="/admin/payments">

                                <c:param name="page"
                                         value="${currentPage - 1}"/>

                                <c:param name="keyword"
                                         value="${keyword}"/>

                                <c:param name="status"
                                         value="${selectedStatus}"/>

                                <c:param name="method"
                                         value="${selectedMethod}"/>

                                <c:param name="sort"
                                         value="${sort}"/>

                            </c:url>

                            <li class="page-item
                                ${currentPage <= 1
                                    ? 'disabled'
                                    : ''}">

                                <a class="page-link"
                                   href="${currentPage <= 1
                                         ? '#'
                                         : previousUrl}">

                                    Trước

                                </a>

                            </li>

                            <c:forEach begin="1"
                                       end="${totalPages}"
                                       var="pageNumber">

                                <c:url var="pageUrl"
                                       value="/admin/payments">

                                    <c:param name="page"
                                             value="${pageNumber}"/>

                                    <c:param name="keyword"
                                             value="${keyword}"/>

                                    <c:param name="status"
                                             value="${selectedStatus}"/>

                                    <c:param name="method"
                                             value="${selectedMethod}"/>

                                    <c:param name="sort"
                                             value="${sort}"/>

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

                            <c:url var="nextUrl"
                                   value="/admin/payments">

                                <c:param name="page"
                                         value="${currentPage + 1}"/>

                                <c:param name="keyword"
                                         value="${keyword}"/>

                                <c:param name="status"
                                         value="${selectedStatus}"/>

                                <c:param name="method"
                                         value="${selectedMethod}"/>

                                <c:param name="sort"
                                         value="${sort}"/>

                            </c:url>

                            <li class="page-item
                                ${currentPage >= totalPages
                                    ? 'disabled'
                                    : ''}">

                                <a class="page-link"
                                   href="${currentPage >= totalPages
                                         ? '#'
                                         : nextUrl}">

                                    Sau

                                </a>

                            </li>

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