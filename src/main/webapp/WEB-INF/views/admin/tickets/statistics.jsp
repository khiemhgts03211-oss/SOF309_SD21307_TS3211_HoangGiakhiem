<%@ page contentType="text/html;charset=UTF-8"%>

<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<body>

<jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

<div class="d-flex">

<jsp:include page="/WEB-INF/views/common/sidebar.jsp"/>

<div class="container-fluid p-4">

<div class="row">

    <div class="col-md-3">

        <div class="card bg-primary text-white">

            <div class="card-body">

                <h5>Total Tickets</h5>

                <h2>${totalTickets}</h2>

            </div>

        </div>

    </div>

    <div class="col-md-3">

        <div class="card bg-warning text-white">

            <div class="card-body">

                <h5>Booked</h5>

                <h2>${bookedTickets}</h2>

            </div>

        </div>

    </div>

    <div class="col-md-3">

        <div class="card bg-success text-white">

            <div class="card-body">

                <h5>Paid</h5>

                <h2>${paidTickets}</h2>

            </div>

        </div>

    </div>

    <div class="col-md-3">

        <div class="card bg-danger text-white">

            <div class="card-body">

                <h5>Revenue</h5>

                <h2>${revenue}</h2>

            </div>

        </div>

    </div>

</div>

</div>

</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>

</body>

</html>