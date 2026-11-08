<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<body>

<jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin.css">

<div class="d-flex">

    <jsp:include page="/WEB-INF/views/common/sidebar.jsp"/>

    <div class="flex-grow-1 p-4">

<div class="card shadow">

<div class="card-header bg-dark text-white">

<div class="d-flex
justify-content-between
align-items-center
flex-wrap">

<h3 class="mb-2 mb-md-0">

Edit Showtime

</h3>

<a href="${pageContext.request.contextPath}/admin/showtimes" class="btn btn-secondary btn-sm">

<i class="bi bi-arrow-left"></i> Back

</a>

</div>

</div>

<div class="card-body">

<form method="post"
      action="${pageContext.request.contextPath}/admin/showtimes/edit"
      class="row g-3">

    <input type="hidden" name="showtimeId" value="${showtime.showtimeId}">

    <div class="col-md-6">

        <label class="form-label">Movie</label>

        <select name="movieId" class="form-select" required>

            <c:forEach var="m" items="${movies}">

                <option value="${m.movieId}"
                    ${m.movieId==showtime.movieId?'selected':''}>

                    ${m.title}

                </option>

            </c:forEach>

        </select>

    </div>

    <div class="col-md-6">

        <label class="form-label">Room</label>

        <select name="roomId" class="form-select" required>

            <c:forEach var="r" items="${rooms}">

                <option value="${r.roomId}"
                    ${r.roomId==showtime.roomId?'selected':''}>

                    ${r.roomName}

                </option>

            </c:forEach>

        </select>

    </div>

    <div class="col-md-4">

        <label class="form-label">Show Date</label>

        <input
            type="date"
            name="showDate"
            class="form-control"
            value="<fmt:formatDate value='${showtime.showDate}' pattern='yyyy-MM-dd'/>"
            required>

    </div>

    <div class="col-md-4">

        <label class="form-label">Start Time</label>

        <input
            type="time"
            name="startTime"
            class="form-control"
            value="<fmt:formatDate value='${showtime.startTime}' pattern='HH:mm'/>"
            required>

    </div>

    <div class="col-md-4">

        <label class="form-label">End Time</label>

        <input
            type="time"
            name="endTime"
            class="form-control"
            value="<fmt:formatDate value='${showtime.endTime}' pattern='HH:mm'/>"
            required>

    </div>

    <div class="col-md-6">

        <label class="form-label">Ticket Price</label>

        <input
            type="number"
            step="1000"
            name="ticketPrice"
            class="form-control"
            value="${showtime.ticketPrice}"
            required>

    </div>

    <div class="col-md-6">

        <label class="form-label">Status</label>

        <select name="status" class="form-select">

            <option value="OPEN" ${showtime.status=='OPEN'?'selected':''}>OPEN</option>
            <option value="CLOSED" ${showtime.status=='CLOSED'?'selected':''}>CLOSED</option>
            <option value="CANCELLED" ${showtime.status=='CANCELLED'?'selected':''}>CANCELLED</option>

        </select>

    </div>

    <div class="col-12 mt-4">

        <button type="submit" class="btn btn-warning">

            <i class="bi bi-save"></i> Update

        </button>

        <a href="${pageContext.request.contextPath}/admin/showtimes" class="btn btn-outline-secondary">

            Cancel

        </a>

    </div>

</form>

</div>

</div>

    </div>

</div>

</body>
</html>
