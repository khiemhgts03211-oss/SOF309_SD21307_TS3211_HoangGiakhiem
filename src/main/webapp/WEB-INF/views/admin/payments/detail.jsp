<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
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

            <div class="card-header bg-primary text-white">

                <h3>Payment Detail</h3>

            </div>

            <div class="card-body">

                <table class="table table-bordered">

                    <tr>
                        <th width="250">Payment ID</th>
                        <td>${payment.paymentId}</td>
                    </tr>

                    <tr>
                        <th>Transaction Code</th>
                        <td>${payment.transactionCode}</td>
                    </tr>

                    <tr>
                        <th>Booking Code</th>
                        <td>${payment.bookingCode}</td>
                    </tr>

                    <tr>
                        <th>Customer</th>
                        <td>${payment.customerName}</td>
                    </tr>

                    <tr>
                        <th>Movie</th>
                        <td>${payment.movieTitle}</td>
                    </tr>

                    <tr>
                        <th>Room</th>
                        <td>${payment.roomName}</td>
                    </tr>

                    <tr>
                        <th>Showtime</th>
                        <td>${payment.showtime}</td>
                    </tr>

                    <tr>
                        <th>Payment Method</th>
                        <td>${payment.paymentMethod}</td>
                    </tr>

                    <tr>
                        <th>Amount</th>
                        <td>${payment.amount}</td>
                    </tr>

                    <tr>
                        <th>Status</th>
                        <td>

                            <span class="badge
                            ${payment.status=='PAID'?'bg-success':
                              payment.status=='PENDING'?'bg-warning':
                              'bg-danger'}">

                                ${payment.status}

                            </span>

                        </td>
                    </tr>

                    <tr>
                        <th>Paid At</th>
                        <td>${payment.paidAt}</td>
                    </tr>

                </table>

                <a href="${pageContext.request.contextPath}/admin/payments"
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