<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<%@ taglib prefix="c"
           uri="jakarta.tags.core"%>

<div class="sidebar bg-dark text-white vh-100">

    <div class="p-3 border-bottom">
        <h5 class="text-center mb-0">
            <i class="bi bi-film"></i>
            Movie Ticket
        </h5>
    </div>

    <ul class="nav flex-column p-2">

        <li class="nav-item">
    		<a class="nav-link
       			${pageTitle == 'Dashboard' ? 'active' : ''}"
       			href="${pageContext.request.contextPath}/admin/dashboard">
        		<i class="bi bi-speedometer2"></i>
        		Dashboard
    		</a>
		</li>

        <li class="nav-item">
    		<a class="nav-link
       			${pageTitle == 'Quản lý phim' ? 'active' : ''}"
       			href="${pageContext.request.contextPath}/admin/movies">
        		<i class="bi bi-film"></i>
        		Quản lý phim
    		</a>
		</li>

        <li class="nav-item">
            <a class="nav-link text-white"
               href="${pageContext.request.contextPath}/admin/rooms">
                <i class="bi bi-building"></i>
                Quản lý phòng
            </a>
        </li>

        <li class="nav-item">
            <a class="nav-link text-white"
               href="${pageContext.request.contextPath}/admin/showtimes">
                <i class="bi bi-camera-reels"></i>
                Suất chiếu
            </a>
        </li>

        <!-- Nhóm dịch vụ: rê chuột vào sẽ hiện "Món dịch vụ" bên dưới -->
        <li class="nav-item sidebar-group">

            <a class="nav-link text-white"
               href="${pageContext.request.contextPath}/admin/service-categories">
                <i class="bi bi-tags"></i>
                Nhóm dịch vụ
            </a>

            <ul class="nav flex-column sidebar-submenu">

                <li class="nav-item">
                    <a class="nav-link text-white"
                       href="${pageContext.request.contextPath}/admin/services">
                        Món dịch vụ
                    </a>
                </li>

            </ul>

        </li>

        <!-- TODO: chưa có servlet /admin/tickets -->
        <li class="nav-item">
            <a class="nav-link text-white"
               href="${pageContext.request.contextPath}/admin/tickets">
                <i class="bi bi-ticket-perforated"></i>
                Quản lý vé
            </a>
        </li>

        <li class="nav-item">
            <a class="nav-link text-white"
               href="${pageContext.request.contextPath}/admin/users">
                <i class="bi bi-people"></i>
                Người dùng
            </a>
        </li>

          <!-- TODO: chưa có servlet /admin/settings -->
        <li class="nav-item">
            <a class="nav-link text-white"
               href="${pageContext.request.contextPath}/admin/payments">
                <i class="bi bi-gear"></i>
                Thanh Toán
            </a>
        </li>

    </ul>

</div>
