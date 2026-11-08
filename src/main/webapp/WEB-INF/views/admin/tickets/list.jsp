<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<link rel="stylesheet"
      href="${pageContext.request.contextPath}/assets/css/admin.css">

<body>

<jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

<div class="d-flex">

    <jsp:include page="/WEB-INF/views/common/sidebar.jsp"/>

    <div class="flex-grow-1 p-4">

        <div class="card shadow">

            <div class="card-header bg-dark text-white">

                <h3 class="mb-0">
                    Ticket Management
                </h3>

            </div>

            <div class="card-body">

                <!-- Search -->
                <form method="get"
                      action="${pageContext.request.contextPath}/admin/tickets"
                      class="row mb-3">

                    <div class="col-md-5">

                        <input
                            type="text"
                            name="keyword"
                            class="form-control"
                            placeholder="Search booking code, customer, movie..."
                            value="${keyword}">

                    </div>

                    <div class="col-md-2">

                        <button class="btn btn-primary w-100">
                            Search
                        </button>

                    </div>

<a class="btn btn-success"
   href="${pageContext.request.contextPath}/admin/tickets/export">

    Export Excel

</a>

                </form>

                <!-- Filter -->
                <form method="get"
                      action="${pageContext.request.contextPath}/admin/tickets"
                      class="row mb-3">

                    <div class="col-md-4">

                        <select
                            class="form-select"
                            name="status">

                            <option value="">All Status</option>

                            <option value="BOOKED"
                                ${status=="BOOKED"?"selected":""}>
                                BOOKED
                            </option>

                            <option value="PAID"
                                ${status=="PAID"?"selected":""}>
                                PAID
                            </option>

                            <option value="CANCELLED"
                                ${status=="CANCELLED"?"selected":""}>
                                CANCELLED
                            </option>

                        </select>

                    </div>

                    <div class="col-md-2">

                        <button class="btn btn-warning w-100">
                            Filter
                        </button>

                    </div>

                </form>

                <!-- Sort -->
                <form method="get"
                      action="${pageContext.request.contextPath}/admin/tickets"
                      class="row mb-3">

                    <div class="col-md-4">

                        <select
                            name="sort"
                            class="form-select">

                            <option value="">-- Sort By --</option>

                            <option value="booking_code">
                                Booking Code
                            </option>

                            <option value="created_at">
                                Created Date
                            </option>

                            <option value="total_price">
                                Total Price
                            </option>

                        </select>

                    </div>

                    <div class="col-md-3">

                        <select
                            name="order"
                            class="form-select">

                            <option value="ASC">
                                Ascending
                            </option>

                            <option value="DESC">
                                Descending
                            </option>

                        </select>

                    </div>

                    <div class="col-md-2">

                        <button class="btn btn-success w-100">
                            Sort
                        </button>

                    </div>

                </form>

                <!-- Table -->

                <div class="table-responsive">

                    <table class="table table-bordered table-hover">

                        <thead class="table-dark">

                        <tr>

                            <th>ID</th>
                            <th>Booking Code</th>
                            <th>Customer</th>
                            <th>Movie</th>
                            <th>Room</th>
                            <th>Showtime</th>
                            <th>People</th>
                            <th>Total Price</th>
                            <th>Status</th>
                            <th>Created At</th>
                            <th width="320">Action</th>

                        </tr>

                        </thead>

                        <tbody>

                        <c:forEach items="${tickets}" var="t">

                            <tr>

                                <td>${t.ticketId}</td>
                                <td>${t.bookingCode}</td>
                                <td>${t.customerName}</td>
                                <td>${t.movieTitle}</td>
                                <td>${t.roomName}</td>
                                <td>${t.showtime}</td>
                                <td>${t.peopleCount}</td>
                                <td>${t.totalPrice}</td>

                                <td>

                                    <span class="badge
                                    ${t.status=='BOOKED'?'bg-primary':
                                      t.status=='PAID'?'bg-success':
                                      'bg-danger'}">

                                        ${t.status}

                                    </span>

                                </td>

                                <td>${t.createdAt}</td>

                                <td>

                                    <a class="btn btn-info btn-sm"
                                       href="${pageContext.request.contextPath}/admin/tickets/detail?id=${t.ticketId}">

                                        Detail

                                    </a>

                                    <form method="post"
                                          action="${pageContext.request.contextPath}/admin/tickets/update-status"
                                          style="display:inline;">

                                        <input type="hidden"
                                               name="id"
                                               value="${t.ticketId}">

                                        <select name="status"
                                                class="form-select form-select-sm d-inline"
                                                style="width:120px;">

                                            <option value="BOOKED"
                                                ${t.status=="BOOKED"?"selected":""}>
                                                BOOKED
                                            </option>

                                            <option value="PAID"
                                                ${t.status=="PAID"?"selected":""}>
                                                PAID
                                            </option>

                                            <option value="CANCELLED"
                                                ${t.status=="CANCELLED"?"selected":""}>
                                                CANCELLED
                                            </option>

                                        </select>

                                        <button class="btn btn-success btn-sm">

                                            Save

                                        </button>

                                    </form>

                                    <a class="btn btn-danger btn-sm"
                                       href="${pageContext.request.contextPath}/admin/tickets/delete?id=${t.ticketId}"
                                       onclick="return confirm('Are you sure you want to delete this ticket?');">

                                        Delete

                                    </a>

                                </td>

                            </tr>

                        </c:forEach>

                        </tbody>

                    </table>

                </div>

                <!-- Pagination -->

                <nav>

                    <ul class="pagination justify-content-center">

                        <c:if test="${currentPage > 1}">

                            <li class="page-item">

                                <a class="page-link"
                                   href="${pageContext.request.contextPath}/admin/tickets?page=${currentPage-1}">

                                    Previous

                                </a>

                            </li>

                        </c:if>

                        <c:forEach begin="1"
                                   end="${totalPage}"
                                   var="i">

                            <li class="page-item ${i==currentPage?'active':''}">

                                <a class="page-link"
                                   href="${pageContext.request.contextPath}/admin/tickets?page=${i}">

                                    ${i}

                                </a>

                            </li>

                        </c:forEach>

                        <c:if test="${currentPage < totalPage}">

                            <li class="page-item">

                                <a class="page-link"
                                   href="${pageContext.request.contextPath}/admin/tickets?page=${currentPage+1}">

                                    Next

                                </a>

                            </li>

                        </c:if>

                    </ul>

                </nav>

            </div>

        </div>

    </div>

</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>

</body>
</html>