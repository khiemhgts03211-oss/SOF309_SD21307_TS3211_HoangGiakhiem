<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<%@ taglib prefix="c"
           uri="jakarta.tags.core"%>

<nav class="navbar navbar-expand-lg navbar-dark bg-dark shadow-sm">

    <div class="container-fluid">

       <a class="navbar-brand fw-bold d-flex align-items-center"
   href="${pageContext.request.contextPath}/">

    <img
        src="${pageContext.request.contextPath}/assets/images/logo.png"
        alt="Movie Ticket Management"
        height="48"
        class="me-2">

</a>

        <!-- Mobile Button -->
        <button class="navbar-toggler"
                type="button"
                data-bs-toggle="collapse"
                data-bs-target="#navbarContent">

            <span class="navbar-toggler-icon"></span>

        </button>

        <div class="collapse navbar-collapse"
             id="navbarContent">

            <!-- Menu -->
            <ul class="navbar-nav me-auto">

                <li class="nav-item">

                    <a class="nav-link
                       ${pageTitle == 'Trang chủ' ? 'active' : ''}"
                       href="${pageContext.request.contextPath}/">

                        <i class="bi bi-house-door"></i>

                        Trang chủ

                    </a>

                </li>

                <li class="nav-item">

                    <a id="navPhimLink" class="nav-link
                       ${pageTitle == 'Phim' ? 'active' : ''}"
                       href="${pageContext.request.contextPath}/home#now-showing">

                        <i class="bi bi-film"></i>

                        Phim

                    </a>

                </li>

                <c:if test="${sessionScope.user.role == 'ADMIN'}">

                    <li class="nav-item">

                        <a class="nav-link"
                           href="${pageContext.request.contextPath}/admin/dashboard">

                            <i class="bi bi-speedometer2"></i>

                            Dashboard

                        </a>

                    </li>

                </c:if>

                <c:if test="${sessionScope.user.role == 'STAFF'}">

                    <li class="nav-item">

                        <a class="nav-link"
                           href="${pageContext.request.contextPath}/staff/dashboard">

                            <i class="bi bi-speedometer2"></i>

                            Dashboard

                        </a>

                    </li>

                </c:if>

            </ul>

            <!-- User -->
            <ul class="navbar-nav ms-auto align-items-center">

                <c:choose>

                    <c:when test="${not empty sessionScope.user}">

                        <li class="nav-item me-3">

                            <span class="navbar-text">

                                <i class="bi bi-person-circle"></i>

                                Xin chào,

                                <strong>

                                    ${sessionScope.user.fullName}

                                </strong>

                            </span>

                        </li>

                        <li class="nav-item">

                            <a class="btn btn-outline-light btn-sm"
                               href="${pageContext.request.contextPath}/logout">

                                <i class="bi bi-box-arrow-right"></i>

                                Đăng xuất

                            </a>

                        </li>

                    </c:when>

                    <c:otherwise>

                        <li class="nav-item">

                            <a class="btn btn-outline-light btn-sm me-2"
                               href="${pageContext.request.contextPath}/login">

                                Đăng nhập

                            </a>

                        </li>

                        <li class="nav-item">

                            <a class="btn btn-warning btn-sm"
                               href="${pageContext.request.contextPath}/register">

                                Đăng ký

                            </a>

                        </li>

                    </c:otherwise>

                </c:choose>

            </ul>

        </div>

    </div>

</nav>

<script>
document.addEventListener('DOMContentLoaded', function () {

    var phimLink = document.getElementById('navPhimLink');
    var nowShowingSection = document.getElementById('now-showing');

    /*
     * Nếu đang đứng sẵn ở trang chủ (có #now-showing trên trang),
     * chặn hành vi mặc định và cuộn mượt xuống đó thay vì tải lại trang.
     * Nếu đang ở trang khác, để link chạy bình thường
     * (sẽ tải /home#now-showing, trình duyệt tự cuộn tới đúng chỗ).
     */
    if (phimLink && nowShowingSection) {

        phimLink.addEventListener('click', function (e) {

            e.preventDefault();

            nowShowingSection.scrollIntoView({
                behavior: 'smooth',
                block: 'start'
            });

        });

    }

});
</script>
