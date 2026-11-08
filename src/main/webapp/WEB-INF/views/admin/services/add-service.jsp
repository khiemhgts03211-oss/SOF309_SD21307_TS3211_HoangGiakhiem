<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<jsp:include page="/WEB-INF/views/common/header.jsp" />

<body>

<jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

<div class="d-flex">

    <jsp:include page="/WEB-INF/views/common/sidebar.jsp"/>

    <div class="flex-grow-1 p-4">

        <div class="card shadow">

            <div class="card-header bg-dark text-white">
                <h3 class="mb-0">Thêm dịch vụ</h3>
            </div>

            <div class="card-body">

                <!-- Thông báo lỗi -->
                <c:if test="${not empty error}">
                    <div class="alert alert-danger">
                        ${error}
                    </div>
                </c:if>

                <form action="${pageContext.request.contextPath}/admin/services/add"
                      method="post">

                    <!-- Tên dịch vụ -->
                    <div class="mb-3">
                        <label class="form-label">
                            Tên dịch vụ
                        </label>

                        <input
                            type="text"
                            name="serviceName"
                            class="form-control"
                            placeholder="Nhập tên dịch vụ"
                            required>
                    </div>

                    <!-- Danh mục -->
                    <div class="mb-3">
                        <label class="form-label">
                            Danh mục
                        </label>

                        <select
                            name="categoryId"
                            class="form-select"
                            required>

                            <option value="">
                                -- Chọn danh mục --
                            </option>

                            <c:if test="${not empty categories}">
                                <c:forEach var="c" items="${categories}">
                                    <option value="${c.categoryId}">
                                        ${c.categoryName}
                                    </option>
                                </c:forEach>
                            </c:if>

                        </select>
                    </div>

                    <!-- Giá -->
                    <div class="mb-3">

                        <label class="form-label">
                            Giá
                        </label>

                        <input
                            type="number"
                            name="price"
                            class="form-control"
                            min="0"
                            step="1000"
                            placeholder="Ví dụ: 50000"
                            required>

                    </div>

                    <!-- Trạng thái -->
                    <div class="mb-4">

                        <label class="form-label">
                            Trạng thái
                        </label>

                        <select
                            name="status"
                            class="form-select">

                            <option value="ACTIVE">
                                Hoạt động
                            </option>

                            <option value="INACTIVE">
                                Ngừng hoạt động
                            </option>

                        </select>

                    </div>

                    <!-- Button -->
                    <div class="d-flex gap-2">

                        <button
                            type="submit"
                            class="btn btn-success">

                            <i class="bi bi-plus-circle"></i>

                            Thêm dịch vụ

                        </button>

                        <a
                            href="${pageContext.request.contextPath}/admin/services"
                            class="btn btn-secondary">

                            Quay lại

                        </a>

                    </div>

                </form>

            </div>

        </div>

    </div>

</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>

</body>
</html>