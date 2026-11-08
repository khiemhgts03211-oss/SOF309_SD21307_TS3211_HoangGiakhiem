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
       			href="${pageContext.request.contextPath}/staff/dashboard">
        		<i class="bi bi-speedometer2"></i>
        		Dashboard
    		</a>
		</li>

        <li class="nav-item">
    		<a class="nav-link
       			${pageTitle == 'Bán dịch vụ' ? 'active' : ''}"
       			href="${pageContext.request.contextPath}/staff/sell-service">
        		<i class="bi bi-cup-straw"></i>
        		Bán dịch vụ
    		</a>
		</li>

        <li class="nav-item">
            <a class="nav-link text-white"
               href="${pageContext.request.contextPath}/staff/checkin">
                <i class="bi bi-qr-code-scan"></i>
                Check-in vé
            </a>
        </li>

        <li class="nav-item">
            <a class="nav-link text-white"
               href="${pageContext.request.contextPath}/staff/showtimes">
                <i class="bi bi-camera-reels"></i>
                Suất chiếu hôm nay
            </a>
        </li>

        <li class="nav-item">
            <a class="nav-link text-white"
               href="${pageContext.request.contextPath}/staff/search-ticket">
                <i class="bi bi-search"></i>
                Tìm vé
            </a>
        </li>

        <li class="nav-item">
            <a class="nav-link text-white"
               href="${pageContext.request.contextPath}/staff/today-payments">
                <i class="bi bi-credit-card"></i>
                Thanh toán hôm nay
            </a>
        </li>

        <li class="nav-item">
            <a class="nav-link text-white"
               href="${pageContext.request.contextPath}/staff/print-ticket">
                <i class="bi bi-printer"></i>
                In vé
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link text-white"
               href="${pageContext.request.contextPath}/staff/profile">
                <i class="bi bi-credit-card"></i>
                Profile 
            </a>
        </li>
                <li class="nav-item">
            <a class="nav-link text-white"
               href="${pageContext.request.contextPath}/staff/revenue">
                <i class="bi bi-credit-card"></i>
                Doanh Thu
            </a>
        </li>
          <li class="nav-item">
            <a class="nav-link
               ${pageTitle == 'Danh sách thanh toán' ? 'active' : ''}"
               href="${pageContext.request.contextPath}/staff/payments">
                <i class="bi bi-credit-card"></i>
                Payments
            </a>
        </li>
          <li class="nav-item">
            <a class="nav-link
               ${pageTitle == 'Danh sách khách hàng' ? 'active' : ''}"
               href="${pageContext.request.contextPath}/staff/customers">
                <i class="bi bi-credit-card"></i>
                Customers
            </a>
        </li>
          <li class="nav-item">
            <a class="nav-link text-white"
               href="${pageContext.request.contextPath}/staff/bookings">
                <i class="bi bi-credit-card"></i>
               Booking
            </a>
        </li>
    </ul>

</div>
