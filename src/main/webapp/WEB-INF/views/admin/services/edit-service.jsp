<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<jsp:include page="/WEB-INF/views/common/header.jsp" />
<jsp:include page="/WEB-INF/views/common/navbar.jsp" />

<div class="d-flex">

    <jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

    <div class="flex-grow-1 p-4">

        <div class="card shadow">

            <div class="card-header bg-dark text-white">

                <h3>Edit Service</h3>

            </div>

            <div class="card-body">

                <c:if test="${not empty error}">
                    <div class="alert alert-danger">
                        ${error}
                    </div>
                </c:if>

                <form method="post"
                      action="${pageContext.request.contextPath}/admin/services/edit">

                    <input type="hidden"
                           name="serviceId"
                           value="${service.serviceId}">

                    <div class="mb-3">

                        <label class="form-label">
                            Service Name
                        </label>

                        <input
                            type="text"
                            class="form-control"
                            name="serviceName"
                            value="${service.serviceName}"
                            required>

                    </div>

                    <div class="mb-3">

                        <label class="form-label">
                            Category
                        </label>

                        <select
                            class="form-select"
                            name="categoryId"
                            required>

                            <c:forEach
                                items="${categories}"
                                var="c">

                                <option
                                    value="${c.categoryId}"
                                    ${c.categoryId == service.categoryId ? 'selected' : ''}>

                                    ${c.categoryName}

                                </option>

                            </c:forEach>

                        </select>

                    </div>

                    <div class="mb-3">

                        <label class="form-label">
                            Price
                        </label>

                        <input
                            type="number"
                            class="form-control"
                            name="price"
                            min="0"
                            step="1000"
                            value="${service.price}"
                            required>

                    </div>

                    <div class="mb-3">

                        <label class="form-label">
                            Status
                        </label>

                        <select
                            class="form-select"
                            name="status">

                            <option
                                value="ACTIVE"
                                ${service.status == 'ACTIVE' ? 'selected' : ''}>

                                ACTIVE

                            </option>

                            <option
                                value="INACTIVE"
                                ${service.status == 'INACTIVE' ? 'selected' : ''}>

                                INACTIVE

                            </option>

                        </select>

                    </div>

                    <button
                        type="submit"
                        class="btn btn-primary">

                        Update Service

                    </button>

                    <a
                        href="${pageContext.request.contextPath}/admin/services"
                        class="btn btn-secondary">

                        Back

                    </a>

                </form>

            </div>

        </div>

    </div>

</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />