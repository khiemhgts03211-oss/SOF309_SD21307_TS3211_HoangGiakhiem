<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<body>

<jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin.css">

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

<div class="d-flex">

    <jsp:include page="/WEB-INF/views/common/sidebar.jsp"/>

    <div class="flex-grow-1 p-4">

<h2 class="mb-4">
Trang Chủ Admin
</h2>

<div class="row g-4">

    <div class="col-xl-3 col-lg-3 col-md-6 col-sm-6 mb-4">
        <div class="card text-center p-3">
            <h6>Tổng Users</h6>
            <h2 id="userCount">${users}</h2>
        </div>
    </div>

    <div class="col-md-3">
        <div class="card text-center p-3">
            <h6>Tổng Phim</h6>
            <h2 id="movieCount">${movies}</h2>
        </div>
    </div>

    <div class="col-md-3">
        <div class="card text-center p-3">
            <h6>Tổng Phòng</h6>
            <h2 id="roomCount">${rooms}</h2>
        </div>
    </div>

    <div class="col-md-3">
        <div class="card text-center p-3">
            <h6>Tổng Suất Chiếu</h6>
			<h2 id="showtimeCount">${showtimes}</h2>
        </div>
    </div>

</div>

<div class="row mt-4">

    <div class="col-lg-6 mb-4">

        <div class="card p-4">

            <h5>Tổng Vé</h5>

            <h1 class="text-primary">

                ${tickets}

            </h1>

        </div>

    </div>

    <div class="col-md-6">

        <div class="card p-4">

            <h5>Doanh Thu</h5>

            <h1 class="text-success">

                ${revenue} VNĐ

            </h1>

        </div>

    </div>

</div>

<div class="card mb-4 p-4">

<h4 class="mb-3">

Doanh thu theo tháng

</h4>

<canvas id="revenueChart"></canvas>

</div>

<script>

const revenueData = {

<c:forEach var="item" items="${revenueByMonth}">

"${item.key}": ${item.value},

</c:forEach>

};

const labels = Object.keys(revenueData);

const values = Object.values(revenueData);

new Chart(document.getElementById("revenueChart"),{

type:"bar",

data:{

labels:labels,

datasets:[{

label:"Doanh thu",

data:values,

borderWidth:1

}]

},

options:{

responsive:true,

plugins:{

legend:{

display:true

}

},

scales:{

y:{

beginAtZero:true

}

}

}

});

</script>

<div class="card mt-5 p-4">

    <h4 class="mb-3">
        Vé bán theo tháng
    </h4>

    <canvas id="ticketChart"></canvas>

<script>

const ticketData = {

<c:forEach var="item" items="${ticketByMonth}">

"${item.key}": ${item.value},

</c:forEach>

};

const ticketLabels = Object.keys(ticketData);

const ticketValues = Object.values(ticketData);

new Chart(document.getElementById("ticketChart"),{

    type:"line",

    data:{

        labels:ticketLabels,

        datasets:[{

            label:"Số vé",

            data:ticketValues,

            fill:false,

            tension:0.3

        }]

    },

    options:{

        responsive:true,

        scales:{

            y:{

                beginAtZero:true

            }

        }

    }

});

</script>

</div>

<div class="card mt-5 p-4">

    <h4 class="mb-3">
        Top phim bán vé
    </h4>

    <canvas id="movieChart"></canvas>

<script>

const movieData = {

<c:forEach var="item" items="${movieChart}">

"${item.key}": ${item.value},

</c:forEach>

};

const movieLabels = Object.keys(movieData);

const movieValues = Object.values(movieData);

new Chart(
    document.getElementById("movieChart"),
{
    type:"pie",

    data:{
        labels:movieLabels,

        datasets:[{

            data:movieValues

        }]
    },

    options:{
        responsive:true
    }

});

</script>

</div>

<div class="card mt-5">

    <div class="card-header">

        <h4>
            Recent Bookings
        </h4>

    </div>

    <div class="table-responsive">

        <table class="table table-hover table-striped mb-0">

            <thead class="table-dark">

                <tr>

                    <th>ID</th>
                    <th>Booking Code</th>
                    <th>Customer</th>
                    <th>Movie</th>
                    <th>Total</th>
                    <th>Status</th>
                    <th>Created</th>

                </tr>

            </thead>

            <tbody>

                <c:forEach var="b"
                           items="${recentBookings}">

                    <tr>

                        <td>${b.ticketId}</td>

                        <td>${b.bookingCode}</td>

                        <td>${b.customerName}</td>

                        <td>${b.movieTitle}</td>

                        <td>${b.totalPrice}</td>

                        <td>

                            <span class="badge bg-success">

                                ${b.status}

                            </span>

                        </td>

                        <td>${b.createdAt}</td>

                    </tr>

                </c:forEach>

            </tbody>

        </table>

    </div>

<script>

fetch("${pageContext.request.contextPath}/api/dashboard")
.then(res => res.json())
.then(data => {

    document.getElementById("userCount").innerText = data.users;
    document.getElementById("movieCount").innerText = data.movies;
    document.getElementById("roomCount").innerText = data.rooms;
    document.getElementById("ticketCount").innerText = data.tickets;

});

</script>

</div>

    </div>

</div>

</body>
</html>
