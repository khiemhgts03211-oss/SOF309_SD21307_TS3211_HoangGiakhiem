<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

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

Add Showtime

</h3>

<a href="${pageContext.request.contextPath}/admin/showtimes" class="btn btn-secondary btn-sm">

<i class="bi bi-arrow-left"></i> Back

</a>

</div>

</div>

<div class="card-body">

<c:if test="${not empty error}">

<div class="alert alert-danger">

${error}

</div>

</c:if>

<form method="post"
      action="${pageContext.request.contextPath}/admin/showtimes/add"
      class="row g-3">

    <div class="col-md-6">

        <label class="form-label">Movie</label>

        <select name="movieId" class="form-select" required>

            <option value="">-- Chọn phim --</option>

            <c:forEach var="m" items="${movies}">

                <option value="${m.movieId}">${m.title}</option>

            </c:forEach>

        </select>

    </div>

    <div class="col-md-6">

        <label class="form-label">Room</label>

        <select name="roomId" class="form-select" required>

            <option value="">-- Chọn phòng --</option>

            <c:forEach var="r" items="${rooms}">

                <option value="${r.roomId}">${r.roomName}</option>

            </c:forEach>

        </select>

    </div>

    <div class="col-md-4">

        <label class="form-label">Show Date</label>

        <input
            type="date"
            name="showDate"
            class="form-control"
            required>

    </div>

    <div class="col-md-4">

        <label class="form-label">Start Time</label>

        <input
            type="time"
            name="startTime"
            class="form-control"
            required>

    </div>

    <div class="col-md-4">

        <label class="form-label">End Time</label>

        <input
            type="time"
            name="endTime"
            class="form-control"
            required>

    </div>

    <div class="col-md-6">

        <label class="form-label">Ticket Price</label>

        <input
            type="number"
            step="1000"
            name="ticketPrice"
            class="form-control"
            required>

    </div>

    <div class="col-md-6">

        <label class="form-label">Status</label>

        <select name="status" class="form-select">

            <option value="OPEN">OPEN</option>
            <option value="CLOSED">CLOSED</option>
            <option value="CANCELLED">CANCELLED</option>

        </select>

    </div>

    <div class="col-12 mt-4">

        <button type="submit" class="btn btn-success">

            <i class="bi bi-save"></i> Save

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
