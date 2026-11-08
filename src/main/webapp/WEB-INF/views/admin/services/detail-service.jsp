<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

<div class="d-flex">

    <jsp:include page="/WEB-INF/views/common/sidebar.jsp"/>

    <div class="flex-grow-1 p-4">

        <div class="card shadow">

            <div class="card-header bg-dark text-white">

                <h3>Service Detail</h3>

            </div>

            <div class="card-body">

                <table class="table table-bordered">

                    <tr>
                        <th width="220">Service ID</th>
                        <td>${service.serviceId}</td>
                    </tr>

                    <tr>
                        <th>Category</th>
                        <td>${service.categoryName}</td>
                    </tr>

                    <tr>
                        <th>Service Name</th>
                        <td>${service.serviceName}</td>
                    </tr>

                    <tr>
                        <th>Price</th>
                        <td>${service.price}</td>
                    </tr>

                    <tr>
                        <th>Status</th>
                        <td>
                            <span class="badge ${service.status=='ACTIVE'?'bg-success':'bg-danger'}">
                                ${service.status}
                            </span>
                        </td>
                    </tr>

                    <tr>
                        <th>Created At</th>
                        <td>${service.createdAt}</td>
                    </tr>

                </table>

                <a
                    href="${pageContext.request.contextPath}/admin/services"
                    class="btn btn-secondary">

                    Back

                </a>

            </div>

        </div>

    </div>

</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>