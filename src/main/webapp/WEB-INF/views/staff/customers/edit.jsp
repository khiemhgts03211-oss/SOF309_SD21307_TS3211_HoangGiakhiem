<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<!DOCTYPE html>
<html lang="vi">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1">

    <title>Cập nhật khách hàng</title>

    <link
        href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
        rel="stylesheet">

    <style>

        body {
            background-color: #f4f6f9;
        }

        .edit-card {
            border: none;
            border-radius: 14px;
            overflow: hidden;
        }

        .page-title {
            font-weight: 700;
        }

        .readonly-field {
            background-color: #e9ecef;
        }

    </style>

</head>

<body>

<div class="container py-4">

    <div class="d-flex flex-wrap
                justify-content-between
                align-items-center
                gap-3 mb-4">

        <div>

            <h2 class="page-title mb-1">
                Cập nhật khách hàng
            </h2>

            <p class="text-muted mb-0">
                Chỉnh sửa thông tin liên hệ của khách hàng
            </p>

        </div>

        <a
            href="${pageContext.request.contextPath}/staff/customers"
            class="btn btn-outline-secondary">

            Quay lại danh sách

        </a>

    </div>

    <c:if test="${not empty errorMessage}">

        <div class="alert alert-danger
                    alert-dismissible fade show">

            <c:out value="${errorMessage}"/>

            <button type="button"
                    class="btn-close"
                    data-bs-dismiss="alert">
            </button>

        </div>

    </c:if>

    <c:choose>

        <c:when test="${empty customer}">

            <div class="alert alert-warning">
                Không tìm thấy thông tin khách hàng.
            </div>

        </c:when>

        <c:otherwise>

            <div class="card shadow-sm edit-card">

                <div class="card-header bg-primary text-white py-3">

                    <h5 class="mb-0">
                        Thông tin tài khoản
                    </h5>

                </div>

                <div class="card-body p-4">

                    <form
                        action="${pageContext.request.contextPath}/staff/customers/edit"
                        method="post">

                        <input
                            type="hidden"
                            name="userId"
                            value="${customer.userId}">

                        <div class="row g-3">

                            <div class="col-md-6">

                                <label class="form-label">
                                    User ID
                                </label>

                                <input
                                    type="text"
                                    class="form-control readonly-field"
                                    value="${customer.userId}"
                                    readonly>

                            </div>

                            <div class="col-md-6">

                                <label class="form-label">
                                    Username
                                </label>

                                <input
                                    type="text"
                                    class="form-control readonly-field"
                                    value="<c:out value='${customer.username}'/>"
                                    readonly>

                            </div>

                            <div class="col-md-6">

                                <label class="form-label">
                                    Vai trò
                                </label>

                                <input
                                    type="text"
                                    class="form-control readonly-field"
                                    value="<c:out value='${customer.role}'/>"
                                    readonly>

                            </div>

                            <div class="col-md-6">

                                <label class="form-label">
                                    Trạng thái
                                </label>

                                <input
                                    type="text"
                                    class="form-control readonly-field"
                                    value="<c:out value='${customer.status}'/>"
                                    readonly>

                            </div>

                            <div class="col-12">

                                <label for="fullName"
                                       class="form-label">

                                    Họ và tên
                                    <span class="text-danger">*</span>

                                </label>

                                <input
                                    type="text"
                                    id="fullName"
                                    name="fullName"
                                    class="form-control"
                                    maxlength="100"
                                    value="<c:out value='${customer.fullName}'/>"
                                    required>

                            </div>

                            <div class="col-md-6">

                                <label for="email"
                                       class="form-label">

                                    Email
                                    <span class="text-danger">*</span>

                                </label>

                                <input
                                    type="email"
                                    id="email"
                                    name="email"
                                    class="form-control"
                                    maxlength="150"
                                    value="<c:out value='${customer.email}'/>"
                                    required>

                            </div>

                            <div class="col-md-6">

                                <label for="phone"
                                       class="form-label">

                                    Số điện thoại

                                </label>

                                <input
                                    type="text"
                                    id="phone"
                                    name="phone"
                                    class="form-control"
                                    minlength="9"
                                    maxlength="11"
                                    pattern="[0-9]{9,11}"
                                    value="<c:out value='${customer.phone}'/>"
                                    placeholder="Ví dụ: 0912345678">

                                <div class="form-text">
                                    Nhập từ 9 đến 11 chữ số.
                                </div>

                            </div>

                        </div>

                        <hr class="my-4">

                        <div class="d-flex
                                    justify-content-end
                                    gap-2">

                            <a
                                href="${pageContext.request.contextPath}/staff/customers/detail?id=${customer.userId}"
                                class="btn btn-outline-secondary">

                                Hủy

                            </a>

                            <button
                                type="submit"
                                class="btn btn-primary"
                                onclick="return confirm('Lưu thay đổi thông tin khách hàng?');">

                                Lưu thay đổi

                            </button>

                        </div>

                    </form>

                </div>

            </div>

        </c:otherwise>

    </c:choose>

</div>

<script
    src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js">
</script>

</body>

</html>