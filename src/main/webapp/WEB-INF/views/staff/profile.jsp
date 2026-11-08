<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<link rel="stylesheet"
      href="${pageContext.request.contextPath}/assets/css/admin.css">

<body>

<jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

<div class="d-flex">

    <jsp:include page="/WEB-INF/views/common/sidebar-staff.jsp"/>

    <div class="flex-grow-1 p-4">

        <!-- Thông tin cá nhân -->
        <div class="card shadow">

            <div class="card-header bg-dark text-white">

                <h3 class="mb-0">
                    <i class="bi bi-person-circle"></i>
                    Thông tin cá nhân
                </h3>

            </div>

            <div class="card-body">

                <c:if test="${param.success=='updated'}">
                    <div class="alert alert-success">
                        Cập nhật thông tin thành công.
                    </div>
                </c:if>

                <c:if test="${param.success=='password'}">
                    <div class="alert alert-success">
                        Đổi mật khẩu thành công.
                    </div>
                </c:if>

                <c:if test="${not empty errorMessage}">
                    <div class="alert alert-danger">
                        ${errorMessage}
                    </div>
                </c:if>

                <form action="${pageContext.request.contextPath}/staff/profile"
                      method="post">

                    <div class="row">

                        <div class="col-md-6 mb-3">

                            <label class="form-label">
                                Username
                            </label>

                            <input
                                    type="text"
                                    class="form-control"
                                    value="${user.username}"
                                    readonly>

                        </div>

                        <div class="col-md-6 mb-3">

                            <label class="form-label">
                                Vai trò
                            </label>

                            <input
                                    type="text"
                                    class="form-control"
                                    value="${user.role}"
                                    readonly>

                        </div>

                    </div>

                    <div class="mb-3">

                        <label class="form-label">
                            Họ và tên
                        </label>

                        <input
                                type="text"
                                name="fullName"
                                class="form-control"
                                value="${user.fullName}"
                                required>

                    </div>

                    <div class="mb-3">

                        <label class="form-label">
                            Email
                        </label>

                        <input
                                type="email"
                                name="email"
                                class="form-control"
                                value="${user.email}"
                                required>

                    </div>

                    <div class="mb-3">

                        <label class="form-label">
                            Số điện thoại
                        </label>

                        <input
                                type="text"
                                name="phone"
                                class="form-control"
                                value="${user.phone}">

                    </div>

                    <button
                            class="btn btn-primary">

                        <i class="bi bi-save"></i>

                        Cập nhật thông tin

                    </button>

                </form>

            </div>

        </div>

        <!-- Đổi mật khẩu -->

        <div class="card shadow mt-4">

            <div class="card-header bg-warning">

                <h3 class="mb-0">

                    <i class="bi bi-key"></i>

                    Đổi mật khẩu

                </h3>

            </div>

            <div class="card-body">

                <c:if test="${not empty passwordError}">

                    <div class="alert alert-danger">

                        ${passwordError}

                    </div>

                </c:if>

                <form
                        action="${pageContext.request.contextPath}/staff/change-password"
                        method="post">

                    <div class="mb-3">

                        <label class="form-label">

                            Mật khẩu hiện tại

                        </label>

                        <input
                                type="password"
                                name="oldPassword"
                                class="form-control"
                                required>

                    </div>

                    <div class="mb-3">

                        <label class="form-label">

                            Mật khẩu mới

                        </label>

                        <input
                                type="password"
                                name="newPassword"
                                class="form-control"
                                minlength="6"
                                required>

                    </div>

                    <div class="mb-3">

                        <label class="form-label">

                            Xác nhận mật khẩu

                        </label>

                        <input
                                type="password"
                                name="confirmPassword"
                                class="form-control"
                                minlength="6"
                                required>

                    </div>

                    <button
                            class="btn btn-warning">

                        <i class="bi bi-arrow-repeat"></i>

                        Đổi mật khẩu

                    </button>

                    <a
                            href="${pageContext.request.contextPath}/staff/dashboard"
                            class="btn btn-secondary">

                      
                    </a>

                </form>

            </div>

        </div>

    </div>

</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>

</body>
</html>