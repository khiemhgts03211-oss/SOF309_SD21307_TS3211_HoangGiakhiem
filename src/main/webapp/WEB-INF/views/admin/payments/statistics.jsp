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

        <h2 class="mb-4">
            Payment Statistics
        </h2>

        <div class="row g-4">

            <!-- Total Payments -->
            <div class="col-md-4">

                <div class="card border-primary shadow h-100">

                    <div class="card-body text-center">

                        <h5 class="card-title text-primary">
                            Total Payments
                        </h5>

                        <h2 class="fw-bold">
                            ${totalPayments}
                        </h2>

                    </div>

                </div>

            </div>

            <!-- Paid Payments -->
            <div class="col-md-4">

                <div class="card border-success shadow h-100">

                    <div class="card-body text-center">

                        <h5 class="card-title text-success">
                            Paid Payments
                        </h5>

                        <h2 class="fw-bold">
                            ${paidPayments}
                        </h2>

                    </div>

                </div>

            </div>

            <!-- Pending Payments -->
            <div class="col-md-4">

                <div class="card border-warning shadow h-100">

                    <div class="card-body text-center">

                        <h5 class="card-title text-warning">
                            Pending Payments
                        </h5>

                        <h2 class="fw-bold">
                            ${pendingPayments}
                        </h2>

                    </div>

                </div>

            </div>

            <!-- Total Revenue -->
            <div class="col-md-4">

                <div class="card border-danger shadow h-100">

                    <div class="card-body text-center">

                        <h5 class="card-title text-danger">
                            Total Revenue
                        </h5>

                        <h2 class="fw-bold">
                            ${totalRevenue}
                        </h2>

                    </div>

                </div>

            </div>

            <!-- Today Revenue -->
            <div class="col-md-4">

                <div class="card border-info shadow h-100">

                    <div class="card-body text-center">

                        <h5 class="card-title text-info">
                            Today Revenue
                        </h5>

                        <h2 class="fw-bold">
                            ${todayRevenue}
                        </h2>

                    </div>

                </div>

            </div>

            <!-- Month Revenue -->
            <div class="col-md-4">

                <div class="card border-dark shadow h-100">

                    <div class="card-body text-center">

                        <h5 class="card-title">
                            This Month Revenue
                        </h5>

                        <h2 class="fw-bold">
                            ${monthRevenue}
                        </h2>

                    </div>

                </div>

            </div>

            <!-- Year Revenue -->
            <div class="col-md-12">

                <div class="card shadow">

                    <div class="card-body text-center">

                        <h4 class="text-secondary">
                            Revenue This Year
                        </h4>

                        <h1 class="fw-bold text-success">
                            ${yearRevenue}
                        </h1>

                    </div>

                </div>

            </div>

        </div>

        <div class="mt-4">

            <a href="${pageContext.request.contextPath}/admin/payments"
               class="btn btn-secondary">

                Back to Payment List

            </a>

        </div>

    </div>

</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>

</body>
</html>