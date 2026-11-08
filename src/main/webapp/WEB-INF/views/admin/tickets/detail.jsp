<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<body>

<jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

<link rel="stylesheet"
      href="${pageContext.request.contextPath}/assets/css/admin.css">

<div class="d-flex">

    <jsp:include page="/WEB-INF/views/common/sidebar.jsp"/>

    <div class="flex-grow-1 p-4">

        <div class="card shadow">

            <div class="card-header bg-primary text-white">

                <h3>Ticket Detail</h3>

            </div>

            <div class="card-body">

                <table class="table table-bordered">

                    <tr>
                        <th width="220">Ticket ID</th>
                        <td>${ticket.ticketId}</td>
                    </tr>

                    <tr>
                        <th>Booking Code</th>
                        <td>${ticket.bookingCode}</td>
                    </tr>

                    <tr>
                        <th>Customer</th>
                        <td>${ticket.customerName}</td>
                    </tr>

                    <tr>
                        <th>Movie</th>
                        <td>${ticket.movieTitle}</td>
                    </tr>

                    <tr>
                        <th>Room</th>
                        <td>${ticket.roomName}</td>
                    </tr>

                    <tr>
                        <th>Showtime</th>
                        <td>${ticket.startTime}</td>
                    </tr>

                    <tr>
                        <th>People</th>
                        <td>${ticket.peopleCount}</td>
                    </tr>

                    <tr>
                        <th>Total Price</th>
                        <td>${ticket.totalPrice}</td>
                    </tr>

                    <tr>
                        <th>Status</th>
                        <td>

                            <span class="badge bg-success">

                                ${ticket.status}

                            </span>

                        </td>
                    </tr>

                    <tr>
                        <th>Created At</th>
                        <td>${ticket.createdAt}</td>
                    </tr>

                </table>

                <a href="${pageContext.request.contextPath}/admin/tickets"
                   class="btn btn-secondary">

                    Back

                </a>

            </div>

        </div>

    </div>

</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>

</body>

</html>